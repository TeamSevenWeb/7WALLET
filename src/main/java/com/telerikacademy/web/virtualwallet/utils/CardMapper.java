package com.telerikacademy.web.virtualwallet.utils;

import com.telerikacademy.web.virtualwallet.models.Card;
import com.telerikacademy.web.virtualwallet.models.dtos.CardDto;
import com.telerikacademy.web.virtualwallet.services.contracts.CardService;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {

    private final CardService cardService;

    public CardMapper(CardService cardService) {
        this.cardService = cardService;
    }

    public Card fromDto(CardDto cardDto) {
        Card card = new Card();
        card.setNumber(cardDto.getNumber());
        card.setHolder(cardDto.getHolder());
        card.setCvv(cardDto.getCvv());
        card.setExpirationDate(cardDto.getExpirationDate());
        return card;
    }
}
