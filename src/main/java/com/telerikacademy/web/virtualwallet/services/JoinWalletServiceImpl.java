package com.telerikacademy.web.virtualwallet.services;

import com.telerikacademy.web.virtualwallet.exceptions.FundsSupplyException;
import com.telerikacademy.web.virtualwallet.models.Currency;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.wallets.JoinWallet;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;
import com.telerikacademy.web.virtualwallet.repositories.contracts.JoinWalletRepository;
import com.telerikacademy.web.virtualwallet.services.contracts.CurrencyService;
import com.telerikacademy.web.virtualwallet.services.contracts.JoinWalletService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class JoinWalletServiceImpl implements JoinWalletService {

    private final JoinWalletRepository joinWalletRepository;

    private final CurrencyService currencyService;

    public JoinWalletServiceImpl(JoinWalletRepository joinWalletRepository, CurrencyService currencyService) {
        this.joinWalletRepository = joinWalletRepository;
        this.currencyService = currencyService;
    }


    @Override
    public JoinWallet get(int id) {
        return joinWalletRepository.getById(id);
    }

    @Override
    public List<JoinWallet> getAll() {
        return joinWalletRepository.getAll();
    }

    public List<JoinWallet>getAllByUser(User user){
        return  joinWalletRepository.getAllByUser(user);
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
        wallet.setCurrency(currency);
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

    public JoinWallet createJoinWallet(User user, String name){
        JoinWallet wallet = new JoinWallet();
        wallet.setName(name);
        wallet.setHolder(user);
        wallet.setHoldings(0.0);
        wallet.setCurrency(currencyService.getById(1));
        return wallet;
    }
}
