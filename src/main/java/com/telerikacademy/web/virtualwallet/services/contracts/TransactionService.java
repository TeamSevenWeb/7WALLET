package com.telerikacademy.web.virtualwallet.services.contracts;

import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.TransactionToJoinDto;
import com.telerikacademy.web.virtualwallet.models.wallets.JoinWallet;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;

public interface TransactionService {

    Transaction getById(int id, User user);

    void create(Transaction outgoing, Transaction ingoing);

    Transaction getTransaction(TransactionToJoinDto transactionDto, User user, JoinWallet joinWalletOutgoing, Wallet walletIngoing);
}
