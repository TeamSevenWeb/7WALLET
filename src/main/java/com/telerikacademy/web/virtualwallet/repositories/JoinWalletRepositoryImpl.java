package com.telerikacademy.web.virtualwallet.repositories;

import com.telerikacademy.web.virtualwallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.wallets.JoinWallet;
import com.telerikacademy.web.virtualwallet.repositories.contracts.JoinWalletRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JoinWalletRepositoryImpl extends AbstractCRUDRepository<JoinWallet> implements JoinWalletRepository {

    @Autowired
    public JoinWalletRepositoryImpl(SessionFactory sessionFactory) {
        super(JoinWallet.class, sessionFactory);
    }

    @Override
    public JoinWallet getByUserAndName(User user, String walletName) {
        try (Session session = sessionFactory.openSession()) {
            Query<JoinWallet> query = session.createQuery("from JoinWallet where holder = :user and name = :name", JoinWallet.class);
            query.setParameter("user", user);
            query.setParameter("name", walletName);
            if (query.list().isEmpty()){
                throw new EntityNotFoundException("Wallet","name",walletName);
            }
            return query.list().get(0);
        }
    }

    public List<JoinWallet> getAllByUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            Query<JoinWallet> query = session.createQuery("from JoinWallet where holder = :user", JoinWallet.class);
            query.setParameter("user", user);

            return query.list();
        }
    }
}


