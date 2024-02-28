package com.telerikacademy.web.virtualwallet.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "transfers")
public class Transfer {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "transfer_id")
    private int id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "wallet")
    private Wallet wallet;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "card")
    private Card card;

    @Column(name = "amount")
    private long amount;

    @Column(name = "direction")
    private int direction;

    @Column(name = "date")
    private LocalDateTime date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transfer transfer = (Transfer) o;

        return id == transfer.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
