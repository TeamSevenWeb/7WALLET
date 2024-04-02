package com.telerikacademy.web.virtualwallet.config;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:application.properties")
public class MailJetConfig {

    private final String apiKey,apiSecret;

    public MailJetConfig(Environment env) {
        this.apiKey = env.getProperty("mailjet.api.key");
        this.apiSecret = env.getProperty("mailjet.api.secret");
    }

    @Bean
    public MailjetClient mailjetClient(){
        ClientOptions options = ClientOptions.builder()
                .apiKey(apiKey)
                .apiSecretKey(apiSecret)
                .build();

        return new MailjetClient(options);
    }

}
