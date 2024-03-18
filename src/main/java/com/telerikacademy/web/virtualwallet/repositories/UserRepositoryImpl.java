package com.telerikacademy.web.virtualwallet.repositories;

import com.telerikacademy.web.virtualwallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtualwallet.filters.UserFilterOptions;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.repositories.contracts.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

@Repository
public class UserRepositoryImpl extends AbstractCRUDRepository<User> implements UserRepository {

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        super(User.class, sessionFactory);
    }

    @Override
    public User searchByAnyMatch(String parameter) {
        final String query = format("from %s WHERE :value IN (username, phoneNumber, email)", clazz.getSimpleName());
        final String notFoundErrorMessage = format("%s is not found", clazz.getSimpleName());

        try (Session session = sessionFactory.openSession()) {
            return session
                    .createQuery(query, clazz)
                    .setParameter("value", parameter)
                    .uniqueResultOptional()
                    .orElseThrow(() -> new EntityNotFoundException(notFoundErrorMessage));
        }
    }

    @Override
    public List<User> getAllUsersFiltered(UserFilterOptions filterOptions) {
        try (Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            filterOptions.getUsername().ifPresent(value -> {
                filters.add("username like :username");
                params.put("username",String.format("%%%s%%", value));
            });

            filterOptions.getEmail().ifPresent(value -> {
                filters.add("email like :email");
                params.put("email",String.format("%%%s%%", value));
            });

            filterOptions.getPhoneNumber().ifPresent(value -> {
                filters.add("phoneNumber like :phone_number");
                params.put("phone_number",String.format("%%%s%%", value));
            });

            StringBuilder queryString = new StringBuilder("from User");
            if (!filters.isEmpty()) {
                queryString
                        .append(" where ")
                        .append(String.join(" and ", filters));
            }

            Query<User> query = session.createQuery(queryString.toString(), User.class);
            query.setProperties(params);

            List<User> result = query.list();
            if (result.isEmpty()){
                throw new EntityNotFoundException("User","these","filters");
            }
            return result;
        }
    }

}
