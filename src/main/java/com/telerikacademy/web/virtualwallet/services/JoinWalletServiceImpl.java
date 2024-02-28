package com.telerikacademy.web.virtualwallet.services;

import com.telerikacademy.web.virtualwallet.exceptions.FundsSupplyException;
import com.telerikacademy.web.virtualwallet.models.Currency;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.wallets.JoinWallet;
import com.telerikacademy.web.virtualwallet.repositories.contracts.JoinWalletRepository;
import com.telerikacademy.web.virtualwallet.services.contracts.JoinWalletService;

import java.util.List;

public class JoinWalletServiceImpl implements JoinWalletService {

    private final JoinWalletRepository joinWalletRepository;

    public JoinWalletServiceImpl(JoinWalletRepository joinWalletRepository) {
        this.joinWalletRepository = joinWalletRepository;
    }


    @Override
    public JoinWallet get(int id) {
        return joinWalletRepository.getById(id);
    }

    @Override
    public List<JoinWallet> getAll() {
        return joinWalletRepository.getAll();
    }

    @Override
    public void create(JoinWallet wallet) {
        joinWalletRepository.create(wallet);
    }

    @Override
    public void update(JoinWallet wallet) {
        joinWalletRepository.update(wallet);
    }

    @Override
    public void delete(int id) {
        joinWalletRepository.delete(id);
    }

    @Override
    public void addFunds(int walletId, double funds) {
        JoinWallet wallet = get(walletId);
        wallet.setHoldings(wallet.getHoldings() + funds);
        joinWalletRepository.update(wallet);
    }

    @Override
    public void subtractFunds(int walletId, double funds) {
        JoinWallet wallet = get(walletId);
        if (wallet.getHoldings() < funds){
            throw new FundsSupplyException();
        }
        wallet.setHoldings(wallet.getHoldings() - funds);
        joinWalletRepository.update(wallet);
    }

    @Override
    public void changeCurrency(int walletId, Currency currency) {
        JoinWallet wallet = get(walletId);
        wallet.setCurrency(currency.getCurrencyCode());
        joinWalletRepository.update(wallet);
    }

    @Override
    public List<User> getAllUsers(int walletId) {
        JoinWallet joinWallet = get(walletId);
        return joinWallet.getUsers().stream().toList();
    }

    @Override
    public void addUser(int walletId, User user) {
        JoinWallet joinWallet = get(walletId);
        joinWallet.getUsers().add(user);
        joinWalletRepository.update(joinWallet);
    }

    @Override
    public void removeUser(int walletId, User user) {
        JoinWallet joinWallet = get(walletId);
        joinWallet.getUsers().remove(user);
        joinWalletRepository.update(joinWallet);
    }
}
