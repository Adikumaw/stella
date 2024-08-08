package com.nothing.stella.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nothing.stella.entity.User;
import com.nothing.stella.exception.InvalidEmailException;
import com.nothing.stella.exception.InvalidEmailOrNumberException;
import com.nothing.stella.exception.UserNotFoundException;
import com.nothing.stella.miscellaneous.Miscellaneous;
import com.nothing.stella.model.UserViewModel;
import com.nothing.stella.repository.UserRepository;

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
            Optional<User> optionalUser = userRepository.findByEmail(reference);
            if (optionalUser.isPresent()) {
                return optionalUser.get();
            } else {
                throw new UserNotFoundException("Error: User not found by reference: " + reference);
            }
        }
        if (Miscellaneous.isValidNumber(reference)) {
            Optional<User> optionalUser = userRepository.findByNumber(reference);
            if (optionalUser.isPresent()) {
                return optionalUser.get();
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
        return userRepository.findUserIdByEmail(email).orElse(0);
    }

    @Override
    public User findById(int userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public int findUserIdByNumber(String number) {
        return userRepository.findUserIdByNumber(number).orElse(0);
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
        throw new InvalidEmailOrNumberException("Error: Invalid Email address or Number: " + reference);
    }

    @Override
    public User findByNumber(String number) {
        return userRepository.findByNumber(number).orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public UserViewModel getInfo(String reference) {
        User user = get(reference);

        return new UserViewModel(user);
    }

    @Override
    public String getUserName(int userId) {
        Optional<String> optionalName = userRepository.findNameByUserId(userId);

        return optionalName.orElse("unknown");
    }

}
