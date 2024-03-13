package com.telerikacademy.web.virtualwallet.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telerikacademy.web.virtualwallet.models.dtos.TransactionDto;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "transactions")
public class Transaction {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "transaction_id")
    private int id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sender")
    private User sender;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "receiver")
    private User receiver;


    @Column(name = "amount")
    private double amount;

    @Column(name = "date")
    private LocalDateTime date;


    public Transaction(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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

        Transaction transaction = (Transaction) o;

        return id == transaction.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
