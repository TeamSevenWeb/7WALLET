package com.telerikacademy.web.virtualwallet.services;

import com.telerikacademy.web.virtualwallet.exceptions.AuthenticationException;
import com.telerikacademy.web.virtualwallet.exceptions.AuthorizationException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityDuplicateException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtualwallet.models.Card;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.repositories.contracts.CardRepository;
import com.telerikacademy.web.virtualwallet.repositories.contracts.UserRepository;
import com.telerikacademy.web.virtualwallet.services.contracts.CardService;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    private static final String MODIFY_CARD_ERROR_MESSAGE = "You are not the holder of this card!";
    @Autowired
    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }


    @Override
    public Card get(User holder, int id) {
        Card card = cardRepository.getById(id);
        checkModifyPermissions(holder,card);
        return card;
    }

    @Override
    public List<Card> getUsersCards(User holder) {
        return cardRepository.getAllByUser(holder);
    }

    @Override
    public void create(User holder, Card card) {
        boolean duplicateExists = true;
        try {
            Card existingCard = cardRepository.getByField("number",card.getNumber());
            if(!existingCard.getHolder().equals(holder)){
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }
        if (duplicateExists){
            throw new EntityDuplicateException("Please enter a different card.");
        }
        cardRepository.create(card);
    }

    @Override
    public void delete(User holder, int id) {
        Card card = cardRepository.getById(id);
        checkModifyPermissions(holder,card);
        cardRepository.delete(id);
    }

    private void checkModifyPermissions(User holder, Card card) {
        if (!card.getHolder().equals(holder)) {
            throw new AuthorizationException(MODIFY_CARD_ERROR_MESSAGE);
        }
    }
}
