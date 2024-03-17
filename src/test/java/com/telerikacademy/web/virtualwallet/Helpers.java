package com.telerikacademy.web.virtualwallet;

import com.telerikacademy.web.virtualwallet.models.*;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;

import java.time.LocalDateTime;
import java.util.HashSet;

public class Helpers {

    public static User createMockUser() {
        var mockUser = new User();
        Role role = new Role();
        role.setRoleType("admin");
        mockUser.setId(1);
        mockUser.setUsername("MockUsername");
        mockUser.setPassword("MockPassword");
        mockUser.setLastName("MockLastName");
        mockUser.setFirstName("MockFirstName");
        mockUser.setEmail("mock@user.com");
        mockUser.setPhoneNumber("123456789123");
        mockUser.setUserRoles(new HashSet<>());
        mockUser.getUserRoles().add(role);
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
        mockUser.getWallet().setId(2);
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

    public static Transaction createMockTransaction(){
        var transaction = new Transaction();
        User sender = createMockUser();
        User receiver = createMockUser2();
        transaction.setId(1);
        transaction.setAmount(500);
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setDate(LocalDateTime.now());
        transaction.setExpirationDate(LocalDateTime.now().plusDays(2));

        return transaction;
    }
}
