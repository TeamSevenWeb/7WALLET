package com.telerikacademy.web.virtualwallet.utils;

import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.UserDto;
import com.telerikacademy.web.virtualwallet.models.dtos.UserUpdateDto;
import com.telerikacademy.web.virtualwallet.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserUpdateMapper {

    private final UserService userService;

    @Autowired
    public UserUpdateMapper(UserService userService) {
        this.userService = userService;
    }

    public User fromDto(int id, UserUpdateDto userDto) {
        User user = fromDto(userDto);
        User oldUser = userService.getById(id);
        user.setUserCards(oldUser.getUserCards());
        user.setUserRoles(oldUser.getUserRoles());
        user.setWallets(oldUser.getWallets());
        user.setId(oldUser.getId());
        user.setPassword(oldUser.getPassword());
        user.setUsername(oldUser.getUsername());
        return user;
    }

    public User fromDto(UserUpdateDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        return user;
    }

    public UserUpdateDto toDto(User user){
        UserUpdateDto userDto = new UserUpdateDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        return userDto;
    }
}
