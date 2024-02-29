package com.telerikacademy.web.virtualwallet.repositories;

import com.telerikacademy.web.virtualwallet.models.Transfer;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;
import com.telerikacademy.web.virtualwallet.repositories.contracts.WalletRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;


@Repository
public class WalletRepositoryImpl extends AbstractCRUDRepository<Wallet> implements WalletRepository {


    public WalletRepositoryImpl(SessionFactory sessionFactory) {
        super(Wallet.class, sessionFactory);
    }

    @Override
    public void transfer(Transfer transfer){
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(transfer);
            session.getTransaction().commit();
        }

    }
}
