package com.telerikacademy.web.virtualwallet.services;

import com.telerikacademy.web.virtualwallet.models.Currency;
import com.telerikacademy.web.virtualwallet.repositories.contracts.CurrencyRepository;
import com.telerikacademy.web.virtualwallet.services.contracts.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }


    @Override
    public Currency getById(int id) {
        return currencyRepository.getById(id);
    }

    @Override
    public List<Currency> getAll() {
        return currencyRepository.getAll();
    }

    @Override
    public void create(Currency currency) {
        currencyRepository.create(currency);
    }

    @Override
    public void update(Currency currency) {
        currencyRepository.update(currency);
    }

    @Override
    public void delete(int id) {
        currencyRepository.delete(id);
    }


}
