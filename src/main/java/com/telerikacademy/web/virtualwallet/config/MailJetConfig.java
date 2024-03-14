package com.telerikacademy.web.virtualwallet.config;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailJetConfig {

    @Bean
    public MailjetClient mailjetClient(){
        ClientOptions options = ClientOptions.builder()
                .apiKey("1191f5c10a573c9273c7ffe09dfd932c")
                .apiSecretKey("bdaed0a9d5f2d4892d252215afe209d5")
                .build();

        return new MailjetClient(options);
    }

}
