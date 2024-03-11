package com.telerikacademy.web.virtualwallet.services.contracts;

import com.telerikacademy.web.virtualwallet.models.Currency;
import com.telerikacademy.web.virtualwallet.models.Transfer;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.wallets.JoinWallet;

import java.util.List;

public interface JoinWalletService {

    JoinWallet get(int id,User user);

    List<JoinWallet> getAll();

    List<JoinWallet> getAllByUser(User user);

    void create(JoinWallet wallet, User user);

    void update(JoinWallet wallet,User user, int id);

    void delete(int id);

    List<User> getAllUsers(int walletId);

    void addUser(int walletId, String user, User owner);

    void removeOtherUser(int walletId, String user, User owner);

    void  removeWallet(int walletId, User user);

}
