package com.telerikacademy.web.virtualwallet;

import com.telerikacademy.web.virtualwallet.models.*;
import com.telerikacademy.web.virtualwallet.models.wallets.JoinWallet;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
        mockUser.getUserRoles().add(createMockAdminRole());
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

    public static Role createMockAdminRole(){
        var mockRole = new Role();
        mockRole.setRoleId(1);
        mockRole.setRoleType("admin");
        return mockRole;
    }

    public static Role createMockBlockedRole(){
        var mockRole = new Role();
        mockRole.setRoleId(1);
        mockRole.setRoleType("blocked");
        return mockRole;
    }

    public static Role createMockRegularRole(){
        var mockRole = new Role();
        mockRole.setRoleId(1);
        mockRole.setRoleType("regular");
        return mockRole;
    }

    public static TransactionVerificationCodes createMockTransactionVerificationCodes(){
        var mockTransactionVerificationCode = new TransactionVerificationCodes();
        mockTransactionVerificationCode.setTransactionVerificationCodeId(1);
        mockTransactionVerificationCode.setVerificationCode("testcode");
        mockTransactionVerificationCode.setSenderWallet(new Wallet());
        mockTransactionVerificationCode.setReceiverWallet(new Wallet());
        mockTransactionVerificationCode.setTransaction(createMockTransaction());
        return mockTransactionVerificationCode;
    }
    public static VerificationCodes createMockVerificationCodes(){
        var mockVerificationCode = new VerificationCodes();
        mockVerificationCode.setVerificationCode("testcode");
        mockVerificationCode.setVerificationCodeId(1);
        mockVerificationCode.setUser(createMockUser());
        return mockVerificationCode;
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

    public static Currency createMockCurrency(){
        var currency = new Currency();
        currency.setId(1);
        currency.setCurrencyCode("BGN");
        currency.setRating(1.1);

        return currency;
    }

    public static Wallet createMockWallet(){
        var wallet = new Wallet();
        wallet.setId(1);
        wallet.setName("TestWallet");
        wallet.setHolder(createMockUser());
        wallet.setHoldings(1);
        wallet.setCurrency(createMockCurrency());

        return wallet;
    }

    public static JoinWallet createMockJoinWallet(){
        var joinWallet = new JoinWallet();
        joinWallet.setId(1);
        joinWallet.setName("TestJoinWallet");
        joinWallet.setHolder(createMockUser());
        joinWallet.setHoldings(1);
        joinWallet.setCurrency(createMockCurrency());
        User user = createMockUser();
        User user1 = createMockUser2();
        Set<User> users = new HashSet<>();
        users.add(user);
        users.add(user1);
        joinWallet.setUsers(users);

        return joinWallet;
    }
}
