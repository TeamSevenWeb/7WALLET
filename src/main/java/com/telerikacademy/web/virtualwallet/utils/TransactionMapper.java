package com.telerikacademy.web.virtualwallet.utils;

import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.TransactionDto;
import com.telerikacademy.web.virtualwallet.repositories.contracts.TransactionRepository;
import com.telerikacademy.web.virtualwallet.repositories.contracts.UserRepository;
import com.telerikacademy.web.virtualwallet.repositories.contracts.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;



    @Autowired
    public TransactionMapper(TransactionRepository transactionRepository, UserRepository userRepository, WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
    }


    public Transaction fromDto(TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        User receiver = userRepository.getByField("username",transactionDto.getReceiver());
        transaction.setReceiver(receiver);
        transaction.setAmount(transactionDto.getAmount());
        transaction.setWallet(walletRepository.getById(transactionDto.getWallet()));
        receiver.getTransactions().add(transaction);

        return transaction;
    }
}
