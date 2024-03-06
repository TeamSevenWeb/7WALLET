package com.telerikacademy.web.virtualwallet.services;


import com.telerikacademy.web.virtualwallet.exceptions.AuthenticationException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtualwallet.exceptions.FundsSupplyException;
import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;
import com.telerikacademy.web.virtualwallet.repositories.contracts.TransactionRepository;
import com.telerikacademy.web.virtualwallet.services.contracts.TransactionService;
import com.telerikacademy.web.virtualwallet.services.contracts.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.awt.*;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final WalletService walletService;

    private final TransactionRepository repository;

    private static final String VIEW_TRANSACTION_PERMISSION_ERROR = "You are not authorized to view this transaction.";

    @Autowired
    public TransactionServiceImpl(WalletService walletService, TransactionRepository repository) {
        this.walletService = walletService;
        this.repository = repository;
    }

    @Override
    public Transaction getById(int id, User user) {
            Transaction transaction = repository.getById(id);
            checkModifyPermissions(transaction,user);
            return transaction;
    }

    @Override
    public void create(Transaction outgoing,Transaction ingoing, User sender) {
        Wallet senderWallet = sender.getWallet();
        Wallet receiverWallet = outgoing.getReceiver().getWallet();
        if(senderWallet.getHoldings()<outgoing.getAmount()){
            throw new FundsSupplyException();
        }
        repository.createMultiple(outgoing,ingoing);
        walletService.subtractFunds(senderWallet.getId(),outgoing.getAmount());
        walletService.addFunds(receiverWallet.getId(),outgoing.getAmount());
    }

    private void checkModifyPermissions(Transaction transaction, User user) {
        if (!transaction.getSender().equals(user) & !transaction.getReceiver().equals(user)) {
            throw new AuthenticationException(VIEW_TRANSACTION_PERMISSION_ERROR);
        }
    }
}
