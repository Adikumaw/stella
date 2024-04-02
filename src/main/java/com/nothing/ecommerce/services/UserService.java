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

import exception.UserExistException;
import exception.UserNotFoundException;

@Service
public class UserService {

    private UserRepository userRepository;
    private UserAddressRepository userAddressRepository;
    private LoginCredentialsRepository loginCredentialsRepository;

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
        String name = userDetails.getUserName();
        String email = userDetails.getEmail();
        String number = userDetails.getNumber();

        checkUserDetails(userDetails);

        if (isUserExist(userId)) {
            throw new UserExistException("user already exists");
        }

        return userRepository.save(userDetails);
    }

    public boolean checkUserDetails(UserDetails userDetails) {
        String name = userDetails.getUserName();
        String email = userDetails.getEmail();
        String number = userDetails.getNumber();

        if (name == null || name == "") {
            throw new IllegalArgumentException("Invalid user name");
        }

        checkNumber(number);

        if (email == null || email == "") {
            userDetails.setEmail(null);
        }

        return true;
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

    public boolean isUserExist(int userId) {
        Optional<UserDetails> OpUserDetails = userRepository.findById(userId);

        if (OpUserDetails.isPresent()) {
            return true;
        }
        return false;
    }

    public boolean checkNumber(String number) {
        if (number == null || number == "" || Miscellaneous.containsNonNumeric(number)) {
            throw new IllegalArgumentException("Invalid phone number");
        }

        UserDetails userDetails = userRepository.findByNumber(number);
        if (userDetails == null) {
            throw new UserExistException("user Already exists");
        }

        return true;
    }

}