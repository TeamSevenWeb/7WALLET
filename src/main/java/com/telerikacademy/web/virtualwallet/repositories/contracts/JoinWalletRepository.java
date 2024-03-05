package com.telerikacademy.web.virtualwallet.repositories.contracts;

import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.wallets.JoinWallet;

import java.util.List;

public interface JoinWalletRepository extends BaseCRUDRepository<JoinWallet>{

    JoinWallet getByUserAndName(User user, String name);

    List<JoinWallet> getAllByUser(User user);
}
