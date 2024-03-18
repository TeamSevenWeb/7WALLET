package com.telerikacademy.web.virtualwallet.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserPasswordDto {
    @NotEmpty(message = "Password cannot be empty.")
    @Pattern(regexp = "((?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&+^-])[A-Za-z\\d@$!%*?&+^-]{8,}$)"
            , message = "Password must be at least 8 characters, contain at least one capital letter, special symbol and number.")
    private String password;

    @NotEmpty(message = "Password confirmation cannot be empty")
    private String passwordConfirm;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}
