package com.telerikacademy.web.virtualwallet.repositories.contracts;

import com.telerikacademy.web.virtualwallet.filters.TransactionFilterOptions;
import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.User;

import java.util.List;

public interface TransactionRepository extends BaseCRUDRepository<Transaction> {

    List<Transaction> filterAndSort(User user, TransactionFilterOptions filterOptions);

}
