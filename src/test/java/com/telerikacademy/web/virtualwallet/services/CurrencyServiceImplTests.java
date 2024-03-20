package com.telerikacademy.web.virtualwallet.services;

import com.telerikacademy.web.virtualwallet.exceptions.AuthorizationException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityDuplicateException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtualwallet.models.Currency;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.repositories.contracts.CurrencyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.telerikacademy.web.virtualwallet.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceImplTests {

    @InjectMocks
    CurrencyServiceImpl mockCurrencyService;

    @Mock
    CurrencyRepository currencyRepository;

    @Test
    void  getById_Should_ReturnCurrency_When_MatchByIdExists(){
        //Arrange
        Currency mockCurrency = createMockCurrency();

        Mockito.when(currencyRepository.getById(Mockito.anyInt()))
                .thenReturn(mockCurrency);

        //Act
        Currency result = mockCurrencyService.getById(mockCurrency.getId());

        //Assert
        Assertions.assertEquals(mockCurrency,result);
    }

    @Test
    void create_Should_Throw_When_CurrencyWithSameCodeExists(){
        //Arrange
        Currency currency = createMockCurrency();

        User user = createMockUser();

        Mockito.when(currencyRepository.getByField("currencyCode",currency.getCurrencyCode())).thenReturn(currency);

        //Act,Assert
        Assertions.assertThrows(EntityDuplicateException.class, ()-> mockCurrencyService.create(currency, user));
    }

    @Test
    void create_Should_Throw_When_UserIsNotAdmin(){
        //Arrange
        Currency currency = createMockCurrency();

        User user = createMockUser2();

        Mockito.when(currencyRepository.getByField("currencyCode",currency.getCurrencyCode()))
                .thenThrow(EntityNotFoundException.class);

        //Act,Assert
        Assertions.assertThrows(AuthorizationException.class, ()-> mockCurrencyService.create(currency, user));
    }

    @Test
    void create_Should_CallRepository_When_CurrencyWithSameCodeDoesNotExistAndUserIsAdmin(){
        //Arrange
        User user = createMockUser();

        Currency currency = createMockCurrency();

        Mockito.when(currencyRepository.getByField("currencyCode",currency.getCurrencyCode()))
                .thenThrow(EntityNotFoundException.class);

        //Act
        mockCurrencyService.create(currency,user);

        //Assert
        Mockito.verify(currencyRepository,Mockito.times(1))
                .create(currency);
    }

    @Test
    void update_Should_Throw_When_CurrencyWithSameCodeExists(){
        //Arrange
        Currency currency = createMockCurrency();

        Currency currency2 = createMockCurrency();

        currency2.setId(2);

        User user = createMockUser();

        Mockito.when(currencyRepository.getByField("currencyCode",currency.getCurrencyCode())).thenReturn(currency2);

        //Act,Assert
        Assertions.assertThrows(EntityDuplicateException.class, ()-> mockCurrencyService.update(currency, user));
    }

    @Test
    void update_Should_Throw_When_UserIsNotAdmin(){
        //Arrange
        Currency currency = createMockCurrency();

        User user = createMockUser2();

        Mockito.when(currencyRepository.getByField("currencyCode",currency.getCurrencyCode()))
                .thenThrow(EntityNotFoundException.class);

        //Act,Assert
        Assertions.assertThrows(AuthorizationException.class, ()-> mockCurrencyService.update(currency, user));
    }

    @Test
    void update_Should_CallRepository_When_CurrencyWithSameCodeDoesNotExistAndUserIsAdmin(){
        //Arrange
        User user = createMockUser();

        Currency currency = createMockCurrency();

        Mockito.when(currencyRepository.getByField("currencyCode",currency.getCurrencyCode()))
                .thenReturn(currency);

        //Act
        mockCurrencyService.update(currency,user);

        //Assert
        Mockito.verify(currencyRepository,Mockito.times(1))
                .update(currency);
    }

    @Test
    void delete_Should_Throw_When_UserIsNotAdmin(){
        //Arrange
        User user = createMockUser2();

        //Act,Assert
        Assertions.assertThrows(AuthorizationException.class, ()-> mockCurrencyService.delete(1, user));
    }

    @Test
    void delete_Should_CallRepository_When_UserIsAdmin(){
        //Arrange
        User user = createMockUser();

        //Act
        mockCurrencyService.delete(1,user);

        //Assert
        Mockito.verify(currencyRepository,Mockito.times(1))
                .delete(1);
    }
}
