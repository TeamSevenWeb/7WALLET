package com.telerikacademy.web.virtualwallet.utils;

import com.telerikacademy.web.virtualwallet.filters.UserFilterOptions;
import com.telerikacademy.web.virtualwallet.filters.dtos.UserFilterOptionsDto;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserFilterOptionsMapper {

    public UserFilterOptions fromDto(UserFilterOptionsDto userFilterOptionsDto) {
        UserFilterOptions userFilterOptions = new UserFilterOptions(
                userFilterOptionsDto.getUsername(),
                userFilterOptionsDto.getEmail(),
                userFilterOptionsDto.getPhoneNumber());

        return userFilterOptions;
    }

}
