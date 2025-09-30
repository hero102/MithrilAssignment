package com.aurionpro.bankapp.service;

import jakarta.mail.MessagingException;

public interface EmailService {

    void sendSimpleMessage(String to, String subject, String text);

    void sendEmailWithAttachment(String to, String subject, String text, String filename, byte[] attachment) throws MessagingException;
}
