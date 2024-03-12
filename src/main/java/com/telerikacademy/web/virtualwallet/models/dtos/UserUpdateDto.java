package com.telerikacademy.web.virtualwallet.models.dtos;

import jakarta.validation.constraints.*;

public class UserUpdateDto {

    @Pattern(regexp = "(^[a-zA-Z]*$)")
    @NotBlank(message = "First name cannot be empty.")
    private String firstName;

    @Pattern(regexp = "(^[a-zA-Z]*$)")
    @NotBlank(message = "Last name cannot be empty.")
    private String lastName;

    @NotBlank(message = "Email cannot be empty.")
    @Email
    private String email;

    @Pattern(regexp = "([0-9+]*$)")
    @NotEmpty(message = "Phone number cannot be empty.")
    @Size(min = 10, max = 10, message = "Please enter a valid phone number.")
    private String phoneNumber;

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
