package com.telerikacademy.web.virtualwallet.controllers.rest;

import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id){
            return userService.getById(id);

    }

    @PostMapping("/{id}/block")
    public void blockUser(@PathVariable int id){
        userService.block(id,userService.getById(1));

    }
}
