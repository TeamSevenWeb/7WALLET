package com.telerikacademy.web.virtualwallet.services.contracts;

import com.mailjet.client.errors.MailjetException;
import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.User;

public interface VerificationService {

    void sendUserCode(User user) throws MailjetException;

    void sendTransactionCode(User user, Transaction transaction)throws MailjetException;

    void verifyUser(String verificationCode);

    Transaction verifyTransaction(String verificationCode);
}
