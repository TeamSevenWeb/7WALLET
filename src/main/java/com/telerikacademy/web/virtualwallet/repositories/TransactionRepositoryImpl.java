package com.telerikacademy.web.virtualwallet.repositories;

import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.repositories.contracts.TransactionRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepositoryImpl extends AbstractCRUDRepository<Transaction> implements TransactionRepository {

    @Autowired
    public TransactionRepositoryImpl(SessionFactory sessionFactory) {
        super(Transaction.class,sessionFactory);
    }

    @Override
    public void createMultiple(Transaction transaction1, Transaction transaction2) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(transaction1);
            session.persist(transaction2);
            session.getTransaction().commit();
        }
    }
}
