package com.telerikacademy.web.virtualwallet.services;

import com.mailjet.client.MailjetClient;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.transactional.*;
import com.mailjet.client.transactional.response.SendEmailsResponse;
import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.TransactionVerificationCodes;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.VerificationCodes;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;
import com.telerikacademy.web.virtualwallet.repositories.contracts.TransactionRepository;
import com.telerikacademy.web.virtualwallet.repositories.contracts.TransactionVerificationCodesRepository;
import com.telerikacademy.web.virtualwallet.repositories.contracts.VerificationCodesRepository;
import com.telerikacademy.web.virtualwallet.services.contracts.TransactionService;
import com.telerikacademy.web.virtualwallet.services.contracts.UserService;
import com.telerikacademy.web.virtualwallet.services.contracts.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class VerificationServiceImpl implements VerificationService {

    private final VerificationCodesRepository verificationCodesRepository;
    private final TransactionVerificationCodesRepository transactionVerificationCodesRepository;
    private final MailjetClient mailjetClient;
    private final UserService userService;

    private final TransactionRepository transactionRepository;

    @Autowired
    public VerificationServiceImpl(VerificationCodesRepository verificationCodesRepository, TransactionVerificationCodesRepository transactionVerificationCodesRepository, MailjetClient mailjetClient, UserService userService, TransactionRepository transactionRepository) {
        this.verificationCodesRepository = verificationCodesRepository;
        this.transactionVerificationCodesRepository = transactionVerificationCodesRepository;
        this.mailjetClient = mailjetClient;
        this.userService = userService;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void sendUserCode(User user) throws MailjetException {
        TransactionalEmail message = TransactionalEmail
                .builder()
                .to(new SendContact(user.getEmail(), user.getFirstName()))
                .from(new SendContact("sup3rrXDA1@gmail.com", "7Wallet developer team"))
                .htmlPart("<h1>Welcome to 7Wallet!</h1>"
                        + "<p>Please click on the link below to verify your account"
                        + String.format("<p>http://localhost:8080/auth/verify/%s</p>"
                        , generateUserVerificationCode(user)))
                .subject("Welcome to 7Wallet")
                .trackOpens(TrackOpens.ENABLED)
                .header("test-header-key", "test-value")
                .customID("custom-id-value")
                .build();

        SendEmailsRequest request = SendEmailsRequest
                .builder()
                .message(message)
                .build();

        SendEmailsResponse response = request.sendWith(mailjetClient);
    }

    @Override
    public void sendTransactionCode(User user, Transaction transaction, Wallet senderWallet, Wallet receiverWallet)
            throws MailjetException {
        TransactionalEmail message = TransactionalEmail
                .builder()
                .to(new SendContact(user.getEmail(), user.getFirstName()))
                .from(new SendContact("sup3rrXDA1@gmail.com", "7Wallet developer team"))
                .htmlPart(
                        String.format("<p>Please click on the link below to verify your transaction to %s"
                                , transaction.getReceiver().getUsername())
                                + String.format("<p>http://localhost:8080/wallet/transactions/verify/%s</p>"
                                , generateTransactionVerificationCode(transaction,senderWallet,receiverWallet)))
                .subject("Confirm Transaction")
                .trackOpens(TrackOpens.ENABLED)
                .build();

        SendEmailsRequest request = SendEmailsRequest
                .builder()
                .message(message)
                .build();

        SendEmailsResponse response = request.sendWith(mailjetClient);
    }

    @Override
    public TransactionVerificationCodes getByCode(String code) {
       return transactionVerificationCodesRepository.getByField("verificationCode",code);
    }

    @Override
    public void verifyUser(String verificationCode) {

        VerificationCodes verificationCodesEntity = verificationCodesRepository.getByField("verificationCode", verificationCode);

        if (verificationCodesEntity != null) {
            userService.makeRegular(verificationCodesEntity.getUser());
            verificationCodesRepository.delete(verificationCodesEntity.getVerificationCodeId());
        }
    }

    @Override
    public Transaction verifyTransaction(String verificationCode) {
        TransactionVerificationCodes transactionVerificationCodes = transactionVerificationCodesRepository.getByField("verificationCode", verificationCode);
        Transaction transaction = transactionVerificationCodes.getTransaction();
        if (verificationCode != null) {
            transaction.setConfirmed(true);
            transactionRepository.update(transaction);
            transactionVerificationCodesRepository.delete(transactionVerificationCodes.getTransactionVerificationCodeId());
        }
        return transaction;
    }

    private String generateUserVerificationCode(User user) {
        String verificationCode = generateCode();
        VerificationCodes verificationCodeEntity = new VerificationCodes();
        verificationCodeEntity.setUser(user);
        verificationCodeEntity.setVerificationCode(verificationCode);
        verificationCodesRepository.create(verificationCodeEntity);
        return verificationCode;

    }

    private String generateTransactionVerificationCode(Transaction transaction, Wallet senderWallet, Wallet receiverWallet) {
        String verificationCode = generateCode();
        TransactionVerificationCodes transactionVerificationCodes = new TransactionVerificationCodes();
        transactionVerificationCodes.setTransaction(transaction);
        transactionVerificationCodes.setVerificationCode(verificationCode);
        transactionVerificationCodes.setSenderWallet(senderWallet);
        transactionVerificationCodes.setReceiverWallet(receiverWallet);
        transactionVerificationCodesRepository.create(transactionVerificationCodes);
        return verificationCode;
    }


    private String generateCode() {
        String englishAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder verificationCode = new StringBuilder();
        Random random = new Random();

        int length = 7;
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(englishAlphabet.length());
            char randomChar = englishAlphabet.charAt(index);
            verificationCode.append(randomChar);
        }
        return verificationCode.toString();
    }
}
