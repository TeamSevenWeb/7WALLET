package com.telerikacademy.web.virtualwallet.repositories;

import com.telerikacademy.web.virtualwallet.repositories.contracts.BaseCRUDRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

import static java.lang.String.format;

public abstract class AbstractCRUDRepository<T>  implements BaseCRUDRepository<T> {

    protected final SessionFactory sessionFactory;
    protected final Class<T> tClass;
    public AbstractCRUDRepository(Class<T> tClass,SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.tClass = tClass;
    }

    @Override
    public void create(T entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(T entity){
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id){
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(getById(id));
            session.getTransaction().commit();
        }
    }

    @Override
    public <V> T getByField(String name, V value) {
        final String query = format("from %s where %s = :value", tClass.getName(), name);
        final String notFoundErrorMessage = format("%s with %s %s not found", tClass.getSimpleName(), name, value);

        try (Session session = sessionFactory.openSession()) {
            return session
                    .createQuery(query, tClass)
                    .setParameter("value", value)
                    .uniqueResultOptional()
                    .orElseThrow(() -> new RuntimeException(notFoundErrorMessage));
        }

    }

    @Override
    public T getById(int id) {
        return getByField("id", id);
    }

    @Override
    public List<T> getAll(){
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(format("from %s",tClass.getName()),tClass).list();
        }
    }

}
