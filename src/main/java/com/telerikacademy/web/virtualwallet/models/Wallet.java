package com.telerikacademy.web.virtualwallet.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "wallets")
public class Wallet {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "wallet_id")
    private int id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "holder")
    private User holder;


    @Column(name = "holdings")
    private long holdings;

    @Column(name = "currency")
    private String currency;

    public int getId() {
        return id;
    }

    public User getHolder() {
        return holder;
    }

    public long getHoldings() {
        return holdings;
    }

    public String getCurrency() {
        return currency;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHolder(User holder) {
        this.holder = holder;
    }

    public void setHoldings(long holdings) {
        this.holdings = holdings;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Wallet wallet = (Wallet) o;

        return id == wallet.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
