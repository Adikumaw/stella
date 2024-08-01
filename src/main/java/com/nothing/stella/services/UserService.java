package com.nothing.stella.services;

import com.nothing.stella.entity.User;
import com.nothing.stella.model.UserViewModel;

public interface UserService {
    // ----------------------------------------------------------------
    // service methods for user
    // ----------------------------------------------------------------

    int findUserIdByEmail(String email);

    int findUserIdByNumber(String number);

    int findUserIdByReference(String reference);

    User findByNumber(String number);

    User findByEmail(String email);

    User findById(int userId);

    User get(int userId);

    User get(String reference);

    User save(User user);

    UserViewModel getInfo(String reference);
}
