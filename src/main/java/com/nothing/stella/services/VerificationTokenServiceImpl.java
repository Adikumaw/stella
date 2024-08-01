package com.nothing.stella.services;

import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nothing.stella.entity.User;
import com.nothing.stella.entity.VerificationToken;
import com.nothing.stella.miscellaneous.EmailTemplate;
import com.nothing.stella.repository.VerificationTokenRepository;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private String verificationLink = "http://localhost:8080/users/verify-user?token=";
    private String applicationName = "Stella";
    private String emailSubject = "Verify Your Email Address for " + applicationName;
    private int EXPIRATION = 24;
    @Autowired
    private EmailService emailService;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(VerificationTokenServiceImpl.class);

    @Override
    public void sender(String reference) {
        User user = userService.get(reference);
        VerificationToken verificationToken = verificationTokenRepository.findByUserId(user.getUserId());

        String email = user.getEmail();
        String token = verificationToken.getToken();
        String verificationTemplate = EmailTemplate.EMAIL_VERIFICATION_TEMPLATE;
        String formatedMessage = String.format(verificationTemplate, user.getName(), applicationName,
                verificationLink + token, verificationLink + token, EXPIRATION, applicationName,
                applicationName);

        try {
            emailService.sendEmail(email, emailSubject, formatedMessage);
        } catch (Exception e) {
            logger.error("Error sending email: " + e.getMessage(), e);
        }
    }

    @Override
    public void sender(User user, VerificationToken verificationToken) {
        String email = user.getEmail();
        String token = verificationToken.getToken();
        String verificationTemplate = EmailTemplate.EMAIL_VERIFICATION_TEMPLATE;
        String formatedMessage = String.format(verificationTemplate, user.getName(), applicationName,
                verificationLink + token, verificationLink + token, EXPIRATION, applicationName,
                applicationName);

        try {
            emailService.sendEmail(email, emailSubject, formatedMessage);
        } catch (Exception e) {
            logger.error("Error sending email: " + e.getMessage(), e);
        }
    }

    @Override
    public VerificationToken findByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    @Override
    public VerificationToken save(VerificationToken token) {
        return verificationTokenRepository.save(token);
    }

    @Override
    public void delete(VerificationToken token) {
        verificationTokenRepository.delete(token);
    }

    @Override
    public boolean verify(String token) {
        // fetch token from Database
        VerificationToken verificationToken = findByToken(token);
        // check if token exist and not expired
        if (verificationToken != null && verificationToken.getExpiryDate().after(new Date())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean verify(VerificationToken token) {
        // check if token exist and not expired
        if (token != null && token.getExpiryDate().after(new Date())) {
            return true;
        }
        return false;
    }

    @Override
    public VerificationToken generate(int userId) {
        // Generate Verification Token
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken(token, userId);

        return save(verificationToken);
    }
}
