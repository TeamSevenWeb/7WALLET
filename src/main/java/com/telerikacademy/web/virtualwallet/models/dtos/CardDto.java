package com.telerikacademy.web.virtualwallet.models.dtos;

import jakarta.validation.constraints.Size;

import java.util.Date;

public class CardDto {

    @Size(min = 2,max = 32,message = "Please enter a name between 2 and 30 symbols.")
    private String holder;

    @Size(min = 16,max = 16,message = "Please enter a valid card number.")
    private int number;

    @Size(min = 3,max = 3,message = "Invalid cvv number.")
    private int cvv;

    private Date expirationDate;

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
