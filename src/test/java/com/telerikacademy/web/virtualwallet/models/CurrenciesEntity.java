package com.telerikacademy.web.virtualwallet.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "currencies", schema = "virtual_wallet", catalog = "")
public class CurrenciesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "currency_id")
    private int currencyId;
    @Basic
    @Column(name = "currency_code")
    private String currencyCode;

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrenciesEntity that = (CurrenciesEntity) o;
        return currencyId == that.currencyId && Objects.equals(currencyCode, that.currencyCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currencyId, currencyCode);
    }
}
