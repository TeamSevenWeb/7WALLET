package com.telerikacademy.web.virtualwallet.services.contracts;

import com.telerikacademy.web.virtualwallet.models.Currency;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.wallets.JoinWallet;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;

import java.util.List;

public interface JoinWalletService {

    JoinWallet get(int id);

    List<JoinWallet> getAll();

    void create(JoinWallet wallet);

    void update(JoinWallet wallet);

    void delete(int id);

    void addFunds(int walletId, double funds);

    void subtractFunds(int walletId, double funds);

    void changeCurrency(int walletId, Currency currency);

    List<User> getAllUsers(int walletId);

    void addUser(int walletId, User user);

    void removeUser(int walletId, User user);
}
