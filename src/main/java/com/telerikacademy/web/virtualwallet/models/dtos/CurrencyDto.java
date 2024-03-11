package com.telerikacademy.web.virtualwallet.models.dtos;

import jakarta.validation.constraints.*;

public class CurrencyDto {

    @NotEmpty
    @Size(min = 3, max = 3, message = "Currency code must be 3 characters.")
    private String currencyCode;

    @NotNull(message = "Rating can't be empty")
    private double rating;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
