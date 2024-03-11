package com.telerikacademy.web.virtualwallet.services;

import com.telerikacademy.web.virtualwallet.exceptions.AuthorizationException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityDuplicateException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtualwallet.models.Currency;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.repositories.contracts.CurrencyRepository;
import com.telerikacademy.web.virtualwallet.services.contracts.CurrencyService;
import com.telerikacademy.web.virtualwallet.utils.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CurrencyServiceImpl implements CurrencyService {

    public static final String ONLY_ADMINS_ERR = "Only admins can manage currencies";
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
    public void create(Currency currency,User user) {
        checkIsDuplicated(currency);
        checkAdmin(user);
        currencyRepository.create(currency);
    }

    @Override
    public void update(Currency currency,User user) {
        checkIsDuplicated(currency, currency.getId());
        checkAdmin(user);
        currencyRepository.update(currency);
    }

    @Override
    public void delete(int id,User user) {
        checkAdmin(user);
        currencyRepository.delete(id);
    }

    private void checkAdmin(User user){
        if (!isAdmin(user)){
            throw new AuthorizationException(ONLY_ADMINS_ERR);
        }
    }

    private boolean isAdmin(User user) {
        return user.getUserRoles().stream().anyMatch(r -> r.getRoleType().equals(UserRole.admin.toString()));
    }

    private void checkIsDuplicated(Currency currency) {
        try {
            currencyRepository.getByField("currencyCode", currency.getCurrencyCode());
            throw new EntityDuplicateException("Currency","code", currency.getCurrencyCode());
        } catch (EntityNotFoundException ignore) {
        }
    }

    private void checkIsDuplicated(Currency currency, int id) {
        try {
            Currency currencyToCheck = currencyRepository.getByField("currencyCode", currency.getCurrencyCode());
            if (currencyToCheck.getId() != id) {
                throw new EntityDuplicateException("Currency", "code", currency.getCurrencyCode());
            }
        } catch (EntityNotFoundException ignore) {
        }
    }


}
