package com.telerikacademy.web.virtualwallet.utils;

import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.TransactionDto;
import com.telerikacademy.web.virtualwallet.models.dtos.TransactionToJoinDto;
import com.telerikacademy.web.virtualwallet.models.wallets.JoinWallet;
import com.telerikacademy.web.virtualwallet.repositories.contracts.TransactionRepository;
import com.telerikacademy.web.virtualwallet.repositories.contracts.UserRepository;
import com.telerikacademy.web.virtualwallet.repositories.contracts.WalletRepository;
import com.telerikacademy.web.virtualwallet.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TransactionMapper {

    private final UserService userService;


    @Autowired
    public TransactionMapper(UserService userService) {
        this.userService = userService;
    }


    public Transaction fromDto(User sender, TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        User receiver = userService.searchByAnyMatch(transactionDto.getReceiver());
        transaction.setReceiver(receiver);
        transaction.setSender(sender);
        transaction.setAmount(transactionDto.getAmount());
        transaction.setDate(LocalDateTime.now());
        transaction.setExpirationDate(LocalDateTime.now().plusDays(5));

        return transaction;
    }

    public Transaction fromDtoToJoin(User sender, TransactionToJoinDto transactionDto) {
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(sender);
        transaction.setAmount(transactionDto.getAmount());
        transaction.setDate(LocalDateTime.now());
        transaction.setExpirationDate(LocalDateTime.now().plusDays(5));

        return transaction;
    }
}
