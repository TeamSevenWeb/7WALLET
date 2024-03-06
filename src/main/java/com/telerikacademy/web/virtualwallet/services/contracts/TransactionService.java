package com.telerikacademy.web.virtualwallet.services.contracts;

import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.User;

public interface TransactionService {

    Transaction getById(int id, User user);

    void create(Transaction outgoing,Transaction ingoing, User user);
}
