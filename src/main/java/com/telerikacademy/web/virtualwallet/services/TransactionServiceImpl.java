package com.telerikacademy.web.virtualwallet.services;


import com.telerikacademy.web.virtualwallet.exceptions.AuthorizationException;
import com.telerikacademy.web.virtualwallet.exceptions.FundsSupplyException;
import com.telerikacademy.web.virtualwallet.filters.TransactionFilterOptions;
import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.TransactionToJoinDto;
import com.telerikacademy.web.virtualwallet.models.wallets.JoinWallet;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;
import com.telerikacademy.web.virtualwallet.repositories.contracts.TransactionRepository;
import com.telerikacademy.web.virtualwallet.services.contracts.TransactionService;
import com.telerikacademy.web.virtualwallet.services.contracts.WalletService;
import com.telerikacademy.web.virtualwallet.utils.TransactionMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final WalletService walletService;

    private final TransactionRepository repository;

    private final TransactionMapper transactionMapper;

    private static final String VIEW_TRANSACTION_PERMISSION_ERROR = "You are not authorized to view this transaction.";

    @Autowired
    public TransactionServiceImpl(WalletService walletService, TransactionRepository repository, TransactionMapper transactionMapper) {
        this.walletService = walletService;
        this.repository = repository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public Transaction getById(int id, User user) {
            Transaction transaction = repository.getById(id);
            checkModifyPermissions(transaction,user);
            return transaction;
    }

    @Override
    public void create(Transaction transaction,Wallet sending, Wallet receiving) {
        if(sending.getHoldings()<transaction.getAmount()){
            throw new FundsSupplyException();
        }
        if(sending.equals(receiving)){
            throw new AuthorizationException("Please enter different receiver");
        }
        repository.create(transaction);
        walletService.subtractFunds(sending.getId(),transaction.getAmount());
        walletService.addFunds(receiving.getId(),transaction.getAmount());
    }

    public Transaction getTransaction(TransactionToJoinDto transactionDto, User user
            ,JoinWallet joinWalletOutgoing, Wallet walletIngoing) {
        Transaction transaction = transactionMapper.fromDtoToJoin(user,transactionDto);
        create(transaction, joinWalletOutgoing, walletIngoing);
        return transaction;
    }
    @Override
    public Page<Transaction> getAll(User user, TransactionFilterOptions transactionFilterOptions,Pageable pageable) {
        return repository.filterAndSort(user ,transactionFilterOptions,pageable);
    }
    private void checkModifyPermissions(Transaction transaction, User user) {
        if (!transaction.getSender().equals(user) & !transaction.getReceiver().equals(user)) {
            throw new AuthorizationException(VIEW_TRANSACTION_PERMISSION_ERROR);
        }
    }
}
