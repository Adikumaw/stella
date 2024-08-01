package com.nothing.stella.services;

public interface EmailService {

    public void sendEmail(String to, String subject, String content);
}
