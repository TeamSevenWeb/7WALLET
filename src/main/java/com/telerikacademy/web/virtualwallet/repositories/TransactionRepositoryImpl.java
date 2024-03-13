package com.telerikacademy.web.virtualwallet.repositories;

import com.telerikacademy.web.virtualwallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtualwallet.exceptions.TransactionsNotFoundException;
import com.telerikacademy.web.virtualwallet.filters.TransactionFilterOptions;
import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.repositories.contracts.TransactionRepository;
import com.telerikacademy.web.virtualwallet.repositories.contracts.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
public class TransactionRepositoryImpl extends AbstractCRUDRepository<Transaction> implements TransactionRepository {

    private final UserRepository userRepository;
    @Autowired
    public TransactionRepositoryImpl(SessionFactory sessionFactory, UserRepository userRepository) {
        super(Transaction.class,sessionFactory);
        this.userRepository = userRepository;
    }

    @Override
    public List<Transaction> filterAndSort(User user,TransactionFilterOptions filterOptions){
        try (Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            filters.add("(sender = :user OR receiver = :user)");
            params.put("user", user);

            filterOptions.getReceiver().ifPresent(value -> {
                if(value.isEmpty()||user.getUsername().equals(value)){
                    return;
                }
                try {
                    User recipient = userRepository.getByField("username",value);
                    filters.add("receiver = :receiver");
                    params.put("receiver", recipient);
                } catch (EntityNotFoundException e){
                    throw new EntityNotFoundException("Transactions","receiver",value);
                }
            });

            if(filterOptions.getStartDate().isPresent()&&filterOptions.getEndDate().isPresent()){
                filters.add("date BETWEEN :startDate AND :endDate");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime startDate = LocalDateTime.parse(filterOptions.getStartDate().get() +" 00:00:00",formatter);
                LocalDateTime endDate = LocalDateTime.parse(filterOptions.getEndDate().get() + " 23:59:59",formatter);

                params.put("startDate", startDate);
                params.put("endDate", endDate);
            };


            filterOptions.getDirection().ifPresent(value -> {
                if(value.equals("INGOING")){
                    filters.add("receiver = :receiver");
                    params.put("receiver", user);
                }
                else {
                    filters.add("sender = :sender");
                    params.put("sender", user);
                }
            });

            StringBuilder queryString = new StringBuilder("from Transaction ");

            queryString.append(" where ")
                    .append(String.join(" and ", filters));
            queryString.append(generateOrderBy(filterOptions));

            Query<Transaction> query = session.createQuery(queryString.toString(), Transaction.class);
            query.setProperties(params);

            List<Transaction> result = query.list();
            if (result.isEmpty()){
                throw new TransactionsNotFoundException("Transaction","these","filters");
            }
            return result;
        }
    };

    private String generateOrderBy(TransactionFilterOptions filterOptions) {
        if (filterOptions.getSortBy().isEmpty()) {
            return "";
        }

        String orderBy = "";
        switch (filterOptions.getSortBy().get()) {
            case "amount":
                orderBy = "amount";
                break;
            case "date":
                orderBy = "date";
                break;
            default:
                return "";
        }

        orderBy = String.format(" order by %s", orderBy);

        if (filterOptions.getSortOrder().isPresent() && filterOptions.getSortOrder().get().equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }

        return orderBy;
    }
}
