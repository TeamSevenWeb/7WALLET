package com.telerikacademy.web.virtualwallet.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telerikacademy.web.virtualwallet.models.Transfer;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.repositories.contracts.WalletRepository;
import com.telerikacademy.web.virtualwallet.services.contracts.WalletService;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    private final  String apiUrl = "https://65df74a2ff5e305f32a25197.mockapi.io/api/card/withdraw";

    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public void transfer(Transfer transfer) {

        // Create a RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                null,  // or requestEntity if you have headers and payload
                String.class
        );

        // Access the response body
        String responseBody = responseEntity.getBody();

        // Convert the JSON string to a Map
        Map<String, Object> responseMap = convertJsonToMap(responseBody);

       if(responseMap.get("processed").equals("true")){
           walletRepository.transfer(transfer);
       }
       else {
           System.out.println("Processing failed");
           System.out.println(responseMap.get("processed"));
       }
    }

    private static Map<String, Object> convertJsonToMap(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Convert the JSON string to a Map
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            e.getMessage();
            return null;
        }
    }
    }
