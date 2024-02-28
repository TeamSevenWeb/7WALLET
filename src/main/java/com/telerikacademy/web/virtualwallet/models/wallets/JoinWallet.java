package com.telerikacademy.web.virtualwallet.models.wallets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telerikacademy.web.virtualwallet.models.User;
import jakarta.persistence.*;

import java.util.Set;
@Entity
@Table(name = "wallets")
public class JoinWallet extends Wallet {

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "join_wallets_users",
            joinColumns = @JoinColumn(name="wallet_id"),
            inverseJoinColumns = @JoinColumn(name="user_id")

    )
    private Set<User> users;

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
