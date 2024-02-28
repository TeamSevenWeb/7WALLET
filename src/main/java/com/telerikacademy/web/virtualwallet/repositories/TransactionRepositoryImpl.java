package com.telerikacademy.web.virtualwallet.repositories;

import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.repositories.contracts.TransactionRepository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepositoryImpl extends AbstractCRUDRepository<Transaction> implements TransactionRepository {

    @Autowired
    public TransactionRepositoryImpl(SessionFactory sessionFactory) {
        super(Transaction.class,sessionFactory);
    }
}
