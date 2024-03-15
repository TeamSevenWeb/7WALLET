package com.telerikacademy.web.virtualwallet.services.contracts;

import com.telerikacademy.web.virtualwallet.filters.TransactionFilterOptions;
import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.TransactionToJoinDto;
import com.telerikacademy.web.virtualwallet.models.wallets.JoinWallet;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionService {

    Transaction getById(int id, User user);

    void create(Transaction transaction,Wallet receiving, Wallet sending);

    Transaction getTransaction(TransactionToJoinDto transactionDto, User user, JoinWallet joinWalletOutgoing, Wallet walletIngoing);

    Page<Transaction> getAll(User user, TransactionFilterOptions transactionFilterOptions, Pageable pageable);
}
