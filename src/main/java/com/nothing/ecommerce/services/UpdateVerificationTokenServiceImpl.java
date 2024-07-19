package com.nothing.ecommerce.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nothing.ecommerce.entity.UpdateVerificationToken;
import com.nothing.ecommerce.entity.User;
import com.nothing.ecommerce.miscellaneous.EmailTemplate;
import com.nothing.ecommerce.miscellaneous.Miscellaneous;
import com.nothing.ecommerce.repository.UpdateVerificationTokenRepository;

@Service
public class UpdateVerificationTokenServiceImpl implements UpdateVerificationTokenService {

    // TODO change link according to need
    private String verificationLink = "http://localhost:8080/users/verify-update?token=";
    private String applicationName = "Ecommerce_web";
    private String emailSubject = "Verify Your updated Email Address for " + applicationName;
    private int EXPIRATION = 1;

    @Autowired
    private EmailService emailService;
    @Autowired
    private UpdateVerificationTokenRepository updateVerificationTokenRepository;
    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UpdateVerificationTokenServiceImpl.class);

    @Override
    public void sender(String reference) {
        User user = userService.get(reference);
        UpdateVerificationToken updateVerificationToken = updateVerificationTokenRepository
                .findByUserId(user.getUserId());

        String email = user.getEmail();
        String token = updateVerificationToken.getToken();

        String verificationTemplate = EmailTemplate.EMAIL_UPDATE_VERIFICATION_TEMPLATE;

        String formatedMessage = String.format(verificationTemplate,
                user.getName(), applicationName,
                fetchData(updateVerificationToken.getData()),
                verificationLink + token, verificationLink + token, EXPIRATION,
                applicationName,
                applicationName);

        try {

            emailService.sendEmail(email, emailSubject, formatedMessage);

        } catch (Exception e) {

            logger.error("Error sending email: " + e.getMessage(), e);

        }
    }

    @Override
    public void sender(User user, UpdateVerificationToken updateVerificationToken) {
        String data = updateVerificationToken.getData();
        data = fetchData(data);
        String email = null;
        if (Miscellaneous.isValidEmail(data)) {
            email = data;
        } else {
            email = user.getEmail();
        }

        String token = updateVerificationToken.getToken();
        String verificationTemplate = EmailTemplate.EMAIL_UPDATE_VERIFICATION_TEMPLATE;
        String formatedMessage;
        formatedMessage = String.format(verificationTemplate,
                user.getName(), applicationName,
                fetchData(updateVerificationToken.getData()),
                verificationLink + token, verificationLink + token, EXPIRATION,
                applicationName,
                applicationName);

        try {
            emailService.sendEmail(email, emailSubject, formatedMessage);
        } catch (Exception e) {
            logger.error("Error sending email: " + e.getMessage(), e);
        }
    }

    @Override
    public UpdateVerificationToken findByToken(String token) {
        return updateVerificationTokenRepository.findByToken(token);
    }

    @Override
    public UpdateVerificationToken findByData(String data) {
        return updateVerificationTokenRepository.findByData(data);
    }

    @Override
    public UpdateVerificationToken save(UpdateVerificationToken token) {
        return updateVerificationTokenRepository.save(token);
    }

    @Override
    public void delete(UpdateVerificationToken token) {
        updateVerificationTokenRepository.delete(token);
    }

    @Override
    public boolean verify(String token) {
        // fetch token from Database
        UpdateVerificationToken updateVerificationToken = findByToken(token);
        // check if token exist and not expired
        if (updateVerificationToken != null && updateVerificationToken.getExpiryDate().isAfter(LocalDateTime.now())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean verify(UpdateVerificationToken token) {
        // check if token exist and not expired
        if (token != null && token.getExpiryDate().isAfter(LocalDateTime.now())) {
            return true;
        }
        return false;
    }

    @Override
    public UpdateVerificationToken generate(int userId, String data, String prefix) {
        data = setPrefix(data, prefix);
        // check if data is not used before
        UpdateVerificationToken updateVerificationToken = findByData(data);
        if (updateVerificationToken != null) {
            delete(updateVerificationToken);
        }

        // Generate Verification Token
        String token = UUID.randomUUID().toString();

        updateVerificationToken = new UpdateVerificationToken(data, token, userId);

        return save(updateVerificationToken);
    }

    @Override
    public String getPrefix(String data) {
        if (data == null || data.isEmpty()) {
            return ""; // Return empty string for null or empty input
        }
        int indexOfColon = data.indexOf(":");
        return indexOfColon > 0 ? data.substring(0, indexOfColon) : data;
    }

    @Override
    public String setPrefix(String data, String prefix) {
        return prefix + ":" + data;
    }

    @Override
    public String fetchData(String data) {
        if (data == null || data.isEmpty()) {
            return ""; // Return empty string for null or empty input
        }
        int indexOfColon = data.indexOf(":");
        return indexOfColon > 0 ? data.substring(indexOfColon + 1) : data;

    }
}
