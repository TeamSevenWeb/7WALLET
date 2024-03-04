package com.telerikacademy.web.virtualwallet.utils;

import com.telerikacademy.web.virtualwallet.models.Card;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.CardDto;
import com.telerikacademy.web.virtualwallet.repositories.contracts.UserRepository;
import com.telerikacademy.web.virtualwallet.services.contracts.UserService;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {

    private final UserService userService;

    public CardMapper(UserService userService) {
        this.userService = userService;
    }

    public Card fromDto(CardDto cardDto) {
        Card card = new Card();
        card.setNumber(cardDto.getNumber());
        User holder = userService.getByFirstName(cardDto.getHolder());
        holder.getUserCards().add(card);
        card.setHolder(holder);
        card.setCvv(cardDto.getCvv());
        card.setExpirationDate(cardDto.getExpirationDate());
        return card;
    }
}
