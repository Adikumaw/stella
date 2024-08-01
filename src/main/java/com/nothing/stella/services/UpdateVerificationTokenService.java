package com.nothing.stella.services;

import com.nothing.stella.entity.UpdateVerificationToken;
import com.nothing.stella.entity.User;

public interface UpdateVerificationTokenService {
    UpdateVerificationToken findByToken(String token);

    UpdateVerificationToken findByData(String data);

    UpdateVerificationToken save(UpdateVerificationToken token);

    void delete(UpdateVerificationToken token);

    void sender(String reference, String verificationLink);

    void sender(User user, UpdateVerificationToken updateVerificationToken, String verificationLink);

    boolean verify(String token);

    boolean verify(UpdateVerificationToken token);

    UpdateVerificationToken generate(int userId, String data, String prefix);

    String getPrefix(String data);

    String setPrefix(String data, String prefix);

    String fetchData(String data);
}
