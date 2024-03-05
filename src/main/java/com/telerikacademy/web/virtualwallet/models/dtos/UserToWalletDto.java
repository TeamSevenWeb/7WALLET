package com.telerikacademy.web.virtualwallet.models.dtos;

import jakarta.validation.constraints.NotNull;

public class UserToWalletDto {


    @NotNull(message = "User to add/remove can not be empty")
    private String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
