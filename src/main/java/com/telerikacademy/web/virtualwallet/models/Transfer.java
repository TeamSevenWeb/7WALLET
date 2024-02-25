//package com.telerikacademy.web.virtualwallet.models;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//
//import java.time.LocalDateTime;
//import java.util.Objects;
//
//@Entity
//@Table(name = "transfers")
//public class Transfer {
//
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Id
//    @Column(name = "transfer_id")
//    private int id;
//
//    @JsonIgnore
//    @ManyToOne
//    @JoinColumn(name = "sender")
//    private User sender;
//
//    @JsonIgnore
//    @ManyToOne
//    @JoinColumn(name = "receiver")
//    private User receiver;
//
//    @JsonIgnore
//    @ManyToOne
//    @Column(name = "card")
//    private Card card;
//
//    @Column(name = "amount")
//    private long amount;
//
//    @Column(name = "direction")
//    private int direction;
//
//    @Column(name = "date")
//    private LocalDateTime date;
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Transfer transfer = (Transfer) o;
//
//        return id == transfer.id;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id);
//    }
//}
