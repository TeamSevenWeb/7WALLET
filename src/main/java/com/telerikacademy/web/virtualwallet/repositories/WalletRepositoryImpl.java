package com.telerikacademy.web.virtualwallet.repositories;

import com.telerikacademy.web.virtualwallet.models.Wallet;
import com.telerikacademy.web.virtualwallet.repositories.contracts.WalletRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WalletRepositoryImpl implements WalletRepository {
    @Override
    public void delete(int id) {

    }

    @Override
    public void create(Wallet entity) {

    }

    @Override
    public void update(Wallet entity) {

    }

    @Override
    public List<Wallet> getAll() {
        return null;
    }

    @Override
    public Wallet getById(int id) {
        return null;
    }

    @Override
    public <V> Wallet getByField(String name, V value) {
        return null;
    }
}
