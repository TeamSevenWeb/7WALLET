package com.telerikacademy.web.virtualwallet.services;

import com.telerikacademy.web.virtualwallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtualwallet.exceptions.FundsSupplyException;
import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;
import com.telerikacademy.web.virtualwallet.repositories.contracts.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.telerikacademy.web.virtualwallet.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTests {
    @InjectMocks
    TransactionServiceImpl mockTransactionService;

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    WalletServiceImpl mockWalletService;

    @Test
    void getById_Should_ReturnTransaction_When_MatchByIdExists(){
        //Arrange
        Transaction mockTransaction = createMockTransactionOutgoing();

        Mockito.when(transactionRepository.getById(Mockito.anyInt()))
                .thenReturn(mockTransaction);
        //Act
        Transaction result = mockTransactionService.getById(mockTransaction.getId(),mockTransaction.getSender());

        //Assert
        Assertions.assertEquals(mockTransaction, result);
    }

    @Test
    void create_Should_throw_When_WalletAmountIsLessThanTransactionAmount(){
        //Arrange
        Transaction mockTransaction1 = createMockTransactionOutgoing();
        Transaction mockTransaction2 = createMockTransactionIngoing();

        //Assert
        Assertions.assertThrows(FundsSupplyException.class, ()->mockTransactionService.create(mockTransaction1,
                mockTransaction2));

    }

    @Test
    void create_Should_CallWalletService_When_WalletAmountIsEnoughForTheTransaction(){
        //Arrange
        Transaction mockTransaction1 = createMockTransactionOutgoing();
        Wallet senderWallet = mockTransaction1.getSender().getWallet();
        Wallet receiverWallet = mockTransaction1.getReceiver().getWallet();
        Transaction mockTransaction2 = createMockTransactionIngoing();

        //Act
        senderWallet.setHoldings(2000);
        mockTransactionService.create(mockTransaction1,mockTransaction2);

        //Assert
        Mockito.verify(mockWalletService,Mockito.times(1))
                .subtractFunds(senderWallet.getId(),mockTransaction1.getAmount());

        Mockito.verify(mockWalletService,Mockito.times(1))
                .addFunds(receiverWallet.getId(),mockTransaction1.getAmount());
    }

    @Test
    void create_Should_CallRepository_When_WalletAmountIsEnoughForTheTransaction(){
        //Arrange
        Transaction mockTransaction1 = createMockTransactionOutgoing();
        Wallet senderWallet = mockTransaction1.getSender().getWallet();
        Transaction mockTransaction2 = createMockTransactionIngoing();

        //Act
        senderWallet.setHoldings(2000);
        mockTransactionService.create(mockTransaction1,mockTransaction2);

        //Assert
        Mockito.verify(transactionRepository,Mockito.times(1))
                .createMultiple(mockTransaction1, mockTransaction2);
    }
}