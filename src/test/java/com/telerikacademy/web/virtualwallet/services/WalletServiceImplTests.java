package com.telerikacademy.web.virtualwallet.services;

import com.telerikacademy.web.virtualwallet.exceptions.AuthorizationException;
import com.telerikacademy.web.virtualwallet.exceptions.FundsSupplyException;
import com.telerikacademy.web.virtualwallet.models.Currency;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;
import com.telerikacademy.web.virtualwallet.repositories.contracts.WalletRepository;
import com.telerikacademy.web.virtualwallet.services.contracts.CurrencyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.telerikacademy.web.virtualwallet.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class WalletServiceImplTests {

    @InjectMocks
    WalletServiceImpl mockWalletService;

    @Mock
    WalletRepository walletRepository;

    @Mock
    CurrencyService currencyService;

    @Test
    void getById_Should_ReturnWallet_When_UserIsOwner(){
        //Arrange
        Wallet wallet = createMockWallet();

        User user = createMockUser();

        user.setWallet(wallet);

        Mockito.when(walletRepository.getById(Mockito.anyInt()))
                .thenReturn(wallet);

        //Act
        Wallet result = mockWalletService.get(wallet.getId(),user);

        //Assert
        Assertions.assertEquals(wallet,result);
    }

    @Test
    void getById_Should_Throw_When_UserIsNotOwner(){
        Wallet wallet = createMockWallet();

        User user2 = createMockUser();

        //Act,Assert
        Assertions.assertThrows(AuthorizationException.class, ()-> mockWalletService.get(wallet.getId(),user2));
    }

    @Test
    void getByUser_Should_CallRepository(){
        //Arrange
        User user = createMockUser();

        Wallet wallet = createMockWallet();

        user.setWallet(wallet);

        wallet.setHolder(user);

        //Act
        mockWalletService.getByUser(user);

        //Assert
        Mockito.verify(walletRepository,Mockito.times(1))
                .getByUser(user);
    }

    @Test
    void getAll_Should_CallRepository(){
        //Arrange,Act
        mockWalletService.getAll();

        //Assert
        Mockito.verify(walletRepository,Mockito.times(1))
                .getAll();
    }

    @Test
    void create_Should_CallRepository(){
        //Arrange
        Wallet wallet = createMockWallet();

        //Act
        mockWalletService.create(wallet);

        //Assert
        Mockito.verify(walletRepository,Mockito.times(1))
                .create(wallet);
    }

    @Test
    void createDefaultWallet_Should_CreateWalletWithHolderGivenUser(){
        //Arrange
        User user = createMockUser();

        Mockito.when(currencyService.getById(Mockito.anyInt())).thenReturn(createMockCurrency());

        //Act
       Wallet wallet =  mockWalletService.createDefaultWallet(user);

        //Assert
        Assertions.assertEquals(wallet.getHolder(),user);
    }

    @Test
    void update_Should_CallRepository(){
        //Arrange
        Wallet wallet = createMockWallet();

        //Act
        mockWalletService.update(wallet);

        //Assert
        Mockito.verify(walletRepository,Mockito.times(1))
                .update(wallet);
    }

    @Test
    void addFunds_Should_AddFundsToGivenWallet(){
        //Arrange
        Wallet wallet = createMockWallet();

        Mockito.when(walletRepository.getById(Mockito.anyInt())).thenReturn(wallet);

        //Act
        mockWalletService.addFunds(wallet.getId(),1000);

        //Assert
        Assertions.assertEquals(wallet.getHoldings(),1001);
    }

    @Test
    void addFunds_Should_AddRemoveFundsFromGivenWallet_When_HoldingsAreEnough(){
        //Arrange
        Wallet wallet = createMockWallet();

        wallet.setHoldings(1001);

        Mockito.when(walletRepository.getById(Mockito.anyInt())).thenReturn(wallet);

        //Act
        mockWalletService.subtractFunds(wallet.getId(),1000);

        //Assert
        Assertions.assertEquals(wallet.getHoldings(),1);
    }

    @Test
    void addFunds_Should_Throw_When_HoldingsAreNotEnough(){
        //Arrange
        Wallet wallet = createMockWallet();


        Mockito.when(walletRepository.getById(Mockito.anyInt())).thenReturn(wallet);

        //Act,Assert
        Assertions.assertThrows(FundsSupplyException.class
                ,()-> mockWalletService.subtractFunds(wallet.getId(),100));
    }

    @Test
    void changeCurrency_Should_ChangeCurrency(){
        //Arrange
        Wallet wallet = createMockWallet();

        Mockito.when(currencyService.getById(Mockito.anyInt())).thenReturn(createMockCurrency());

        Currency currency = createMockCurrency();

        currency.setCurrencyCode("USD");

        Mockito.when(walletRepository.getById(Mockito.anyInt())).thenReturn(wallet);

        //Act
        mockWalletService.changeCurrency(wallet.getId(),currency.getId());

        //Assert
        Assertions.assertEquals(wallet.getCurrency(),currency);
    }
}
