package com.telerikacademy.web.virtualwallet.repositories.contracts;

import com.telerikacademy.web.virtualwallet.models.Transaction;

public interface TransactionRepository extends BaseCRUDRepository<Transaction> {

    void createMultiple(Transaction transaction1, Transaction transaction2);

}
