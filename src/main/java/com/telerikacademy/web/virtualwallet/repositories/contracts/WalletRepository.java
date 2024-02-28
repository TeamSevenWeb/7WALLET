package com.telerikacademy.web.virtualwallet.repositories.contracts;

import com.telerikacademy.web.virtualwallet.models.Transfer;
import com.telerikacademy.web.virtualwallet.models.Wallet;

public interface WalletRepository extends BaseCRUDRepository<Wallet>{

    void transfer(Transfer transfer);
}
