package com.telerikacademy.web.virtualwallet.utils;

import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.UserDto;
import com.telerikacademy.web.virtualwallet.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final UserService userService;

    @Autowired
    public UserMapper(UserService userService) {
        this.userService = userService;
    }


    public User fromDto(int id, UserDto userDto) {
        User user = fromDto(userDto);
        User oldUser = userService.getById(id);
        user.setUserCards(oldUser.getUserCards());
        user.setUserRoles(oldUser.getUserRoles());
        user.setReceivedTransactions(oldUser.getReceivedTransactions());
        user.setSentTransactions(oldUser.getSentTransactions());
        user.setWallets(oldUser.getWallets());
        user.setId(oldUser.getId());
        return user;
    }

    public User fromDto(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        return user;
    }

}
