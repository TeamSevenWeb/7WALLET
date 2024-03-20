package com.telerikacademy.web.virtualwallet.services;

import com.telerikacademy.web.virtualwallet.exceptions.EntityDuplicateException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtualwallet.models.Currency;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.wallets.JoinWallet;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;
import com.telerikacademy.web.virtualwallet.repositories.contracts.JoinWalletRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.telerikacademy.web.virtualwallet.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class JoinWalletServiceImplTests {

    @InjectMocks
    JoinWalletServiceImpl mockJoinWalletService;

    @Mock
    JoinWalletRepository joinWalletRepository;

    @Test
    void get_Should_ReturnWallet_When_UserIsOwner(){
        //Arrange
        JoinWallet joinWallet = createMockJoinWallet();

        User user = createMockUser();

        joinWallet.setHolder(user);

        Mockito.when(joinWalletRepository.getById(Mockito.anyInt()))
                .thenReturn(joinWallet);

        //Act
        Wallet result = mockJoinWalletService.get(joinWallet.getId(),user);

        //Assert
        Assertions.assertEquals(joinWallet,result);
    }

    @Test
    void get_Should_ReturnWallet_When_UserUseWallet(){
        //Arrange
        JoinWallet joinWallet = createMockJoinWallet();

        User user = createMockUser2();

        Mockito.when(joinWalletRepository.getById(Mockito.anyInt()))
                .thenReturn(joinWallet);

        //Act
        Wallet result = mockJoinWalletService.get(joinWallet.getId(),user);

        //Assert
        Assertions.assertEquals(joinWallet,result);
    }

    @Test
    void getAll_Should_CallRepository(){
        //Arrange,Act
        mockJoinWalletService.getAll();

        //Assert
        Mockito.verify(joinWalletRepository,Mockito.times(1))
                .getAll();
    }

    @Test
    void getAllByUser_Should_ReturnAllJoinWalletsThatUserOwnAndParticipate(){
        //Arrange
        User user = createMockUser();

        JoinWallet wallet = createMockJoinWallet();

        wallet.setHolder(user);

        Set<JoinWallet> wallets = new HashSet<>();

        wallets.add(createMockJoinWallet());

        user.setJoinWallets(wallets);

        Mockito.when(joinWalletRepository.getAllByUser(user)).thenReturn(new ArrayList<>());

        //Act,Assert
        Assertions.assertEquals(mockJoinWalletService.getAllByUser(user).size(),1);

    }

    @Test
    void create_Should_Throw_When_UserOwnWalletWithSameName(){
        //Assert
        User user = createMockUser();

        JoinWallet wallet = createMockJoinWallet();

        Mockito.when(joinWalletRepository.getByUserAndName(user,wallet.getName()))
                .thenReturn(wallet);
        //Act
        Assertions.assertThrows(EntityDuplicateException.class,
                ()-> mockJoinWalletService.create(wallet,user));
    }

    @Test
    void create_Should_CallRepository_When_UserDoesntHoldWalletWithSameName(){
        //Arrange
        User user = createMockUser();

        JoinWallet wallet = createMockJoinWallet();

        Mockito.when(joinWalletRepository.getByUserAndName(user,wallet.getName()))
                .thenThrow(EntityNotFoundException.class);

        //Act
        mockJoinWalletService.create(wallet,user);

        //Assert
        Mockito.verify(joinWalletRepository,Mockito.times(1))
                .create(wallet);
    }

    @Test
    void update_Should_Throw_When_UserOwnWalletWithSameName(){
        //Assert
        User user = createMockUser();

        JoinWallet wallet = createMockJoinWallet();

        JoinWallet wallet2 = createMockJoinWallet();

        wallet2.setId(2);

        Mockito.when(joinWalletRepository.getByUserAndName(user,wallet.getName()))
                .thenReturn(wallet2);
        //Act
        Assertions.assertThrows(EntityDuplicateException.class,
                ()-> mockJoinWalletService.create(wallet,user));
    }

    @Test
    void update_Should_CallRepository_When_UserDoesntHoldWalletWithSameName(){
        //Arrange
        User user = createMockUser();

        JoinWallet wallet = createMockJoinWallet();

        Mockito.when(joinWalletRepository.getByUserAndName(user,wallet.getName()))
                .thenThrow(EntityNotFoundException.class);

        //Act
        mockJoinWalletService.update(wallet,user,wallet.getId());

        //Assert
        Mockito.verify(joinWalletRepository,Mockito.times(1))
                .update(wallet);
    }

    @Test
    void delete_Should_CallRepository(){
        //Arrange
        User user = createMockUser();

        //Act
        mockJoinWalletService.delete(1);

        //Assert
        Mockito.verify(joinWalletRepository,Mockito.times(1))
                .delete(1);
    }

    @Test
    void getAllUsers_Should_ReturnNumberOfAllUserConnectedWithWallet(){
        //Arrange
        JoinWallet joinWallet = createMockJoinWallet();

        Set<User> users = new HashSet<>();

        users.add(createMockUser2());

        joinWallet.setUsers(users);

        Mockito.when(joinWalletRepository.getById(Mockito.anyInt())).thenReturn(joinWallet);

        //Act
       int walletUsers = mockJoinWalletService.getAllUsers(joinWallet.getId()).size();

       //Assert
        Assertions.assertEquals(walletUsers,2);
    }
}
