package com.telerikacademy.web.virtualwallet.repositories;

import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.repositories.contracts.TransactionRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepositoryImpl extends AbstractCRUDRepository<Transaction> implements TransactionRepository {

    public TransactionRepositoryImpl(Class<Transaction> clazz, SessionFactory sessionFactory) {
        super(clazz, sessionFactory);
    }
}
