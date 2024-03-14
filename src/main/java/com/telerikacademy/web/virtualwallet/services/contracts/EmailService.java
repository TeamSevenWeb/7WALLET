package com.telerikacademy.web.virtualwallet.services.contracts;

import com.mailjet.client.errors.MailjetException;

public interface EmailService {

    void send() throws MailjetException;
}
