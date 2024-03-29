package com.telerikacademy.web.virtualwallet.services;


import com.mailjet.client.errors.MailjetException;
import com.telerikacademy.web.virtualwallet.exceptions.AuthorizationException;
import com.telerikacademy.web.virtualwallet.exceptions.FundsSupplyException;
import com.telerikacademy.web.virtualwallet.exceptions.TransactionConfirmationException;
import com.telerikacademy.web.virtualwallet.exceptions.TransactionExpiredException;
import com.telerikacademy.web.virtualwallet.filters.TransactionFilterOptions;
import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.TransactionToJoinDto;
import com.telerikacademy.web.virtualwallet.models.wallets.JoinWallet;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;
import com.telerikacademy.web.virtualwallet.repositories.contracts.TransactionRepository;
import com.telerikacademy.web.virtualwallet.services.contracts.TransactionService;
import com.telerikacademy.web.virtualwallet.services.contracts.VerificationService;
import com.telerikacademy.web.virtualwallet.services.contracts.WalletService;
import com.telerikacademy.web.virtualwallet.utils.TransactionMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final WalletService walletService;

    private final TransactionRepository repository;

    private final TransactionMapper transactionMapper;
    private final VerificationService verificationService;

    private static final String VIEW_TRANSACTION_PERMISSION_ERROR = "You are not authorized to view this transaction.";

    @Autowired
    public TransactionServiceImpl(WalletService walletService, TransactionRepository repository, TransactionMapper transactionMapper, VerificationService verificationService) {
        this.walletService = walletService;
        this.repository = repository;
        this.transactionMapper = transactionMapper;
        this.verificationService = verificationService;
    }

    @Override
    public Transaction getById(int id, User user) {
            Transaction transaction = repository.getById(id);
            checkModifyPermissions(transaction,user);
            return transaction;
    }

    @Override
    public void create(Transaction transaction, Wallet sending, Wallet receiving) {

        if (sending.getHoldings() < transaction.getAmount()) {
            throw new FundsSupplyException();
        }
        if (sending.equals(receiving)) {
            throw new AuthorizationException("Please enter different receiver");
        }
        repository.create(transaction);
        try {
            if (transaction.getAmount() >= 1000) {
                transaction.setConfirmed(false);
                verificationService.sendTransactionCode(transaction.getSender(),transaction,sending,receiving);
            }
            else transaction.setConfirmed(true);
        } catch (MailjetException e) {
            throw new RuntimeException(e);
        }
        processTransaction(transaction, sending, receiving);

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

    @Override
    public void processTransaction(Transaction transaction, Wallet sending, Wallet receiving) {
        if (!transaction.isConfirmed()) {
            throw new TransactionConfirmationException("Please check your email to confirm this transaction.");
        }
        if (transaction.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new TransactionExpiredException("Your transaction has expired.");
        }
        walletService.subtractFunds(sending.getId(), transaction.getAmount());
        walletService.addFunds(receiving.getId(), transaction.getAmount());

    }

    private void checkModifyPermissions(Transaction transaction, User user) {
        if (!transaction.getSender().equals(user) & !transaction.getReceiver().equals(user)) {
            throw new AuthorizationException(VIEW_TRANSACTION_PERMISSION_ERROR);
        }
    }
}
