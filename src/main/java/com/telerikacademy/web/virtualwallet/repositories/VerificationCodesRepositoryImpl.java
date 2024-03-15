package com.telerikacademy.web.virtualwallet.repositories;

import com.telerikacademy.web.virtualwallet.models.VerificationCodes;
import com.telerikacademy.web.virtualwallet.repositories.contracts.VerificationCodesRepository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class VerificationCodesRepositoryImpl extends AbstractCRUDRepository<VerificationCodes> implements VerificationCodesRepository {

    @Autowired
    public VerificationCodesRepositoryImpl(SessionFactory sessionFactory) {
        super(VerificationCodes.class, sessionFactory);
    }
}
