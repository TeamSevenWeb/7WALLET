package com.telerikacademy.web.virtualwallet.services;

import com.telerikacademy.web.virtualwallet.exceptions.AuthorizationException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityDuplicateException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtualwallet.exceptions.FundsSupplyException;
import com.telerikacademy.web.virtualwallet.models.Card;
import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.repositories.contracts.CardRepository;
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
public class CardServiceImplTests {

    @InjectMocks
    CardServiceImpl mockCardService;

    @Mock
    CardRepository cardRepository;

    @Mock
    UserService mockUserService;

    @Test
    void getById_Should_ReturnCard_When_MatchByIdExists(){
        //Arrange
        Card mockCard = createMockCard();

        Mockito.when(cardRepository.getById(Mockito.anyInt()))
                .thenReturn(mockCard);
        //Act
        Card result = mockCardService.get(mockCard.getHolder(),mockCard.getId());

        //Assert
        Assertions.assertEquals(mockCard, result);
    }

    @Test
    void create_Should_throw_When_WalletCardWithSameNumberExists(){
        //Arrange
        Card mockCard = createMockCard();

        Mockito.when(cardRepository.getByField("number",mockCard.getNumber())).thenThrow(EntityDuplicateException.class);

        //Assert
        Assertions.assertThrows(EntityDuplicateException.class, ()->mockCardService.create(mockCard.getHolder(),mockCard));
    }

    @Test
    void create_Should_CallRepository_When_CardWithSameNumberDoesNotExist(){
        //Arrange
        Card mockCard = createMockCard();

        //Act
        Mockito.when(cardRepository.getByField("number",mockCard.getNumber())).thenThrow(EntityNotFoundException.class);
        mockCardService.create(mockCard.getHolder(),mockCard);

        //Assert
        Mockito.verify(cardRepository,Mockito.times(1))
                .create(mockCard);
    }

    @Test
    void delete_Should_CallRepository_When_MatchById(){
        //Arrange
        Card mockCard = createMockCard();

        //Act
        Mockito.when(cardRepository.getById(Mockito.anyInt())).thenReturn(mockCard);
        mockCardService.delete(mockCard.getHolder(),mockCard.getId());

        //Assert
        Mockito.verify(cardRepository,Mockito.times(1))
                .delete(Mockito.anyInt());
    }

    @Test
    void delete_Should_throw_When_UserIsNotHolder(){
        //Arrange
        Card mockCard = createMockCard();
        User user = createMockUser2();

        //Act
        Mockito.when(cardRepository.getById(Mockito.anyInt())).thenReturn(mockCard);

        //Assert
        Assertions.assertThrows(AuthorizationException.class, ()->mockCardService.delete(user,mockCard.getId()));

    }
}
