package com.telerikacademy.web.virtualwallet.services.contracts;

import com.telerikacademy.web.virtualwallet.models.Currency;
import com.telerikacademy.web.virtualwallet.models.User;

import java.util.List;

public interface CurrencyService {

    Currency getById(int id);

    List<Currency> getAll(User user);

    void create(Currency currency, User user);

    void update(Currency currency,User user);

    void delete(int id,User user);
}
