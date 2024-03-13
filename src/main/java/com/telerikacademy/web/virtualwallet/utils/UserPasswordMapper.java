package com.telerikacademy.web.virtualwallet.utils;

import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.UserPasswordDto;
import com.telerikacademy.web.virtualwallet.services.contracts.UserService;
import org.springframework.stereotype.Component;

@Component
public class UserPasswordMapper {
    private final UserService userService;
    public UserPasswordMapper(UserService userService) {
        this.userService = userService;
    }

    public User fromDto(int id, UserPasswordDto userDto) {
        User user = new User();
        User oldUser = userService.getById(id);
        user.setUserCards(oldUser.getUserCards());
        user.setUserRoles(oldUser.getUserRoles());
        user.setReceivedTransactions(oldUser.getReceivedTransactions());
        user.setSentTransactions(oldUser.getSentTransactions());
        user.setWallets(oldUser.getWallets());
        user.setId(oldUser.getId());
        user.setUsername(oldUser.getUsername());
        user.setUsername(oldUser.getUsername());
        user.setPassword(userDto.getPassword());
        user.setFirstName(oldUser.getFirstName());
        user.setLastName(oldUser.getLastName());
        user.setEmail(oldUser.getEmail());
        user.setPhoneNumber(oldUser.getPhoneNumber());
        return user;
    }

}
