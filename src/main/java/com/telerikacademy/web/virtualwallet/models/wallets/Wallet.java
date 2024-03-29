package com.telerikacademy.web.virtualwallet.models.wallets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telerikacademy.web.virtualwallet.models.Currency;
import com.telerikacademy.web.virtualwallet.models.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "wallets")
@DiscriminatorColumn(name = "wallet_type",
        discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorValue("1")
public class Wallet {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "wallet_id")
    private int id;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "holder")
    private User holder;


    @Column(name = "holdings")
    private double holdings;

    @ManyToOne
    @JoinColumn(name = "currency")
    private Currency currency;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public User getHolder() {
        return holder;
    }

    public double getHoldings() {
        return holdings;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHolder(User holder) {
        this.holder = holder;
    }

    public void setHoldings(double holdings) {
        this.holdings = holdings;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getHoldingsByRating(){
        BigDecimal holdingsDecimal = new BigDecimal(holdings);
        BigDecimal decimalResult =  holdingsDecimal.multiply(BigDecimal.valueOf(currency.getRating()));
        return String.format("%.2f",decimalResult.doubleValue());
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
