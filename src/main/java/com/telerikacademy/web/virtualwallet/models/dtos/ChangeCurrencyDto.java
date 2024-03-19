package com.telerikacademy.web.virtualwallet.models.dtos;

import jakarta.validation.constraints.NotNull;

public class ChangeCurrencyDto {

    @NotNull(message = "Currency can not be empty")
    private int currencyId;

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }
}
