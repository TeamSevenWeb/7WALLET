package com.telerikacademy.web.virtualwallet.utils;

import com.telerikacademy.web.virtualwallet.exceptions.AuthenticationException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHelper {
    private static final String AUTHENTICATION_HEADER_NAME = "Authentication";
    private static final String INVALID_AUTHENTICATION_ERROR = "Invalid authentication.";
    private final HttpHeaders headers = new HttpHeaders();

    private final UserService userService;

    @Autowired
    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
    }

    public User tryGetUser(String headers1) {
        try {
            headers.add(AUTHENTICATION_HEADER_NAME,headers1);
            String userInfo = headers.getFirst(AUTHENTICATION_HEADER_NAME);
            String username = getUsername(userInfo);
            String password = getPassword(userInfo);
            User user = userService.getByUsername(username);

            if (!user.getPassword().equals(password)) {
                throw new AuthenticationException(INVALID_AUTHENTICATION_ERROR);
            }

            return user;
        } catch (EntityNotFoundException e) {
            throw new AuthenticationException(INVALID_AUTHENTICATION_ERROR);
        }
    }

    private String getUsername(String userInfo) {
        int firstSpace = userInfo.indexOf(" ");
        if (firstSpace == -1) {
            throw new AuthenticationException(INVALID_AUTHENTICATION_ERROR);
        }

        return userInfo.substring(0, firstSpace);
    }

    private String getPassword(String userInfo) {
        int firstSpace = userInfo.indexOf(" ");
        if (firstSpace == -1) {
            throw new AuthenticationException(INVALID_AUTHENTICATION_ERROR);
        }

        return userInfo.substring(firstSpace + 1);
    }

    public User tryGetCurrentUser(HttpSession session) {
        String currentUsername = (String) session.getAttribute("currentUser");

        if (currentUsername == null) {
            throw new AuthenticationException(INVALID_AUTHENTICATION_ERROR);
        }

        return userService.getByUsername(currentUsername);
    }

    public User verifyAuthentication(String username, String password) {
        try {
            User user = userService.getByUsername(username);
            if (!user.getPassword().equals(password)) {
                throw new AuthenticationException(INVALID_AUTHENTICATION_ERROR);
            }
            if (!userService.isRegular(user)){
                throw new AuthenticationException("Please check your email to verify your account.");
            }
            return user;
        } catch (EntityNotFoundException e) {
            throw new AuthenticationException(INVALID_AUTHENTICATION_ERROR);
        }
    }
}
