package com.telerikacademy.web.virtualwallet.services;

import com.mailjet.client.MailjetClient;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.transactional.*;
import com.mailjet.client.transactional.response.SendEmailsResponse;
import com.telerikacademy.web.virtualwallet.services.contracts.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final MailjetClient mailjetClient;

    @Autowired
    public EmailServiceImpl(MailjetClient mailjetClient) {
        this.mailjetClient = mailjetClient;
    }

    @Override
    public void send() throws MailjetException {
        TransactionalEmail message1 = TransactionalEmail
                .builder()
                .to(new SendContact("sup3rrXDA1@gmail.com", "stanislav"))
                .from(new SendContact("sup3rrXDA1@gmail.com", "Mailjet integration test"))
                .htmlPart("<h1>This is the HTML content of the mail</h1>")
                .subject("This is the subject")
                .trackOpens(TrackOpens.ENABLED)
                .header("test-header-key", "test-value")
                .customID("custom-id-value")
                .build();

        SendEmailsRequest request = SendEmailsRequest
                .builder()
                .message(message1)
                .build();

        // act
        SendEmailsResponse response = request.sendWith(mailjetClient);    }
}
