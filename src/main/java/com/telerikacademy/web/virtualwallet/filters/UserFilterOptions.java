package com.telerikacademy.web.virtualwallet.filters;

import java.util.Optional;

public class UserFilterOptions {
    private Optional<String> username;
    private Optional<String> email;
    private Optional<String> phoneNumber;

    public UserFilterOptions() {
        this(null, null, null);
    }

    public UserFilterOptions(
            String username,
            String email,
            String phoneNumber){
        this.username = Optional.ofNullable(username);
        this.email = Optional.ofNullable(email);
        this.phoneNumber = Optional.ofNullable(phoneNumber);

    }

    public Optional<String> getUsername() {
        return username;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public Optional<String> getPhoneNumber() {
        return phoneNumber;
    }


}

