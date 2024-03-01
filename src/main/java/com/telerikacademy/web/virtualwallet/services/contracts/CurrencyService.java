package com.telerikacademy.web.virtualwallet.services.contracts;

import com.telerikacademy.web.virtualwallet.models.Currency;

import java.util.List;

public interface CurrencyService {
//TODO finish this
    Currency getById(int id);

    List<Currency> getAll();

    void create(Currency currency);

    void update(Currency currency);

    void delete(int id);
}
