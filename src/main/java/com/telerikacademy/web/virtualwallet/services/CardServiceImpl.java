package com.telerikacademy.web.virtualwallet.services;

import com.telerikacademy.web.virtualwallet.exceptions.EntityDuplicateException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtualwallet.models.Card;
import com.telerikacademy.web.virtualwallet.repositories.contracts.CardRepository;
import com.telerikacademy.web.virtualwallet.repositories.contracts.UserRepository;
import com.telerikacademy.web.virtualwallet.services.contracts.CardService;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    private final UserRepository userRepository;
    @Autowired
    public CardServiceImpl(CardRepository cardRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }


    @Override
    public Card get(int id) {
        return cardRepository.getByField("id",id);
    }

    @Override
    public void create(Card card) {
        boolean duplicateExists = true;
        try {
            cardRepository.getByField("number",card.getNumber());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists){
            throw new EntityDuplicateException("Card", "Number", card.getNumber());
        }
        cardRepository.create(card);
    }

    @Override
    public void delete(int id) {
        cardRepository.delete(id);
    }
}
