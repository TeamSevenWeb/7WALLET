package com.telerikacademy.web.virtualwallet.services.contracts;

import com.telerikacademy.web.virtualwallet.models.Card;

public interface CardService {


    Card get(int id);

    void create(Card card);

    void delete(int id);


}
