package com.nothing.ecommerce.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nothing.ecommerce.entity.User;
import com.nothing.ecommerce.exception.InvalidEmailException;
import com.nothing.ecommerce.exception.UserNotFoundException;
import com.nothing.ecommerce.miscellaneous.Miscellaneous;
import com.nothing.ecommerce.model.UserViewModel;
import com.nothing.ecommerce.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User get(int userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    @Override
    public User get(String reference) {
        if (Miscellaneous.isValidEmail(reference)) {
            User userDetails = userRepository.findByEmail(reference);
            if (userDetails != null) {
                return userDetails;
            } else {
                throw new UserNotFoundException("Error: User not found by reference: " + reference);
            }
        }
        if (Miscellaneous.isValidNumber(reference)) {
            User userDetails = userRepository.findByNumber(reference);
            if (userDetails != null) {
                return userDetails;
            } else {
                throw new UserNotFoundException("Error: User not found by reference: " + reference);
            }
        }
        throw new InvalidEmailException("Error: Invalid Email address or Number: " + reference);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public int findUserIdByEmail(String email) {
        return userRepository.findUserIdByEmail(email);
    }

    @Override
    public User findById(int userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public int findUserIdByNumber(String number) {
        return userRepository.findUserIdByNumber(number);
    }

    @Override
    public int findUserIdByReference(String reference) {
        if (Miscellaneous.isValidEmail(reference)) {
            int userId = findUserIdByEmail(reference);
            if (userId != 0) {
                return userId;
            } else {
                throw new UserNotFoundException("Error: User not found");
            }
        }
        if (Miscellaneous.isValidNumber(reference)) {
            int userId = findUserIdByNumber(reference);
            if (userId != 0) {
                return userId;
            } else {
                throw new UserNotFoundException("Error: User not found");
            }
        }
        throw new IllegalArgumentException("Error: Invalid Email address or Number: " + reference);
    }

    @Override
    public User findByNumber(String number) {
        return userRepository.findByNumber(number);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserViewModel getInfo(String reference) {
        User user = get(reference);

        return converToViewModel(user);
    }

    @Override
    public UserViewModel converToViewModel(User user) {
        return new UserViewModel(user.getName(), user.getEmail(), user.getNumber(),
                user.getActive());
    }
}
