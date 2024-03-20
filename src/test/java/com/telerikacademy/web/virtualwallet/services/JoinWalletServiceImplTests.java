package com.telerikacademy.web.virtualwallet.services;

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
        JoinWallet joinWallet = createJoinWallet();

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
        JoinWallet joinWallet = createJoinWallet();

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


}
