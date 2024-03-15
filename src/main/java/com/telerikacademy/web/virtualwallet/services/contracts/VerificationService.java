package com.telerikacademy.web.virtualwallet.services.contracts;

import com.mailjet.client.errors.MailjetException;
import com.telerikacademy.web.virtualwallet.models.User;

public interface VerificationService {

    void send(User user) throws MailjetException;

    void verifyUser(String verificationCode);
}
