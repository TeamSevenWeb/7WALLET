package com.telerikacademy.web.virtualwallet.repositories.contracts;

import com.telerikacademy.web.virtualwallet.models.Transfer;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;

public interface WalletRepository extends BaseCRUDRepository<Wallet>{

    Wallet getByUser(User user);

    void transfer(Transfer transfer);
}
