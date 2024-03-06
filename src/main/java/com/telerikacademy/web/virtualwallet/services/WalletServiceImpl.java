package com.telerikacademy.web.virtualwallet.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telerikacademy.web.virtualwallet.exceptions.AuthenticationException;
import com.telerikacademy.web.virtualwallet.exceptions.AuthorizationException;
import com.telerikacademy.web.virtualwallet.exceptions.FundsSupplyException;
import com.telerikacademy.web.virtualwallet.exceptions.TransferFailedException;
import com.telerikacademy.web.virtualwallet.models.Currency;
import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.Transfer;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;
import com.telerikacademy.web.virtualwallet.repositories.contracts.WalletRepository;
import com.telerikacademy.web.virtualwallet.services.contracts.CurrencyService;
import com.telerikacademy.web.virtualwallet.services.contracts.WalletService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class WalletServiceImpl implements WalletService {

    private static final String WALLET_PERMISSION_ERROR = "You are not authorized for this wallet.";

    private final WalletRepository walletRepository;

    private final CurrencyService currencyService;

    private static final  String apiUrl = "https://65df74a2ff5e305f32a25197.mockapi.io/api/card/transfer";

    private static final String resetUrl = "https://mockapi.io/api/mocks/65df74a2ff5e305f32a25198/resources/reset_all";


    public WalletServiceImpl(WalletRepository walletRepository, CurrencyService currencyService) {
        this.walletRepository = walletRepository;
        this.currencyService = currencyService;
    }

    @Override
    public Wallet get(int id, User user) {
        checkModifyPermissions(id,user);
        return walletRepository.getById(id);
    }

    @Override
    public Wallet getByUser(User user) {
        return walletRepository.getByUser(user);
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
        Wallet wallet = walletRepository.getById(walletId);
        wallet.setHoldings(wallet.getHoldings() + funds);
        update(wallet);
    }

    @Override
    public void subtractFunds(int walletId, double funds) {
        Wallet wallet = walletRepository.getById(walletId);
        if (wallet.getHoldings() < funds){
            throw new FundsSupplyException();
        }
        wallet.setHoldings(wallet.getHoldings() - funds);
        update(wallet);
    }

    @Override
    public void changeCurrency(int walletId, Currency currency) {
//        Wallet wallet = get(walletId);  we might need the checkModifyPermissions here
        Wallet wallet = walletRepository.getById(walletId);
        wallet.setCurrency(currency);
        update(wallet);
    }

    @Override
    public void transfer(Transfer transfer) {
        String responseBody = "";

        try {
            responseBody = cardTransferRequest();
        }catch (RuntimeException e){
            resetTransferLimit();
            responseBody = cardTransferRequest();
        }

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

    private void resetTransferLimit(){
        HttpHeaders headers = new HttpHeaders();

        headers.set("Token",
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJfaWQiOiI2NWRmNzQ4NmZmNWUzMDVmMzJhMjUxNzEiLCJpYXQiOjE3MDkxNDMxNzQ5NzYsImV4cCI6MTc3MjIxNTE3NDk3Nn0.1bcAm-1RHCShgLnd8AfA_Og3H8Ufh04SvwYDBira0HQ");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.exchange(
                resetUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
    }

    private String cardTransferRequest(){
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                null,  // or requestEntity if you have headers and payload
                String.class
        ).getBody();
    }

    public Wallet createDefaultWallet(User user){
        Wallet wallet = new Wallet();
        wallet.setName("Personal");
        wallet.setHolder(user);
        wallet.setHoldings(0.0);
        wallet.setCurrency(currencyService.getById(1));
        return wallet;
    }

    private void checkModifyPermissions(int id, User user) {
        user.getWallets().stream().filter(wallet -> wallet.getId() == id)
                .findFirst()
                .orElseThrow(() -> new AuthorizationException(WALLET_PERMISSION_ERROR));
    }

    }
