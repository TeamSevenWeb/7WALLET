package com.telerikacademy.web.virtualwallet.config;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:application.properties")
public class CloudinaryConfig {

    private final String cloudName,apiKey,apiSecret;

    @Autowired
    public CloudinaryConfig(Environment env) {
        this.cloudName = env.getProperty("cloudinary.cloud.name");
        this.apiKey = env.getProperty("cloudinary.api.key");
        this.apiSecret = env.getProperty("cloudinary.api.secret");
    }

    @Bean
    public Cloudinary cloudinary() {
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));
        return cloudinary;
    }}