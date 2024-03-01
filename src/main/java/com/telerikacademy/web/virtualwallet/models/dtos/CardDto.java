package com.telerikacademy.web.virtualwallet.models.dtos;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Date;

public class CardDto {

    @Pattern(regexp = "(^[a-zA-Z]$)")
    @Size(min = 2,max = 32,message = "Please enter a name between 2 and 30 symbols.")
    private String holder;

    @Pattern(regexp = "(^[0-9]$)")
    @Size(min = 16,max = 16,message = "Please enter a valid card number.")
    private String number;

    @Pattern(regexp = "(^[0-9]$)")
    @Size(min = 3,max = 3,message = "Invalid cvv number.")
    private String cvv;

    private String expirationDate;

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
