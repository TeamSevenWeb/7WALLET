package com.telerikacademy.web.virtualwallet.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.telerikacademy.web.virtualwallet.models.wallets.JoinWallet;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;
import jakarta.persistence.*;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id")
    private int id;

    @Column(name = "username")
    private String username;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "holder",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private Set<Card> userCards;

    @JsonManagedReference
    @OneToOne(mappedBy = "user",
    cascade = CascadeType.ALL,
    fetch = FetchType.EAGER)
    private ProfilePhoto profilePhoto;

    @JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "sender",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private Set<Transaction> sentTransactions;

    @JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "receiver",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private Set<Transaction> receivedTransactions;


    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<Role> userRoles;

    @JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "holder",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private Set<Wallet> wallets;

    @ManyToMany(mappedBy = "users",
    fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<JoinWallet> joinWallets;


    public Set<Role> getUserRoles() {
        return userRoles;
    }

    public ProfilePhoto getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(ProfilePhoto profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<Card> getUserCards() {
        return new HashSet<>(userCards);
    }

    public void setUserCards(Set<Card> userCards) {
        this.userCards = userCards;
    }

    public Set<Transaction> getSentTransactions() {
        return new HashSet<>(sentTransactions);
    }

    public void setSentTransactions(Set<Transaction> transactions) {
        this.sentTransactions = transactions;
    }

    public Set<Transaction> getReceivedTransactions() {
        return new HashSet<>(receivedTransactions);
    }

    public void setReceivedTransactions(Set<Transaction> receivedTransactions) {
        this.receivedTransactions = receivedTransactions;
    }

    public void setUserRoles(Set<Role> userRoles) {
        this.userRoles = userRoles;
    }

    public Wallet getWallet() {
        return getWallets().stream().toList().get(0);
    }

    public void setWallet(Wallet wallet) {
        List<Wallet> newWalletsList = new ArrayList<>(getWallets());
        newWalletsList.set(0,wallet);
        Set<Wallet> newWalletsSet = new HashSet<>(newWalletsList);
        setWallets(newWalletsSet);
    }

    public Set<Wallet> getWallets() {
        return wallets;
    }

    public void setWallets(Set<Wallet> wallets) {
        this.wallets = wallets;
    }

    public Set<JoinWallet> getJoinWallets() {
        return joinWallets;
    }

    public void setJoinWallets(Set<JoinWallet> joinWallets) {
        this.joinWallets = joinWallets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
