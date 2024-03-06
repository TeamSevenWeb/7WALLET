package com.telerikacademy.web.virtualwallet.services;


import com.telerikacademy.web.virtualwallet.exceptions.FundsSupplyException;
import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;
import com.telerikacademy.web.virtualwallet.repositories.contracts.TransactionRepository;
import com.telerikacademy.web.virtualwallet.services.contracts.TransactionService;
import com.telerikacademy.web.virtualwallet.services.contracts.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final WalletService walletService;

    private final TransactionRepository repository;

    @Autowired
    public TransactionServiceImpl(WalletService walletService, TransactionRepository repository) {
        this.walletService = walletService;
        this.repository = repository;
    }

    @Override
    public Transaction getById(int id) {
        return repository.getById(id);
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
}
