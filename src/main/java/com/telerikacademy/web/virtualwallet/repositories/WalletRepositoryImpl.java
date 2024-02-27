package com.telerikacademy.web.virtualwallet.repositories;

import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;
import com.telerikacademy.web.virtualwallet.repositories.contracts.WalletRepository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class WalletRepositoryImpl extends AbstractCRUDRepository<Wallet> implements WalletRepository {


    @Autowired
    public WalletRepositoryImpl(SessionFactory sessionFactory) {
        super(Wallet.class, sessionFactory);
    }
}
