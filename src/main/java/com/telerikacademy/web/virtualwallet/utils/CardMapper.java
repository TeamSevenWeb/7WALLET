package com.telerikacademy.web.virtualwallet.utils;

import com.telerikacademy.web.virtualwallet.models.Card;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.CardDto;
import com.telerikacademy.web.virtualwallet.repositories.contracts.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {

    private final UserRepository userRepository;

    public CardMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Card fromDto(CardDto cardDto) {
        Card card = new Card();
        card.setNumber(cardDto.getNumber());
        User holder = userRepository.getByField("firstName",cardDto.getHolder());
        card.setHolder(holder);
        card.setCvv(cardDto.getCvv());
        card.setExpirationDate(cardDto.getExpirationDate());
        return card;
    }
}
