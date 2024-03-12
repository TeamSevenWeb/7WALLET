package com.telerikacademy.web.virtualwallet.repositories;

import com.telerikacademy.web.virtualwallet.models.Card;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;
import com.telerikacademy.web.virtualwallet.repositories.contracts.CardRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

import static java.lang.String.format;

@Repository
public class CardRepositoryImpl extends AbstractCRUDRepository<Card> implements CardRepository {

    @Autowired
    public CardRepositoryImpl(SessionFactory sessionFactory) {
        super(Card.class, sessionFactory);
    }

    @Override
    public List<Card> getAllByUser(User holder){
        try (Session session = sessionFactory.openSession()) {
            Query<Card> query = session.createQuery("from Card where holder = :user", Card.class);
            query.setParameter("user", holder);

            return query.list();
        }
    }
}
