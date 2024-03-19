package com.telerikacademy.web.virtualwallet.services;

import com.telerikacademy.web.virtualwallet.models.TransactionVerificationCodes;
import com.telerikacademy.web.virtualwallet.models.VerificationCodes;
import com.telerikacademy.web.virtualwallet.repositories.contracts.TransactionRepository;
import com.telerikacademy.web.virtualwallet.repositories.contracts.TransactionVerificationCodesRepository;
import com.telerikacademy.web.virtualwallet.repositories.contracts.VerificationCodesRepository;
import com.telerikacademy.web.virtualwallet.services.contracts.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.telerikacademy.web.virtualwallet.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class VerificationServiceImplTests {

    @InjectMocks
    VerificationServiceImpl mockVerificationServiceImpl;

    @Mock
    TransactionVerificationCodesRepository mockTransactionVerificationCodesRepository;

    @Mock
    VerificationCodesRepository mockVerificationCodesRepository;

    @Mock
    UserService mockUserService;

    @Mock
    TransactionRepository mockTransactionRepository;


    @Test
    void getByCode_Should_ReturnTransactionVerificationCodes_When_MatchByCodeExists() {
        //Arrange
        TransactionVerificationCodes transactionVerificationCodes = createMockTransactionVerificationCodes();

        Mockito.when(mockTransactionVerificationCodesRepository.getByField("verificationCode", transactionVerificationCodes.getVerificationCode()))
                .thenReturn(transactionVerificationCodes);
        //Act
        TransactionVerificationCodes result = mockVerificationServiceImpl
                        .getByCode(transactionVerificationCodes.getVerificationCode());

        //Assert
        Assertions.assertEquals(transactionVerificationCodes, result);
    }

    @Test
    void verifyUser_Should_Call_UserService_If_ValidCode(){
        //Arrange
        VerificationCodes verificationCodes = createMockVerificationCodes();
        Mockito.when(mockVerificationCodesRepository.getByField("verificationCode",verificationCodes.getVerificationCode()))
                .thenReturn(verificationCodes);

        //Act
        mockVerificationServiceImpl.verifyUser(verificationCodes.getVerificationCode());

        //Assert
        Mockito.verify(mockUserService,Mockito.times(1)).makeRegular(verificationCodes.getUser());

    }

    @Test
    void verifyUser_Should_Call_VerificationCodesRepository_If_ValidCode(){
        //Arrange
        VerificationCodes verificationCodes = createMockVerificationCodes();
        Mockito.when(mockVerificationCodesRepository.getByField("verificationCode",verificationCodes.getVerificationCode()))
                .thenReturn(verificationCodes);
        //Act
        mockVerificationServiceImpl.verifyUser(verificationCodes.getVerificationCode());

        //Assert
        Mockito.verify(mockVerificationCodesRepository,Mockito.times(1)).delete(verificationCodes.getVerificationCodeId());

    }

    @Test
    void verifyTransaction_Should_Call_TransactionRepository_If_ValidCode(){
        //Arrange
        TransactionVerificationCodes transactionVerificationCodes = createMockTransactionVerificationCodes();

        Mockito.when(mockTransactionVerificationCodesRepository.getByField("verificationCode", transactionVerificationCodes.getVerificationCode()))
                .thenReturn(transactionVerificationCodes);
        //Act
        mockVerificationServiceImpl.verifyTransaction(transactionVerificationCodes.getVerificationCode());

        //Assert
        Mockito.verify(mockTransactionRepository,Mockito.times(1)).update(transactionVerificationCodes.getTransaction());

    }

    @Test
    void verifyTransaction_Should_Call_TransactionVerificationCodesRepository_If_ValidCode(){
        //Arrange
        TransactionVerificationCodes transactionVerificationCodes = createMockTransactionVerificationCodes();

        Mockito.when(mockTransactionVerificationCodesRepository.getByField("verificationCode", transactionVerificationCodes.getVerificationCode()))
                .thenReturn(transactionVerificationCodes);
        //Act
        mockVerificationServiceImpl.verifyTransaction(transactionVerificationCodes.getVerificationCode());

        //Assert
        Mockito.verify(mockTransactionVerificationCodesRepository,Mockito.times(1)).delete(transactionVerificationCodes.getTransactionVerificationCodeId());

    }
}
