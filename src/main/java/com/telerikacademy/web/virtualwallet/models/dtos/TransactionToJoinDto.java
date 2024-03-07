package com.telerikacademy.web.virtualwallet.models.dtos;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

public class TransactionToJoinDto {

    @NotNull(message = "Amount can't be empty")
    @Digits(integer = 10, fraction = 2)
    private double amount;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
