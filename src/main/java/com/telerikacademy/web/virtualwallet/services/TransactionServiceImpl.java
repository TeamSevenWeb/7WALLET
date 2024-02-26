package com.telerikacademy.web.virtualwallet.services;


import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.services.contracts.TransactionService;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Override
    public Transaction getById(int id) {
        return null;
    }

    @Override
    public void create(Transaction transaction, User user) {

    }
}
