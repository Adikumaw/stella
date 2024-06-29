package com.nothing.ecommerce.services;

import com.nothing.ecommerce.model.UserInputModel;
import com.nothing.ecommerce.model.UserViewModel;

public interface UserAdvanceService {

    // ----------------------------------------------------------------
    // RestApi methods for user
    // ----------------------------------------------------------------

    boolean register(UserInputModel userModel);

    boolean verify(String token);

    boolean verifyUpdate(String token);

    UserViewModel updateName(int userId, String name);

    UserViewModel updateName(String reference, String name);

    void updateNumber(int userId, String number);

    void updateNumber(String reference, String number);

    void updateEmail(int userId, String email);

    void updateEmail(String reference, String email);

    void updatePassword(int userId, String oldPassword, String newPassword);

    void updatePassword(String reference, String oldPassword, String newPassword);

    boolean deactivate(int userId, String password);

    boolean deactivate(String reference, String password);

    void delete(int userId);

    void delete(String reference);
}
