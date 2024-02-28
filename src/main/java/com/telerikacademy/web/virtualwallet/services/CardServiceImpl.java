package com.telerikacademy.web.virtualwallet.services;

import com.telerikacademy.web.virtualwallet.models.Card;
import com.telerikacademy.web.virtualwallet.repositories.contracts.CardRepository;
import com.telerikacademy.web.virtualwallet.services.contracts.CardService;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }


    @Override
    public Card get(int id) {
        return cardRepository.getByField("id",id);
    }

    @Override
    public void create(Card card) {
        cardRepository.create(card);
    }

    @Override
    public void delete(int id) {
        cardRepository.delete(id);
    }
}
