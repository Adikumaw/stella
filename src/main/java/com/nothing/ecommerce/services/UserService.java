package com.nothing.ecommerce.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nothing.ecommerce.dao.LoginCredentialsRepository;
import com.nothing.ecommerce.dao.UserAddressRepository;
import com.nothing.ecommerce.dao.UserRepository;
import com.nothing.ecommerce.entity.UserDetails;
import com.nothing.ecommerce.entity.UserAddress;
import com.nothing.ecommerce.entity.LoginCredentials;
import com.nothing.ecommerce.miscellaneous.*;

import com.nothing.ecommerce.exception.UserExistException;
import com.nothing.ecommerce.exception.UserNotFoundException;

@Service
public class UserService {

    // user contains { userId, userName, emial, number, active }
    private UserRepository userRepository;
    // userAddress contains { userId, streetAddress, city, state, postalCode,
    // country }
    private UserAddressRepository userAddressRepository;
    // LoginCredentials contains { userId, email, number, password, active }
    private LoginCredentialsRepository loginCredentialsRepository;

    // Injecting repositories ...
    public UserService(UserRepository userRepository, UserAddressRepository userAddressRepository,
            LoginCredentialsRepository loginCredentialsRepository) {
        this.userRepository = userRepository;
        this.userAddressRepository = userAddressRepository;
        this.loginCredentialsRepository = loginCredentialsRepository;
    }

    /**
     * Returns the user details for a given user ID.
     *
     * @param userId the ID of the user
     * @return the user details as a string
     */
    public String getUser(int userId) {
        UserDetails userDetails;
        UserAddress userAddress;
        String UserDetailsString = "";

        // fetch the user
        Optional<UserDetails> fetchedUser = userRepository.findById(userId);
        Optional<UserAddress> fetchedUserAddress = userAddressRepository.findById(userId);

        if (fetchedUser.isPresent()) {
            userDetails = fetchedUser.get();
            UserDetailsString = userDetails.toString();
            if (fetchedUserAddress.isPresent()) {
                userAddress = fetchedUserAddress.get();
                UserDetailsString += " " + userAddress.toString();
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
    public UserDetails saveUser(UserDetails userDetails) {
        int userId = userDetails.getUserId();
        if (isUserExist(userId)) {
            throw new UserExistException("user already exists {check your username}");
        }

        checkUserDetails(userDetails);

        return userRepository.save(userDetails);
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
        Optional<UserDetails> OpUserDetails = userRepository.findById(userId);
        if (OpUserDetails.isPresent()) {
            return true;
        }
        return false;
    }

    public boolean checkUserDetails(UserDetails userDetails) {
        String name = userDetails.getUserName();
        String email = userDetails.getEmail();
        String number = userDetails.getNumber();

        if (name == null || name == "") {
            System.out.println("throw new IllegalStateException invalid user name");
            throw new IllegalArgumentException("Invalid user name");
        }
        checkNumber(number);
        if (email == null || email == "") {
            userDetails.setEmail(null);
        } else {
            checkEmail(email);
        }
        return true;
    }

    public boolean checkNumber(String number) {
        System.out.println("checking mobile number ...");
        if (!Miscellaneous.verifyMobileNumber(number)) {
            System.out.println("throw new IllegalArgumentException for wrong mobile number");
            throw new IllegalArgumentException("Invalid phone number");
        }

        UserDetails userDetails = userRepository.findByNumber(number);
        if (userDetails != null) {
            System.out.println("user already exists {check your phone number}");
            throw new UserExistException("user Already exists");
        }

        return true;
    }

    public boolean checkEmail(String email) {
        System.out.println("checking email ...");
        if (!Miscellaneous.verifyEmail(email)) {
            System.out.println("throw new IllegalArgumentException for wrong email Address");
            throw new IllegalArgumentException("Invalid email Address");
        }

        UserDetails userDetails = userRepository.findByEmail(email);
        if (userDetails == null) {
            System.out.println("user already exists {check your email}");
            throw new UserExistException("user Already exists");
        }

        return true;
    }
}