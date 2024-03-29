package com.telerikacademy.web.virtualwallet.repositories;

import com.telerikacademy.web.virtualwallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtualwallet.exceptions.TransactionsNotFoundException;
import com.telerikacademy.web.virtualwallet.filters.TransactionFilterOptions;
import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.repositories.contracts.TransactionRepository;
import com.telerikacademy.web.virtualwallet.repositories.contracts.UserRepository;
import com.telerikacademy.web.virtualwallet.services.contracts.UserService;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
public class TransactionRepositoryImpl extends AbstractCRUDRepository<Transaction> implements TransactionRepository {

    private final UserRepository userRepository;

    private final UserService userService;
    @Autowired
    public TransactionRepositoryImpl(SessionFactory sessionFactory, UserRepository userRepository, UserService userService) {
        super(Transaction.class,sessionFactory);
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public Page<Transaction> filterAndSort(User user, TransactionFilterOptions filterOptions, Pageable pageable){
        try (Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();
            StringBuilder queryString = new StringBuilder("from Transaction ");

            if(!userService.isAdmin(user)) {
                filters.add("(sender = :user OR receiver = :user)");
                params.put("user", user);
            }

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

            filterOptions.getSender().ifPresent(value -> {
                if(value.isEmpty()||user.getUsername().equals(value)){
                    return;
                }
                try {
                    User sender = userRepository.getByField("username",value);
                    filters.add("sender = :sender");
                    params.put("sender", sender);
                } catch (EntityNotFoundException e){
                    throw new EntityNotFoundException("Transactions","receiver",value);
                }
            });


            filterOptions.getDate().ifPresent(value -> {
                if(value.isEmpty()){
                    return;
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(value, formatter);
                LocalDateTime localDateTime = LocalDateTime.of(date, LocalTime.MIDNIGHT);

                filters.add("date = :date");
                params.put("date", localDateTime);
            });


            filterOptions.getDirection().ifPresent(value -> {
                if(value.isEmpty()){
                    return;
                }
                if(value.equals("INGOING")){
                    filters.add("receiver = :receiver");
                    params.put("receiver", user);
                }
                else {
                    filters.add("sender = :sender");
                    params.put("sender", user);
                }
            });

            queryString
                    .append(" where ")
                    .append(String.join(" and ", filters));


            queryString.append(generateOrderBy(filterOptions));

            Query<Transaction> query = session.createQuery(queryString.toString(), Transaction.class);
            query.setProperties(params);

            List<Transaction> result = query.list();
            if (result.isEmpty()){
                throw new TransactionsNotFoundException("Transactions");
            }
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), result.size());

            return new PageImpl<>(result.subList(start, end), pageable, result.size());
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
