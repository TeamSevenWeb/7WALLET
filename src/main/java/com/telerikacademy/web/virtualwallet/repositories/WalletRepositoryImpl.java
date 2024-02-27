package com.telerikacademy.web.virtualwallet.repositories;

import com.telerikacademy.web.virtualwallet.models.Wallet;
import com.telerikacademy.web.virtualwallet.repositories.contracts.WalletRepository;
import com.telerikacademy.web.virtualwallet.services.contracts.WalletService;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WalletRepositoryImpl extends AbstractCRUDRepository<Wallet> implements WalletRepository {


    @Autowired
    public WalletRepositoryImpl(SessionFactory sessionFactory) {
        super(Wallet.class, sessionFactory);
    }
}
