package com.telerikacademy.web.virtualwallet.repositories;

import com.telerikacademy.web.virtualwallet.models.TransactionVerificationCodes;
import com.telerikacademy.web.virtualwallet.repositories.contracts.TransactionVerificationCodesRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionVerificationCodesRepositoryImpl
        extends AbstractCRUDRepository<TransactionVerificationCodes>
        implements TransactionVerificationCodesRepository {
    public TransactionVerificationCodesRepositoryImpl(SessionFactory sessionFactory) {
        super(TransactionVerificationCodes.class, sessionFactory);
    }
}
