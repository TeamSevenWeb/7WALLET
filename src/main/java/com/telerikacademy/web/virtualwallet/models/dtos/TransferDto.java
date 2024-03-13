package com.telerikacademy.web.virtualwallet.models.dtos;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

public class TransferDto {

    @NotNull(message = "Card can not be empty")
    private int cardId;

    @NotNull(message = "Amount can't be empty")
    @Digits(integer = 10, fraction = 2)
    private long amount;

    public TransferDto(){
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
