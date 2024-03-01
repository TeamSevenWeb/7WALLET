package com.telerikacademy.web.virtualwallet.repositories;

import com.telerikacademy.web.virtualwallet.models.Currency;
import com.telerikacademy.web.virtualwallet.repositories.contracts.CurrencyRepository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CurrencyRepositoryImpl extends AbstractCRUDRepository<Currency> implements CurrencyRepository {
    @Autowired
    public CurrencyRepositoryImpl(SessionFactory sessionFactory) {
        super(Currency.class, sessionFactory);
    }
}
