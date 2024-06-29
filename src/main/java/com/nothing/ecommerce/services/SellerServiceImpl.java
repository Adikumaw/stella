package com.nothing.ecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;

import com.nothing.ecommerce.entity.Roles;
import com.nothing.ecommerce.entity.Seller;
import com.nothing.ecommerce.entity.User;
import com.nothing.ecommerce.entity.VerificationToken;
import com.nothing.ecommerce.exception.InvalidSellerAddressException;
import com.nothing.ecommerce.exception.InvalidStoreNameException;
import com.nothing.ecommerce.model.SellerInputModel;
import com.nothing.ecommerce.model.UserInputModel;
import com.nothing.ecommerce.repository.RolesRepository;
import com.nothing.ecommerce.repository.SellerRepository;

public class SellerServiceImpl implements SellerService {

    @Autowired
    private UserAdvanceService userAdvanceService;
    @Autowired
    private UserService userService;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private SellerRepository sellerRepository;

    @Override
    public boolean register(SellerInputModel model) {
        // vrify seller details
        verifySellerDetails(model);

        UserInputModel userModel = convertSellerToUserInputModel(model);
        userAdvanceService.register(userModel);

        int userId = userService.findUserIdByEmail(model.getEmail());

        Seller seller = new Seller(userId, model.getStoreName(), model.getAddress());
        seller = sellerRepository.save(seller);

        // create new seller role
        Roles roles = new Roles(userId, "ROLE_SELLER");
        rolesRepository.save(roles);

        return (seller != null) ? true : false;
    }

    private void verifySellerDetails(SellerInputModel model) {
        if (model.getStoreName() == null || model.getStoreName().isEmpty()) {
            throw new InvalidStoreNameException("Error: Empty Store name");
        }
        if (model.getAddress() == null || model.getAddress().isEmpty()) {
            throw new InvalidSellerAddressException("Error: Address must be specified");
        }
    }

    @Override
    public boolean verifyUpdate(String token) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verifyUpdate'");
    }

    private UserInputModel convertSellerToUserInputModel(SellerInputModel model) {
        UserInputModel userInputModel = new UserInputModel(model.getName(), model.getEmail(), model.getNumber(),
                model.getPassword());

        return userInputModel;
    }

}
