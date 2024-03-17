package com.telerikacademy.web.virtualwallet.services;

import com.telerikacademy.web.virtualwallet.exceptions.FundsSupplyException;
import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;
import com.telerikacademy.web.virtualwallet.repositories.contracts.TransactionRepository;
import com.telerikacademy.web.virtualwallet.services.contracts.VerificationService;
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
    VerificationService verificationService;

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    WalletServiceImpl mockWalletService;

    @Test
    void getById_Should_ReturnTransaction_When_MatchByIdExists(){
        //Arrange
        Transaction mockTransaction = createMockTransaction();

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
        Transaction mockTransaction1 = createMockTransaction();

        //Assert
        Assertions.assertThrows(FundsSupplyException.class, ()->mockTransactionService.create(mockTransaction1,
                mockTransaction1.getSender().getWallet(),mockTransaction1.getReceiver().getWallet()));

    }

    @Test
    void create_Should_CallWalletService_When_WalletAmountIsEnoughForTheTransaction(){
        //Arrange
        Transaction mockTransaction1 = createMockTransaction();
        Wallet senderWallet = mockTransaction1.getSender().getWallet();
        Wallet receiverWallet = mockTransaction1.getReceiver().getWallet();

        //Act
        senderWallet.setHoldings(2000);
        mockTransactionService.create(mockTransaction1,senderWallet,receiverWallet);

        //Assert
        Mockito.verify(mockWalletService,Mockito.times(1))
                .subtractFunds(senderWallet.getId(),mockTransaction1.getAmount());

        Mockito.verify(mockWalletService,Mockito.times(1))
                .addFunds(receiverWallet.getId(),mockTransaction1.getAmount());
    }

    @Test
    void create_Should_CallRepository_When_WalletAmountIsEnoughForTheTransaction(){
        //Arrange
        Transaction mockTransaction1 = createMockTransaction();
        Wallet senderWallet = mockTransaction1.getSender().getWallet();
        Wallet receiverWallet = mockTransaction1.getReceiver().getWallet();

        //Act
        senderWallet.setHoldings(1000);
        mockTransactionService.create(mockTransaction1,senderWallet,receiverWallet);

        //Assert
        Mockito.verify(transactionRepository,Mockito.times(1))
                .create(mockTransaction1);
    }
}