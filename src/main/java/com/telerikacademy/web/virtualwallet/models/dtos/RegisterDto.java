package com.telerikacademy.web.virtualwallet.models.dtos;

import jakarta.validation.constraints.NotEmpty;

public class RegisterDto extends UserDto {

    @NotEmpty(message = "Password confirmation cannot be empty")
    private String passwordConfirm;


    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }


}
