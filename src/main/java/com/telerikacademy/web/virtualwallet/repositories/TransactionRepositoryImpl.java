package com.telerikacademy.web.virtualwallet.repositories;

import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.repositories.contracts.TransactionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    @Override
    public void delete(int id) {

    }

    @Override
    public void create(Transaction entity) {

    }

    @Override
    public void update(Transaction entity) {

    }

    @Override
    public List<Transaction> getAll() {
        return null;
    }

    @Override
    public Transaction getById(int id) {
        return null;
    }

    @Override
    public <V> Transaction getByField(String name, V value) {
        return null;
    }
}
