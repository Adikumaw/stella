package com.nothing.ecommerce.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.nothing.ecommerce.entity.User;
import com.nothing.ecommerce.entity.VerificationToken;
import com.nothing.ecommerce.entity.Address;
import com.nothing.ecommerce.entity.Roles;
import com.nothing.ecommerce.entity.UpdateVerificationToken;
import com.nothing.ecommerce.miscellaneous.*;
import com.nothing.ecommerce.model.UserModel;
import com.nothing.ecommerce.repository.RolesRepository;
import com.nothing.ecommerce.repository.UserRepository;
import com.nothing.ecommerce.exception.IllegalRoleException;
import com.nothing.ecommerce.exception.InvalidEmailException;
import com.nothing.ecommerce.exception.InvalidNumberException;
import com.nothing.ecommerce.exception.InvalidPasswordException;
import com.nothing.ecommerce.exception.UserException;
import com.nothing.ecommerce.exception.UserExistException;
import com.nothing.ecommerce.exception.UserNotFoundException;
import com.nothing.ecommerce.exception.WeakPasswordException;
import com.nothing.ecommerce.exception.WrongPasswordException;

@Service
public class UserAdvanceServiceImpl implements UserAdvanceService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private AddressService addressService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private VerificationTokenService verificationTokenService;
    @Autowired
    private UpdateVerificationTokenService updateVerificationTokenService;

    // ----------------------------------------------------------------
    // service methods for user
    // ----------------------------------------------------------------

    @Override
    public boolean register(UserModel userModel) {
        // vrify user details
        verifyUserDetails(userModel);

        // encrypt password
        String encryptedPassword;
        encryptedPassword = passwordEncoder.encode(userModel.getPassword());
        userModel.setPassword(encryptedPassword);

        // create new user from user model
        User user = new User(userModel);
        user.setActive(0);
        user = userRepository.save(user);

        // create new userRole from user model > role
        Roles roles = new Roles();
        roles.setUserId(user.getUserId());
        if (userModel.getRole().equalsIgnoreCase("BUYER")) {
            roles.setRole("ROLE_BUYER");
        } else if (userModel.getRole().equalsIgnoreCase("SELLER")) {
            roles.setRole("ROLE_SELLER");
        }
        rolesRepository.save(roles);

        // Generate Verification Token
        VerificationToken verificationToken = verificationTokenService.generate(user.getUserId());
        // send verification email
        verificationTokenService.sender(user, verificationToken);

        return (user != null) ? true : false;
    }

    @Override
    public boolean verify(String token) {
        // fetch token from Database
        VerificationToken verificationToken = verificationTokenService.findByToken(token);
        // check if token exist and not expired
        if (verificationTokenService.verify(verificationToken)) {
            // fetch and set user active
            int userId = verificationToken.getUserId();
            User user = userRepository.findById(userId).get();
            user.setActive(1);
            userRepository.save(user);
            // Delete verification token
            verificationTokenService.delete(verificationToken);
            return true;
        }
        return false;
    }

    @Override
    public boolean verifyUpdate(String token) {
        // fetch token from Database
        UpdateVerificationToken updateVerificationToken = updateVerificationTokenService.findByToken(token);
        // check if token exist and not expired
        if (updateVerificationTokenService.verify(updateVerificationToken)) {
            // fetch and set updated value to user
            int userId = updateVerificationToken.getUserId();
            String data = updateVerificationToken.getData();
            User user = userRepository.findById(userId).get();
            // check if data is number or email
            if (Miscellaneous.isValidEmail(data)) {
                user.setEmail(data);
            }
            if (Miscellaneous.isValidNumber(data)) {
                user.setNumber(data);
            }
            // Save updated value to user
            userRepository.save(user);

            // Delete verification token
            updateVerificationTokenService.delete(updateVerificationToken);
            return true;
        }
        try {
            updateVerificationTokenService.delete(updateVerificationToken);
        } catch (InvalidDataAccessApiUsageException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public User updateName(int userId, String name) {
        User fetchedUser = userService.get(userId);
        if (fetchedUser != null) {
            fetchedUser.setName(name);
            return userRepository.save(fetchedUser);
        }
        return null;
    }

    @Override
    public User updateName(String reference, String name) {
        int userId = userService.findUserIdByReference(reference);

        User fetchedUser = userService.get(userId);
        if (fetchedUser != null) {
            fetchedUser.setName(name);
            return userRepository.save(fetchedUser);
        }
        return null;
    }

    @Override
    public void updateNumber(int userId, String number) {
        // check if number is already userd or not
        try {
            userService.get(number); // it will throw exception if user not found ...
            throw new UserExistException("number is already used");
        } catch (UserNotFoundException exc) {
            User user = userService.get(userId);
            boolean verifyNumber = Miscellaneous.isValidNumber(number);
            if (user != null && verifyNumber) {
                // Generate Verification Token
                UpdateVerificationToken updateVerificationToken = updateVerificationTokenService.generate(
                        userId,
                        number);

                // send verification email
                updateVerificationTokenService.sender(user, updateVerificationToken);
            }
        }
    }

    @Override
    public void updateNumber(String reference, String number) {
        int userId = userService.findUserIdByReference(reference);
        // check if number is already userd or not
        try {
            userService.get(number); // it will throw exception if user not found ...
            throw new UserExistException("number is already used");
        } catch (UserNotFoundException exc) {
            User user = userService.get(userId);
            boolean verifyNumber = Miscellaneous.isValidNumber(number);
            if (user != null && verifyNumber) {
                // Generate Verification Token
                UpdateVerificationToken updateVerificationToken = updateVerificationTokenService.generate(
                        userId,
                        number);
                // send verification email
                updateVerificationTokenService.sender(user, updateVerificationToken);
            }
        }
    }

    @Override
    public void updateEmail(int userId, String email) {
        // check if email is already userd or not
        try {
            userService.get(email); // it will throw exception if user not found ...
        } catch (UserNotFoundException exc) {
            User user = userService.get(userId);
            boolean verifyEmail = Miscellaneous.isValidEmail(email);
            if (user != null && verifyEmail) {
                // Generate Verification Token
                UpdateVerificationToken updateVerificationToken = updateVerificationTokenService.generate(
                        userId,
                        email);

                // send verification email
                updateVerificationTokenService.sender(user, updateVerificationToken);
            }
        }
    }

    @Override
    public void updateEmail(String reference, String email) {
        int userId = userService.findUserIdByReference(reference);
        // check if email is already userd or not
        try {
            userService.get(email); // it will throw exception if user not found ...
        } catch (UserNotFoundException exc) {
            User user = userService.get(userId);
            boolean verifyEmail = Miscellaneous.isValidEmail(email);
            if (user != null && verifyEmail) {
                // Generate Verification Token
                UpdateVerificationToken updateVerificationToken = updateVerificationTokenService.generate(
                        userId,
                        email);

                // send verification email
                updateVerificationTokenService.sender(user, updateVerificationToken);
            }
        }
    }

    @Override
    public void updatePassword(int userId, String oldPassword, String newPassword) {
        // fetch user information
        User user = userService.get(userId);
        if (user != null) {
            // verify olf password
            boolean verifyOldPassword = passwordEncoder.matches(oldPassword, user.getPassword());
            if (verifyOldPassword) {
                // verify new password
                if (Miscellaneous.isValidPassword(newPassword)) {
                    // Hash new password ...
                    String encryptedPassword = passwordEncoder.encode(newPassword);
                    user.setPassword(encryptedPassword);
                    // save new password to user
                    userRepository.save(user);
                } else {
                    throw new WeakPasswordException("password is not strong");
                }
            } else {
                throw new WrongPasswordException("Wrong Password");
            }
        }
    }

    @Override
    public void updatePassword(String reference, String oldPassword, String newPassword) {
        int userId = userService.findUserIdByReference(reference);
        // fetch user information
        User user = userService.get(userId);
        if (user != null) {
            // verify olf password
            boolean verifyOldPassword = passwordEncoder.matches(oldPassword, user.getPassword());
            if (verifyOldPassword) {
                // verify new password
                if (Miscellaneous.isValidPassword(newPassword)) {
                    // Hash new password ...
                    String encryptedPassword = passwordEncoder.encode(newPassword);
                    user.setPassword(encryptedPassword);
                    // save new password to user
                    userRepository.save(user);
                } else {
                    throw new WeakPasswordException("password is not strong");
                }
            } else {
                throw new WrongPasswordException("Wrong Password");
            }
        }
    }

    @Override
    public boolean deactivate(int userId, String password) {
        User user = userService.get(userId);
        if (user != null) {
            boolean verifyPassword = passwordEncoder.matches(password, user.getPassword());
            if (verifyPassword) {
                if (user.isActive()) {
                    user.setActive(0);
                    return userRepository.save(user) != null;
                } else {
                    throw new UserException("User is already deactivated");
                }
            } else {
                throw new WrongPasswordException("Wrong Password");
            }
        }
        return false;
    }

    @Override
    public boolean deactivate(String reference, String password) {
        int userId = userService.findUserIdByReference(reference);

        User user = userService.get(userId);
        if (user != null) {
            boolean verifyPassword = passwordEncoder.matches(password, user.getPassword());
            if (verifyPassword) {
                if (user.isActive()) {
                    user.setActive(0);
                    return userRepository.save(user) != null;
                } else {
                    throw new UserException("User is already deactivated");
                }
            } else {
                throw new WrongPasswordException("Wrong Password");
            }
        }
        return false;
    }

    @Override
    public void delete(int userId) {
        // NOTE: remove all the data from tables related to this user before removing
        // the user
        // removing user addresses
        User user = userService.get(userId);
        if (user != null) {
            List<Address> addresses = addressService.getAddresses(userId);
            for (Address address : addresses) {
                addressService.delete(address);
            }

            userRepository.delete(user);
        }
    }

    @Override
    public void delete(String reference) {
        int userId = userService.findUserIdByReference(reference);
        // NOTE: remove all the data from tables related to this user before removing
        // the user
        // removing user addresses
        User user = userService.get(userId);
        if (user != null) {
            List<Address> addresses = addressService.getAddresses(userId);
            for (Address address : addresses) {
                addressService.delete(address);
            }
            userRepository.delete(user);
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
        List<Address> fetchedUserAddress = addressService.getAddresses(userId);

        if (fetchedUser.isPresent()) {
            user = fetchedUser.get();
            UserDetailsString = user.toString();
            for (Address address : fetchedUserAddress) {
                UserDetailsString += " \n" + address.toString();
            }
            return UserDetailsString;
        }
        return "User Not Found";
    }

    public boolean isUserExist(int userId) {
        Optional<User> OpUserDetails = userRepository.findById(userId);
        if (OpUserDetails.isPresent()) {
            return true;
        }
        return false;
    }

    public void verifyUserDetails(UserModel userModel) {
        String name = userModel.getName();
        String email = userModel.getEmail();
        String number = userModel.getNumber();
        String password = userModel.getPassword();
        String role = userModel.getRole();

        // checking user name
        if (name == null || name == "") {
            throw new UserException("Invalid user name");
        }
        // checking user email
        if (email == null || email == "") {
            throw new InvalidEmailException("Empty email address Error");
        } else {
            checkEmail(email);
        }
        // checking user number
        checkNumber(number);
        // checking user Password
        if (password == null || password == "") {
            throw new InvalidPasswordException("empty password field");
        } else if (!Miscellaneous.isValidPassword(password)) {
            throw new WeakPasswordException("password is not strong !!!");
        }
        // checking user Role
        if (!role.equalsIgnoreCase("BUYER") && !role.equalsIgnoreCase("SELLER")) {
            throw new IllegalRoleException("You cannot register a user with this role");
        }
    }

    public void checkNumber(String number) {
        System.out.println("checking mobile number ...");
        if (!Miscellaneous.isValidNumber(number)) {
            throw new InvalidNumberException("Invalid phone number");
        }

        User userDetails = userRepository.findByNumber(number);
        if (userDetails != null) {
            throw new UserExistException("phone number already exists, try login !!!");
        }
    }

    public void checkEmail(String email) {
        System.out.println("checking email ...");
        if (!Miscellaneous.isValidEmail(email)) {
            throw new InvalidEmailException("Invalid email Address");
        }

        User userDetails = userRepository.findByEmail(email);
        if (userDetails != null) {
            throw new UserExistException("email already exists, try login !!!");
        }
    }

}