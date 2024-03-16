package com.telerikacademy.web.virtualwallet.models.dtos;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class TransactionDto {
    @NotNull(message = "Receiver can not be empty")
    private String receiver;

    @NotNull(message = "Amount can't be empty")
    @Digits(integer = 10, fraction = 2)
    @Min(value = 1)
    private int amount;


    public TransactionDto(){
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
