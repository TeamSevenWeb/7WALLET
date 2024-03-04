package com.telerikacademy.web.virtualwallet.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class JoinWalletDto {

    @NotEmpty
    @Size(min = 2, max = 20, message = "Wallet name must be between 2 and 20 characters.")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
