package com.nothing.ecommerce.services;

import com.nothing.ecommerce.entity.User;
import com.nothing.ecommerce.model.UserInfoModel;

public interface UserService {
    // ----------------------------------------------------------------
    // service methods for user
    // ----------------------------------------------------------------

    int findUserIdByEmail(String email);

    int findUserIdByNumber(String number);

    int findUserIdByReference(String reference);

    User findByNumber(String number);

    User findByEmail(String email);

    User get(int userId);

    User get(String reference);

    User save(User user);

    UserInfoModel getInfo(String reference);

    UserInfoModel convertoInfoModel(User user);
}
