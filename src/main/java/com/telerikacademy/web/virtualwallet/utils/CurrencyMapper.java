package com.telerikacademy.web.virtualwallet.utils;

import com.telerikacademy.web.virtualwallet.models.Currency;
import com.telerikacademy.web.virtualwallet.models.dtos.CurrencyDto;
import com.telerikacademy.web.virtualwallet.services.contracts.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CurrencyMapper {

    CurrencyService currencyService;

    @Autowired
    public CurrencyMapper(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    public Currency fromDto(int id, CurrencyDto currencyDto){
        Currency currency = fromDto(currencyDto);
        currency.setId(id);
        return currency;
    }

    public Currency fromDto(CurrencyDto currencyDto){
        Currency currency = new Currency();
        currency.setCurrencyCode(currencyDto.getCurrencyCode().toUpperCase());
        currency.setRating(currencyDto.getRating());
        return currency;
    }
}
