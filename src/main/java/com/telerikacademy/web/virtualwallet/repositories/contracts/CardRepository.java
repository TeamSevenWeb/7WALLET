package com.telerikacademy.web.virtualwallet.repositories.contracts;

import com.telerikacademy.web.virtualwallet.models.Card;
import com.telerikacademy.web.virtualwallet.models.User;

import java.util.List;

public interface CardRepository extends BaseCRUDRepository<Card>{
    List<Card> getAllByUser(User holder);
}
