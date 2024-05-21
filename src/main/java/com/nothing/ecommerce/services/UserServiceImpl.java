package com.nothing.ecommerce.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nothing.ecommerce.entity.User;
import com.nothing.ecommerce.entity.Address;
import com.nothing.ecommerce.miscellaneous.*;
import com.nothing.ecommerce.model.AddressModel;
import com.nothing.ecommerce.model.UserModel;
import com.nothing.ecommerce.repository.AddressRepository;
import com.nothing.ecommerce.repository.UserRepository;
import com.nothing.ecommerce.exception.AddressNotFoundException;
import com.nothing.ecommerce.exception.UserExistException;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private AddressRepository addressRepository;
    private PasswordEncoder passwordEncoder;

    // Injecting repositories ...
    public UserServiceImpl(UserRepository userRepository, AddressRepository addressRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ----------------------------------------------------------------
    // service methods for user
    // ----------------------------------------------------------------

    @Override
    public boolean registerUser(UserModel userModel) {
        String encryptedPassword;
        verifyNewUser(userModel);

        if (userModel.getPassword() != null || userModel.getPassword() != "") {
            encryptedPassword = passwordEncoder.encode(userModel.getPassword());
            userModel.setPassword(encryptedPassword);
        } else {
            throw new IllegalArgumentException("empty password field");
        }

        User user = new User(userModel);
        user = userRepository.save(user);

        return (user != null) ? true : false;
    }

    @Override
    public User getUser(int userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    @Override
    public User updateUserName(int userId, String name) {
        User fetchedUser = getUser(userId);
        if (fetchedUser != null) {
            fetchedUser.setName(name);
            return userRepository.save(fetchedUser);
        }
        return null;
    }

    @Override
    public User updateUserNumber(int userId, String number) {
        User fetchedUser = getUser(userId);
        boolean verifyNumber = Miscellaneous.verifyMobileNumber(number);
        if (fetchedUser != null && verifyNumber) {
            fetchedUser.setNumber(number);
            return userRepository.save(fetchedUser);
        }
        return null;
    }

    @Override
    public User updateUserEmail(int userId, String email) {
        User fetchedUser = getUser(userId);
        boolean verifyEmail = Miscellaneous.verifyEmail(email);
        if (fetchedUser != null && verifyEmail) {
            fetchedUser.setEmail(email);
            return userRepository.save(fetchedUser);
        }
        return null;
    }

    @Override
    public User updateUserPassword(int userId, String oldPassword, String newPassword) {
        User fetchedUser = getUser(userId);
        if (fetchedUser != null) {
            boolean verifyOldPassword = passwordEncoder.matches(oldPassword, fetchedUser.getPassword());
            if (verifyOldPassword) {
                String encryptedPassword = passwordEncoder.encode(newPassword);
                fetchedUser.setPassword(encryptedPassword);
                return userRepository.save(fetchedUser);
            }
        }
        return null;
    }

    @Override
    public boolean deactivateUser(int userId) {
        User fetchedUser = getUser(userId);
        if (fetchedUser != null) {
            fetchedUser.setActive(0);
            return userRepository.save(fetchedUser) != null;
        }
        return false;
    }

    @Override
    public void removeUser(int userId) {
        // NOTE: remove all the data from tables related to this user before removing
        // the user
        // removing user addresses
        User user = getUser(userId);
        if (user != null) {
            List<Address> addresses = addressRepository.findByUserId(userId);
            for (Address address : addresses) {
                addressRepository.delete(address);
            }

            userRepository.delete(user);
        }
    }

    // ----------------------------------------------------------------
    // service methods for User Address
    // ----------------------------------------------------------------
    @Override
    public Address saveAddress(String reference, AddressModel addressModel) {
        int userId;
        // fetch userId
        if (Miscellaneous.verifyEmail(reference)) {
            userId = userRepository.findUserIdByEmail(reference);
        } else if (Miscellaneous.verifyMobileNumber(reference)) {
            userId = userRepository.findUserIdByNumber(reference);
        } else {
            throw new IllegalArgumentException("Invalid reference: " + reference);
        }

        List<Address> addresses = getUserAddresses(userId);

        // removing main from other addresses
        if (addressModel.getMain() == 1) {
            for (Address addr : addresses) {
                if (addr.getMain() == 1) {
                    // TODO scope for modification for performance
                    addr.setMain(0);
                    addressRepository.save(addr);
                }
            }
        }

        Address address = new Address(userId, addressModel);
        return addressRepository.save(address);
    }

    @Override
    public List<Address> getUserAddresses(int userId) {
        List<Address> fetchedUserAddress = addressRepository.findByUserId(userId);
        if (fetchedUserAddress != null) {
            return fetchedUserAddress;
        }
        throw new AddressNotFoundException("Address not found for user " + userId);
    }

    @Override
    public Address getUserAddress(int userId, String streetAddress) {
        Address fetchedUserAddress = addressRepository.findByUserIdAndStreetAddress(userId, streetAddress);
        if (fetchedUserAddress != null) {
            return fetchedUserAddress;
        }
        throw new AddressNotFoundException("Address not found for user " + userId);
    }

    @Override
    @Transactional
    public Address updateAddress(String streetAddress,
            String city, String state, String postalCode, String country, int main, String oldStreetAddress,
            int userId) {
        Address address = addressRepository.findByUserIdAndStreetAddress(userId, oldStreetAddress);
        if (address != null) {
            addressRepository.updateUserAddress(userId, streetAddress, city, state, postalCode, country, main,
                    oldStreetAddress);

            Address updatedAddress = addressRepository.findByUserIdAndStreetAddress(userId, streetAddress);
            return updatedAddress;
        }
        return null;
    };

    @Override
    public void removeAddress(Address address) {
        Address userAddress = addressRepository.findByUserIdAndStreetAddress(address.getUserId(),
                address.getStreetAddress());
        if (userAddress != null) {
            addressRepository.delete(userAddress);
            // check the deleted userAddress is main and add main to next userAddress if it
            // exists
            if (userAddress.getMain() == 1) {
                List<Address> addresses = addressRepository.findByUserId(address.getUserId());
                if (addresses != null) {
                    Address newPrimaryAddress = addresses.get(0);
                    newPrimaryAddress.setMain(1);
                    addressRepository.save(newPrimaryAddress);
                }
            }
        } else {
            throw new AddressNotFoundException("Address not found for user " + address.getUserId());
        }
    }

    // ----------------------------------------------------------------
    // helper methods
    // ----------------------------------------------------------------

    public String getAllUserDetails(int userId) {
        User user;
        String UserDetailsString = "";

        // fetch the user
        Optional<User> fetchedUser = userRepository.findById(userId);
        List<Address> fetchedUserAddress = addressRepository.findByUserId(userId);

        if (fetchedUser.isPresent()) {
            user = fetchedUser.get();
            UserDetailsString = user.toString();
            for (Address address : fetchedUserAddress) {
                UserDetailsString += " \n" + address.toString();
            }
            return UserDetailsString;
        }
        return "User Not Found ...";
    }

    public boolean isUserExist(int userId) {
        Optional<User> OpUserDetails = userRepository.findById(userId);
        if (OpUserDetails.isPresent()) {
            return true;
        }
        return false;
    }

    public void verifyNewUser(UserModel userModel) {
        String name = userModel.getUserName();
        String email = userModel.getEmail();
        String number = userModel.getNumber();

        // checking user name
        if (name == null || name == "") {
            throw new IllegalArgumentException("Invalid user name");
        }
        // checking user number
        checkNumber(number);
        // checking user email
        if (email == null || email == "") {
            userModel.setEmail(null);
        } else {
            checkEmail(email);
        }
    }

    /**
     * Checks the validity of a given phone number.
     *
     * @param number the phone number to be checked
     * @throws IllegalArgumentException if the phone number is invalid
     * @throws UserExistException       if the user already exists
     */
    public void checkNumber(String number) {
        System.out.println("checking mobile number ...");
        if (!Miscellaneous.verifyMobileNumber(number)) {
            throw new IllegalArgumentException("Invalid phone number");
        }

        User userDetails = userRepository.findByNumber(number);
        if (userDetails != null) {
            throw new UserExistException("phone number already exists, try login");
        }
    }

    public void checkEmail(String email) {
        System.out.println("checking email ...");
        if (!Miscellaneous.verifyEmail(email)) {
            System.out.println("throw new IllegalArgumentException for wrong email Address");
            throw new IllegalArgumentException("Invalid email Address");
        }

        User userDetails = userRepository.findByEmail(email);
        if (userDetails != null) {
            System.out.println("user already exists {check your email}");
            throw new UserExistException("email already exists, try login");
        }
    }

    public User findUser(String toFind) {
        if (Miscellaneous.verifyEmail(toFind)) {
            User userDetails = userRepository.findByEmail(toFind);
            if (userDetails != null) {
                return userDetails;
            } else {
                throw new UserExistException("user not found");
            }
        }
        if (Miscellaneous.verifyMobileNumber(toFind)) {
            User userDetails = userRepository.findByNumber(toFind);
            if (userDetails != null) {
                return userDetails;
            } else {
                throw new UserExistException("user not found");
            }
        }
        throw new IllegalArgumentException("input is not a valid email address or number");
    }

}