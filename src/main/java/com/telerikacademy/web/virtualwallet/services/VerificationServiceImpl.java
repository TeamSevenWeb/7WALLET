package com.telerikacademy.web.virtualwallet.services;

import com.mailjet.client.MailjetClient;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.transactional.*;
import com.mailjet.client.transactional.response.SendEmailsResponse;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.VerificationCodes;
import com.telerikacademy.web.virtualwallet.repositories.contracts.VerificationCodesRepository;
import com.telerikacademy.web.virtualwallet.services.contracts.UserService;
import com.telerikacademy.web.virtualwallet.services.contracts.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class VerificationServiceImpl implements VerificationService {

    private final VerificationCodesRepository verificationCodesRepository;
    private final MailjetClient mailjetClient;
    private final UserService userService;

    @Autowired
    public VerificationServiceImpl(VerificationCodesRepository verificationCodesRepository, MailjetClient mailjetClient, UserService userService) {
        this.verificationCodesRepository = verificationCodesRepository;
        this.mailjetClient = mailjetClient;
        this.userService = userService;
    }

    @Override
    public void send(User user) throws MailjetException {
        TransactionalEmail message1 = TransactionalEmail
                .builder()
                .to(new SendContact(user.getEmail(), user.getFirstName()))
                .from(new SendContact("sup3rrXDA1@gmail.com", "7Wallet developer team"))
                .htmlPart("<h1>Welcome to 7Wallet!</h1>"
                        +"<p>Please click on the link below to verify your account"
                        +String.format("<p>http://localhost:8080/auth/verify/%s</p>"
                        ,generateVerificationCode(user)))
                .subject("Welcome to 7Wallet")
                .trackOpens(TrackOpens.ENABLED)
                .header("test-header-key", "test-value")
                .customID("custom-id-value")
                .build();

        SendEmailsRequest request = SendEmailsRequest
                .builder()
                .message(message1)
                .build();

        SendEmailsResponse response = request.sendWith(mailjetClient);    }

    @Override
    public void verifyUser(String verificationCode) {

        VerificationCodes verificationCodesEntity = verificationCodesRepository.getByField("verificationCode",verificationCode);

        if (verificationCodesEntity!=null){
            userService.makeRegular(verificationCodesEntity.getUser());
            verificationCodesRepository.delete(verificationCodesEntity.getVerificationCodeId());
        }
    }

    private String generateVerificationCode(User user){
        String englishAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder verificationCode = new StringBuilder();
        Random random = new Random();

        int length = 7;
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(englishAlphabet.length());
            char randomChar = englishAlphabet.charAt(index);
            verificationCode.append(randomChar);
        }
        VerificationCodes verificationCodeEntity = new VerificationCodes();
        verificationCodeEntity.setUser(user);
        verificationCodeEntity.setVerificationCode(verificationCode.toString());
        verificationCodesRepository.create(verificationCodeEntity);
        return verificationCode.toString();

    }


}
