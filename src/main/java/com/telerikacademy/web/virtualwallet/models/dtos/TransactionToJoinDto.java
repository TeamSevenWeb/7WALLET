package com.telerikacademy.web.virtualwallet.models.dtos;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class TransactionToJoinDto {

    @NotNull(message = "Wallet can not be empty")
    private int walletId;

    @NotNull(message = "Amount can't be empty")
    @Digits(integer = 10, fraction = 2)
    @Min(value = 1)
    private double amount;

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
