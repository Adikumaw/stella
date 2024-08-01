package com.nothing.stella.services;

import com.nothing.stella.entity.User;
import com.nothing.stella.entity.VerificationToken;

public interface VerificationTokenService {
    VerificationToken findByToken(String token);

    VerificationToken save(VerificationToken token);

    void delete(VerificationToken token);

    void sender(String reference);

    void sender(User user, VerificationToken verificationToken);

    boolean verify(String token);

    boolean verify(VerificationToken token);

    VerificationToken generate(int userId);
}
