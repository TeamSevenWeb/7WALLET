package com.telerikacademy.web.virtualwallet.services.contracts;

import com.telerikacademy.web.virtualwallet.models.Card;
import com.telerikacademy.web.virtualwallet.models.User;

public interface CardService {


    Card get(User holder, int id);

    void create(User holder, Card card);

    void delete(User holder, int id);


}
