package com.nothing.ecommerce.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AopInvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nothing.ecommerce.entity.Roles;
import com.nothing.ecommerce.entity.Seller;
import com.nothing.ecommerce.entity.UpdateVerificationToken;
import com.nothing.ecommerce.entity.User;
import com.nothing.ecommerce.exception.EmptyImagesException;
import com.nothing.ecommerce.exception.ImageException;
import com.nothing.ecommerce.exception.InvalidSellerAddressException;
import com.nothing.ecommerce.exception.InvalidStoreNameException;
import com.nothing.ecommerce.exception.SellerExistsException;
import com.nothing.ecommerce.exception.SellerNotFoundException;
import com.nothing.ecommerce.model.SellerInputModel;
import com.nothing.ecommerce.model.SellerUpgradeModel;
import com.nothing.ecommerce.model.SellerViewModel;
import com.nothing.ecommerce.model.UserInputModel;
import com.nothing.ecommerce.repository.RolesRepository;
import com.nothing.ecommerce.repository.SellerRepository;

@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private UserAdvanceService userAdvanceService;
    @Autowired
    private UserService userService;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private UpdateVerificationTokenService updateVerificationTokenService;
    private final static String path = "/home/all_father/Documents/workshop/java/ecommerce/src/main/resources/static/storeLogos";

    private static final Logger logger = LoggerFactory.getLogger(SellerServiceImpl.class);

    @Override
    public boolean register(SellerInputModel model) {
        // vrify seller details
        verifySellerInputModel(model);

        UserInputModel userModel = new UserInputModel(model);
        userAdvanceService.register(userModel);

        int userId = userService.findUserIdByEmail(model.getEmail());

        Seller seller = new Seller(userId, model.getStoreName(), model.getAddress());
        seller = sellerRepository.save(seller);

        // create new seller role
        Roles roles = new Roles(userId, "ROLE_SELLER");
        rolesRepository.save(roles);

        return (seller != null) ? true : false;
    }

    @Override
    public boolean upgradeToSeller(String reference, SellerUpgradeModel model) {
        // vrify seller details
        verifySellerUpgradeModel(model);

        int userId = userService.findUserIdByReference(reference);

        Seller seller = new Seller(userId, model.getStoreName(), model.getAddress());
        seller = sellerRepository.save(seller);

        // create new seller role
        Roles roles = new Roles(userId, "ROLE_SELLER");
        rolesRepository.save(roles);

        return (seller != null) ? true : false;
    }

    @Override
    public boolean verifyUpdate(String token) {
        // fetch token from Database
        UpdateVerificationToken updateVerificationToken = updateVerificationTokenService.findByToken(token);
        // check if token exist and not expired
        if (updateVerificationTokenService.verify(updateVerificationToken)) {
            // fetch and set updated value to user
            int userId = updateVerificationToken.getUserId();
            String dataWithPrefix = updateVerificationToken.getData();
            String prefix = updateVerificationTokenService.getPrefix(dataWithPrefix);
            String data = updateVerificationTokenService.fetchData(dataWithPrefix);
            Seller seller = sellerRepository.findById(userId).get();
            // check if data is number or email
            if (prefix.equals("storename")) {
                seller.setStoreName(data);
            }
            // Save updated value to seller
            sellerRepository.save(seller);

            // Delete verification token
            updateVerificationTokenService.delete(updateVerificationToken);
            return true;
        }
        try {
            updateVerificationTokenService.delete(updateVerificationToken);
        } catch (InvalidDataAccessApiUsageException e) {
            logger.error("Error deleting verification token: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unknown error: " + e.getMessage(), e);
        }
        return false;
    }

    @Override
    public void updateStoreName(String reference, String storeName) {
        int userId = userService.findUserIdByReference(reference);
        // check if storeName is already userd or not
        if (sellerRepository.existsByStoreName(storeName)) {
            throw new SellerExistsException("Error: " + storeName + " is already used");
        }
        Optional<Seller> optionalSeller = sellerRepository.findById(userId);
        if (optionalSeller.isPresent() && storeName != null && !storeName.isEmpty()) {
            // Generate Verification Token
            updateVerificationTokenService.generate(
                    userId,
                    storeName,
                    "storename");

            // send verification email
            updateVerificationTokenService.sender(reference);
        }
    }

    @Override
    public SellerViewModel updateAddress(String reference, String address) {
        int userId = userService.findUserIdByReference(reference);
        Optional<Seller> optionalSeller = sellerRepository.findById(userId);
        if (optionalSeller.isPresent() && address != null && !address.isEmpty()) {
            Seller seller = optionalSeller.get();
            seller.setAddress(address);
            seller = sellerRepository.save(seller);
            User user = userService.findById(userId);
            return new SellerViewModel(user, seller);
        }
        return null;
    }

    @Override
    public SellerViewModel addLogo(String reference, MultipartFile logoFile) {

        // verify Images input is not empty
        if (logoFile == null || logoFile.isEmpty()) {
            throw new EmptyImagesException("Error: Empty images");
        }

        int userId = userService.findUserIdByReference(reference);

        String imageUrl = imageService.save(userId, logoFile, path);

        if (imageUrl.isEmpty() || imageUrl == null) {
            throw new ImageException("Error: Failed to save Logo");
        }

        Optional<Seller> optionalSeller = sellerRepository.findById(userId);

        if (optionalSeller.isPresent()) {
            Seller seller = optionalSeller.get();
            seller.setLogo(imageUrl);
            sellerRepository.save(seller);
            return new SellerViewModel(userService.findById(userId), seller);
        }
        throw new SellerNotFoundException("Error: you don't have seller account");
    }

    private void verifySellerInputModel(SellerInputModel model) {
        if (model.getStoreName() == null || model.getStoreName().isEmpty()) {
            throw new InvalidStoreNameException("Error: Empty Store name");
        }
        if (model.getAddress() == null || model.getAddress().isEmpty()) {
            throw new InvalidSellerAddressException("Error: Address must be specified");
        }
    }

    private void verifySellerUpgradeModel(SellerUpgradeModel model) {
        if (model.getStoreName() == null || model.getStoreName().isEmpty()) {
            throw new InvalidStoreNameException("Error: Empty Store name");
        }
        if (model.getAddress() == null || model.getAddress().isEmpty()) {
            throw new InvalidSellerAddressException("Error: Address must be specified");
        }
    }

    @Override
    public int findUserIdByStoreName(String storeName) {
        return sellerRepository.findUserIdByStoreName(storeName).orElse(0);
    }
}
