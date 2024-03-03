package com.telerikacademy.web.virtualwallet.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telerikacademy.web.virtualwallet.exceptions.FundsSupplyException;
import com.telerikacademy.web.virtualwallet.exceptions.TransferFailedException;
import com.telerikacademy.web.virtualwallet.models.Currency;
import com.telerikacademy.web.virtualwallet.models.Transfer;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;
import com.telerikacademy.web.virtualwallet.repositories.contracts.WalletRepository;
import com.telerikacademy.web.virtualwallet.services.contracts.CurrencyService;
import com.telerikacademy.web.virtualwallet.services.contracts.WalletService;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    private final CurrencyService currencyService;

    private final  String apiUrl = "https://65df74a2ff5e305f32a25197.mockapi.io/api/card/withdraw";

    public WalletServiceImpl(WalletRepository walletRepository, CurrencyService currencyService) {
        this.walletRepository = walletRepository;
        this.currencyService = currencyService;
    }

    @Override
    public Wallet get(int id) {
        return walletRepository.getById(id);
    }

    @Override
    public Wallet getByUser(User user) {
        return walletRepository.getByField("id",user.getId());
    }


    @Override
    public List<Wallet> getAll() {
        return walletRepository.getAll();
    }

    @Override
    public void create(Wallet wallet) {
        walletRepository.create(wallet);
    }

    @Override
    public void update(Wallet wallet) {
        walletRepository.update(wallet);
    }

    @Override
    public void delete(int id) {
        walletRepository.delete(id);
    }

    @Override
    public void addFunds(int walletId, double funds) {
        Wallet wallet = get(walletId);
        wallet.setHoldings(wallet.getHoldings() + funds);
        update(wallet);
    }

    @Override
    public void subtractFunds(int walletId, double funds) {
        Wallet wallet = get(walletId);
        if (wallet.getHoldings() < funds){
            throw new FundsSupplyException();
        }
        wallet.setHoldings(wallet.getHoldings() - funds);
        update(wallet);
    }

    @Override
    public void changeCurrency(int walletId, Currency currency) {
        Wallet wallet = get(walletId);
        wallet.setCurrency(currency);
        update(wallet);
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

        if(responseMap.get("processed").equals(false)){
           throw new TransferFailedException();
       }

        if(transfer.getDirection()==1){
            addFunds(transfer.getWallet().getId(),transfer.getAmount());
        }
        else {
            subtractFunds(transfer.getWallet().getId(),transfer.getAmount());
        }
        walletRepository.transfer(transfer);
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

    public Wallet createDefaultWallet(User user){
        Wallet wallet = new Wallet();
        wallet.setHolder(user);
        wallet.setHoldings(0.0);
        wallet.setCurrency(currencyService.getById(1));
        return wallet;
    }

    }
