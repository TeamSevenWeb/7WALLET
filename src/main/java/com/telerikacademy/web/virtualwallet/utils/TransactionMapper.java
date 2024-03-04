package com.telerikacademy.web.virtualwallet.utils;

import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.TransactionDto;
import com.telerikacademy.web.virtualwallet.repositories.contracts.TransactionRepository;
import com.telerikacademy.web.virtualwallet.repositories.contracts.UserRepository;
import com.telerikacademy.web.virtualwallet.repositories.contracts.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TransactionMapper {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;



    @Autowired
    public TransactionMapper(UserRepository userRepository, WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
    }


    public Transaction outgoingFromDto(User sender, TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        User receiver = userRepository.getByField("username",transactionDto.getReceiver());
        transaction.setReceiver(receiver);
        transaction.setSender(sender);
        transaction.setAmount(transactionDto.getAmount());
        transaction.setWallet(sender.getWallet());
        transaction.setDirection(2);
        sender.getSentTransactions().add(transaction);
        transaction.setDate(LocalDateTime.now());

        return transaction;
    }

    public Transaction ingoingFromDto(User sender, TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        User receiver = userRepository.getByField("username",transactionDto.getReceiver());
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(transactionDto.getAmount());
        transaction.setWallet(sender.getWallet());
        transaction.setDirection(1);
        transaction.setDate(LocalDateTime.now());
        receiver.getReceivedTransactions().add(transaction);

        return transaction;
    }
}
