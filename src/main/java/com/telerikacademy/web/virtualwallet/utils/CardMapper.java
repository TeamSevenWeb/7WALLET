package com.telerikacademy.web.virtualwallet.utils;

import com.telerikacademy.web.virtualwallet.exceptions.AuthorizationException;
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

    public Card fromDto(User holder, CardDto cardDto) {
        Card card = new Card();
        card.setNumber(cardDto.getNumber());
        if(!holder.getFirstName().equals(cardDto.getHolder())){
            throw new AuthorizationException("Check card details.");
        };
        card.setHolder(holder);
        holder.getUserCards().add(card);
        card.setCvv(cardDto.getCvv());
        card.setExpirationDate(cardDto.getExpirationDate());
        return card;
    }

}
