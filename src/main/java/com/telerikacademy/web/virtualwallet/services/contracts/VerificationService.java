package com.telerikacademy.web.virtualwallet.services.contracts;

import com.mailjet.client.errors.MailjetException;
import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.TransactionVerificationCodes;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;

public interface VerificationService {

    void sendUserCode(User user) throws MailjetException;

    void sendTransactionCode(User user, Transaction transaction, Wallet senderWallet, Wallet receiverWallet)throws MailjetException;

    TransactionVerificationCodes getByCode(String code);

    void verifyUser(String verificationCode);

    Transaction verifyTransaction(String verificationCode);
}
