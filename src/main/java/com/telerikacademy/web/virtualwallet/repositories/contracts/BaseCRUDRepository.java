package com.telerikacademy.web.virtualwallet.repositories.contracts;

import java.util.List;

public interface BaseCRUDRepository<T> {

    void delete(int id);

    void create(T entity);

    void update(T entity);


    List<T> getAll();

    T getById(int id);

    <V> T getByField(String name, V value);
}
