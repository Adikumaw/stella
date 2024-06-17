package com.nothing.ecommerce.services;

import com.nothing.ecommerce.entity.User;
import com.nothing.ecommerce.model.UserModel;

public interface UserAdvanceService {

    // ----------------------------------------------------------------
    // RestApi methods for user
    // ----------------------------------------------------------------

    boolean registerUser(UserModel userModel);

    boolean verifyUser(String token);

    boolean verifyUserUpdate(String token);

    User updateUserName(int userId, String name);

    void updateUserNumber(int userId, String number);

    void updateUserEmail(int userId, String email);

    void updateUserPassword(int userId, String oldPassword, String newPassword);

    boolean deactivateUser(int userId, String password);

    boolean activateUser(int userId);

    void removeUser(int userId);
}
