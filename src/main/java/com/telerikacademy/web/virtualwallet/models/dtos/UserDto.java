package com.telerikacademy.web.virtualwallet.models.dtos;

import jakarta.validation.constraints.*;

public class UserDto {

    @NotNull(message = "Username cannot be null.")
    @Size(min = 2, max = 20, message = "Username must be between 2 and 20 characters.")
    private String username;

    @NotEmpty(message = "Password cannot be empty.")
    @Pattern(regexp = "((?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&+^-])[A-Za-z\\d@$!%*?&+^-]{8,}$)")
    @Size(min = 8)
    private String password;
    @Pattern(regexp = "(^[a-zA-Z]$)")
    @NotBlank(message = "First name cannot be empty.")
    private String firstName;

    @Pattern(regexp = "(^[a-zA-Z]$)")
    @NotBlank(message = "Last name cannot be empty.")
    private String lastName;

    @NotBlank(message = "Email cannot be empty.")
    @Email
    private String email;

    @Pattern(regexp = "(^\\+?[0-9+]$)")
    @NotEmpty(message = "Phone number cannot be empty.")
    @Size(min = 10, max = 10)
    private String phoneNumber;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
