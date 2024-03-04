package com.telerikacademy.web.virtualwallet;

import com.telerikacademy.web.virtualwallet.models.Card;
import com.telerikacademy.web.virtualwallet.models.ProfilePhoto;
import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;

import java.time.LocalDateTime;
import java.util.HashSet;

public class Helpers {

    public static User createMockUser() {
        var mockUser = new User();
        mockUser.setId(1);
        mockUser.setUsername("MockUsername");
        mockUser.setPassword("MockPassword");
        mockUser.setLastName("MockLastName");
        mockUser.setFirstName("MockFirstName");
        mockUser.setEmail("mock@user.com");
        mockUser.setPhoneNumber("123456789123");
        mockUser.setUserRoles(new HashSet<>());
        mockUser.setUserCards(new HashSet<>());
        mockUser.setWallets(new HashSet<>());
        mockUser.getWallets().add(new Wallet());
        mockUser.setProfilePhoto(new ProfilePhoto());
        return mockUser;
    }

    public static User createMockUser2() {
        var mockUser = new User();
        mockUser.setId(2);
        mockUser.setUsername("MockUsername2");
        mockUser.setPassword("MockPassword2");
        mockUser.setLastName("MockLastName2");
        mockUser.setFirstName("MockFirstName2");
        mockUser.setEmail("mock2@user.com");
        mockUser.setPhoneNumber("123456789124");
        mockUser.setUserRoles(new HashSet<>());
        mockUser.setUserCards(new HashSet<>());
        mockUser.setWallets(new HashSet<>());
        mockUser.getWallets().add(new Wallet());
        mockUser.setProfilePhoto(new ProfilePhoto());
        return mockUser;
    }

    public static Card createMockCard() {
        var mockCard = new Card();
        var user = createMockUser();
        mockCard.setId(1);
        mockCard.setNumber("1234567891234567");
        mockCard.setExpirationDate("01/01");
        mockCard.setCvv("123");
        mockCard.setHolder(user);
        user.getUserCards().add(mockCard);
        return mockCard;
    }

    public static ProfilePhoto createMockProfilePhoto() {
        var profilePhoto = new ProfilePhoto();
        profilePhoto.setUser(createMockUser());
        profilePhoto.setProfilePhotoId(1);
        profilePhoto.setProfilePhoto("testpicture.png");
        return profilePhoto;
    }

    public static Transaction createMockTransactionOutgoing(){
        var transaction = new Transaction();
        User sender = createMockUser();
        User receiver = createMockUser2();
        transaction.setId(1);
        transaction.setAmount(1000);
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setDirection(2);
        transaction.setWallet(sender.getWallet());
        transaction.setDate(LocalDateTime.now());

        return transaction;
    }

    public static Transaction createMockTransactionIngoing(){
        var transaction = new Transaction();
        User sender = createMockUser();
        User receiver = createMockUser2();
        transaction.setId(2);
        transaction.setAmount(1000);
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setDirection(1);
        transaction.setWallet(sender.getWallet());
        transaction.setDate(LocalDateTime.now());

        return transaction;
    }
}
