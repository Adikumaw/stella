package com.nothing.ecommerce.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nothing.ecommerce.dao.UserAddressRepository;
import com.nothing.ecommerce.dao.UserRepository;
import com.nothing.ecommerce.entity.User;
import com.nothing.ecommerce.entity.UserAddress;
import com.nothing.ecommerce.miscellaneous.*;

import com.nothing.ecommerce.exception.UserExistException;

@Service
public class UserService {

    // user contains { userId, userName, emial, number, active }
    private UserRepository userRepository;
    // userAddress contains { userId, streetAddress, city, state, postalCode,
    // country }
    private UserAddressRepository userAddressRepository;
    // passwordEncoder is used to encode the password
    private PasswordEncoder passwordEncoder;

    // Injecting repositories ...
    public UserService(UserRepository userRepository, UserAddressRepository userAddressRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userAddressRepository = userAddressRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Returns the user details for a given user ID.
     *
     * @param userId the ID of the user
     * @return the user details as a string
     */
    public String getUser(int userId) {
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
        }
        return UserDetailsString;
    }

    /**
     * Saves the given user details to the database.
     *
     * @param userDetails the user details to be saved
     * @return the saved user details
     */
    public User registerUser(User user) {
        String encryptedPassword;
        verifyNewUser(user);
        if (user.getPassword() != null || user.getPassword() != "") {
            encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword("{bcrypt}" + encryptedPassword);
        } else {
            throw new IllegalArgumentException("empty password field");
        }
        return userRepository.save(user);
    }

    /**
     * Saves the given user details to the database.
     *
     * @param userAddress the user details to be saved
     * @return the saved user details
     */
    public UserAddress saveUserAddress(UserAddress userAddress) {
        return userAddressRepository.save(userAddress);
    }

    // checks if user exists or not using user Id ...
    public boolean isUserExist(int userId) {
        Optional<User> OpUserDetails = userRepository.findById(userId);
        if (OpUserDetails.isPresent()) {
            return true;
        }
        return false;
    }

    public void verifyNewUser(User user) {
        int userId = user.getUserId();
        String name = user.getUserName();
        String email = user.getEmail();
        String number = user.getNumber();

        // checking user identity
        if (isUserExist(userId)) {
            throw new UserExistException("user already exists");
        }
        // checking user name
        if (name == null || name == "") {
            System.out.println("throw new IllegalStateException invalid user name");
            throw new IllegalArgumentException("Invalid user name");
        }
        // checking user number
        checkNumber(number);
        // checking user email
        if (email == null || email == "") {
            user.setEmail(null);
        } else {
            checkEmail(email);
        }
    }

    public void checkNumber(String number) {
        System.out.println("checking mobile number ...");
        if (!Miscellaneous.verifyMobileNumber(number)) {
            System.out.println("throw new IllegalArgumentException for wrong mobile number");
            throw new IllegalArgumentException("Invalid phone number");
        }

        User userDetails = userRepository.findByNumber(number);
        if (userDetails != null) {
            System.out.println("user already exists {check your phone number}");
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