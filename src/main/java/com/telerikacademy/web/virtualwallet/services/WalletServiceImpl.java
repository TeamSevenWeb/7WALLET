package com.telerikacademy.web.virtualwallet.services;

import com.telerikacademy.web.virtualwallet.exceptions.FundsSupplyException;
import com.telerikacademy.web.virtualwallet.models.Currency;
import com.telerikacademy.web.virtualwallet.models.Wallet;
import com.telerikacademy.web.virtualwallet.repositories.contracts.WalletRepository;
import com.telerikacademy.web.virtualwallet.services.contracts.WalletService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }


    @Override
    public Wallet get(int id) {
        return walletRepository.getById(id);
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
        walletRepository.update(wallet);
    }

    @Override
    public void subtractFunds(int walletId, double funds) {
        Wallet wallet = get(walletId);
        if (wallet.getHoldings() < funds){
            throw new FundsSupplyException();
        }
        wallet.setHoldings(wallet.getHoldings() - funds);
        walletRepository.update(wallet);
    }

    @Override
    public void changeCurrency(int walletId, Currency currency) {
        Wallet wallet = get(walletId);
        wallet.setCurrency(currency);
    }
}
