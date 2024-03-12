package com.telerikacademy.web.virtualwallet.utils;

import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.RegisterDto;
import com.telerikacademy.web.virtualwallet.models.dtos.UserDto;
import com.telerikacademy.web.virtualwallet.services.contracts.UserService;
import org.springframework.stereotype.Component;

@Component
public class UserRegisterMapper {

    public User fromDto(RegisterDto registerDto) {
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(registerDto.getPassword());
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPhoneNumber(registerDto.getPhoneNumber());
        return user;
    }
}
