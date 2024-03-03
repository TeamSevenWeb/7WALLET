package com.telerikacademy.web.virtualwallet.models.dtos;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

public class TransferDto {

    @NotNull(message = "Card can not be empty")
    private CardDto card;

    @NotNull(message = "Amount can't be empty")
    @Digits(integer = 10, fraction = 2)
    private long amount;

    public TransferDto(){
    }

    public CardDto getCard() {
        return card;
    }

    public void setCard(CardDto card) {
        this.card = card;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
