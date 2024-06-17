package com.nothing.ecommerce.services;

public interface EmailService {

    public void sendEmail(String to, String subject, String content);
}
