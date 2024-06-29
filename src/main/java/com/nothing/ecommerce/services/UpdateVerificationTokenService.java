package com.nothing.ecommerce.services;

import com.nothing.ecommerce.entity.UpdateVerificationToken;
import com.nothing.ecommerce.entity.User;

public interface UpdateVerificationTokenService {
    UpdateVerificationToken findByToken(String token);

    UpdateVerificationToken findByData(String data);

    UpdateVerificationToken save(UpdateVerificationToken token);

    void delete(UpdateVerificationToken token);

    void sender(String reference);

    void sender(User user, UpdateVerificationToken updateVerificationToken);

    boolean verify(String token);

    boolean verify(UpdateVerificationToken token);

    UpdateVerificationToken generate(int userId, String data, String prefix);

    String getPrefix(String data);

    String setPrefix(String data, String prefix);

    String fetchData(String data);
}
