package com.telerikacademy.web.virtualwallet.services.contracts;

import com.telerikacademy.web.virtualwallet.models.Currency;
import com.telerikacademy.web.virtualwallet.models.Transfer;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;

import java.util.List;

public interface WalletService {

    Wallet get(int id, User user);

    Wallet getByUser(User user);

    List<Wallet> getAll();

    void create(Wallet wallet);

    Wallet createDefaultWallet(User user);

    void update(Wallet wallet);

    void addFunds(int walletId, double funds);

    void subtractFunds(int walletId, double funds);

    void changeCurrency(int walletId,int currencyId);

    void transfer(Transfer transfer);

}
