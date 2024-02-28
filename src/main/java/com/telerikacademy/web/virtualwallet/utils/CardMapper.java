package com.telerikacademy.web.virtualwallet.utils;

import com.telerikacademy.web.virtualwallet.models.Card;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.CardDto;
import com.telerikacademy.web.virtualwallet.services.contracts.CardService;
import com.telerikacademy.web.virtualwallet.services.contracts.UserService;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {

    private final CardService cardService;

    private final UserService userService;

    public CardMapper(CardService cardService, UserService userService) {
        this.cardService = cardService;
        this.userService = userService;
    }

    public Card fromDto(CardDto cardDto) {
        Card card = new Card();
        card.setNumber(cardDto.getNumber());
        User holder = userService.getByUsername(cardDto.getHolder());
        card.setHolder(holder);
        card.setCvv(cardDto.getCvv());
        card.setExpirationDate(cardDto.getExpirationDate());
        return card;
    }
}
