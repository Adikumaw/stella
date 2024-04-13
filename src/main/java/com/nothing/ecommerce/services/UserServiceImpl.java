package com.nothing.ecommerce.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nothing.ecommerce.entity.User;
import com.nothing.ecommerce.entity.UserAddress;
import com.nothing.ecommerce.miscellaneous.*;
import com.nothing.ecommerce.model.UserModel;
import com.nothing.ecommerce.repository.UserAddressRepository;
import com.nothing.ecommerce.repository.UserRepository;
import com.nothing.ecommerce.exception.AddressNotFoundException;
import com.nothing.ecommerce.exception.UserExistException;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserAddressRepository userAddressRepository;
    private PasswordEncoder passwordEncoder;

    // Injecting repositories ...
    public UserServiceImpl(UserRepository userRepository, UserAddressRepository userAddressRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userAddressRepository = userAddressRepository;
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
            userModel.setPassword("{bcrypt}" + encryptedPassword);
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
                fetchedUser.setPassword("{bcrypt}" + encryptedPassword);
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
            List<UserAddress> addresses = userAddressRepository.findByUserId(userId);
            for (UserAddress address : addresses) {
                userAddressRepository.delete(address);
            }

            userRepository.delete(user);
        }
    }

    // ----------------------------------------------------------------
    // service methods for User Address
    // ----------------------------------------------------------------

    public UserAddress saveUserAddress(UserAddress userAddress) {
        List<UserAddress> userAddresses = getUserAddresses(userAddress.getUserId());
        if (userAddresses.size() == 0 || userAddresses == null) {
            userAddress.setMain(1);
        }
        return userAddressRepository.save(userAddress);
    }

    @Override
    public List<UserAddress> getUserAddresses(int userId) {
        List<UserAddress> fetchedUserAddress = userAddressRepository.findByUserId(userId);
        if (fetchedUserAddress != null) {
            return fetchedUserAddress;
        }
        throw new AddressNotFoundException("Address not found for user " + userId);
    }

    @Override
    public UserAddress getUserAddress(int userId, String streetAddress) {
        UserAddress fetchedUserAddress = userAddressRepository.findByUserIdAndStreetAddress(userId, streetAddress);
        if (fetchedUserAddress != null) {
            return fetchedUserAddress;
        }
        throw new AddressNotFoundException("Address not found for user " + userId);
    }

    @Override
    @Transactional
    public UserAddress updateAddress(String streetAddress,
            String city, String state, String postalCode, String country, int main, String oldStreetAddress,
            int userId) {
        UserAddress address = userAddressRepository.findByUserIdAndStreetAddress(userId, oldStreetAddress);
        if (address != null) {
            userAddressRepository.updateUserAddress(userId, streetAddress, city, state, postalCode, country, main,
                    oldStreetAddress);

            UserAddress updatedAddress = userAddressRepository.findByUserIdAndStreetAddress(userId, streetAddress);
            return updatedAddress;
        }
        return null;
    };

    @Override
    public void removeAddress(UserAddress address) {
        UserAddress userAddress = userAddressRepository.findByUserIdAndStreetAddress(address.getUserId(),
                address.getStreetAddress());
        if (userAddress != null) {
            userAddressRepository.delete(userAddress);
            // check the deleted userAddress is main and add main to next userAddress if it
            // exists
            if (userAddress.getMain() == 1) {
                List<UserAddress> addresses = userAddressRepository.findByUserId(address.getUserId());
                if (addresses != null) {
                    UserAddress primaryAddress = addresses.get(0);
                    primaryAddress.setMain(1);
                    userAddressRepository.save(primaryAddress);
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
        List<UserAddress> fetchedUserAddress = userAddressRepository.findByUserId(userId);

        if (fetchedUser.isPresent()) {
            user = fetchedUser.get();
            UserDetailsString = user.toString();
            for (UserAddress address : fetchedUserAddress) {
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
            throw new UserExistException("user Already exists");
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
            throw new UserExistException("user Already exists");
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