package com.telerikacademy.web.virtualwallet;

import com.telerikacademy.web.virtualwallet.models.Card;
import com.telerikacademy.web.virtualwallet.models.ProfilePhoto;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;

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
        mockUser.setWallet(new Wallet());
        mockUser.setProfilePhoto(new ProfilePhoto());
        return mockUser;
    }

    public static Card createMockCard() {
        var mockCard = new Card();
        var user = createMockUser();
        mockCard.setId(1);
        mockCard.setNumber("1234567891234567");
        mockCard.setHolder(user);
        mockCard.setExpirationDate("01/01");
        mockCard.setCvv("123");
        return mockCard;
    }

    public static ProfilePhoto createMockProfilePhoto() {
        var profilePhoto = new ProfilePhoto();
        profilePhoto.setUser(createMockUser());
        profilePhoto.setProfilePhotoId(1);
        profilePhoto.setProfilePhoto("testpicture.png");
        return profilePhoto;
    }

}
