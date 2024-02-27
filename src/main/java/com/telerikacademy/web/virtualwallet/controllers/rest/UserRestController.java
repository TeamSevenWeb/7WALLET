package com.telerikacademy.web.virtualwallet.controllers.rest;

import com.telerikacademy.web.virtualwallet.exceptions.AuthorizationException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityDuplicateException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtualwallet.models.ProfilePhoto;
import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.ProfilePhotoDto;
import com.telerikacademy.web.virtualwallet.models.dtos.TransactionDto;
import com.telerikacademy.web.virtualwallet.models.dtos.UserDto;
import com.telerikacademy.web.virtualwallet.services.contracts.TransactionService;
import com.telerikacademy.web.virtualwallet.services.contracts.UserService;
import com.telerikacademy.web.virtualwallet.utils.ProfilePhotoMapper;
import com.telerikacademy.web.virtualwallet.utils.TransactionMapper;
import com.telerikacademy.web.virtualwallet.utils.UserMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    private final UserService userService;

    private final TransactionService transactionService;

    private final ProfilePhotoMapper profilePhotoMapper;

    private final TransactionMapper transactionMapper;

    private final UserMapper userMapper;

    @Autowired
    public UserRestController(UserService userService, TransactionService transactionService, ProfilePhotoMapper profilePhotoMapper, TransactionMapper transactionMapper, UserMapper userMapper) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.profilePhotoMapper = profilePhotoMapper;
        this.transactionMapper = transactionMapper;
        this.userMapper = userMapper;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        try {
            return userService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        try {
            return userService.getAll();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/new")
    public void create(@Valid @RequestBody UserDto userDto) {
        try {
            User user = userMapper.fromDto(userDto);
            userService.create(user);
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}/update")
    public void update(@PathVariable int id, @Valid @RequestBody UserDto userDto) {
        try {
            User user = userMapper.fromDto(id, userDto);
            userService.update(user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/{id}/make-admin")
    public void makeAdmin(@PathVariable int id) {
        try {
            userService.makeAdmin(id, userService.getById(1));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

    @PostMapping("/{id}/make-admin")
    public void removeAdmin(@PathVariable int id) {
        try {
            userService.removeAdmin(id, userService.getById(1));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

    @PostMapping("/{id}/block")
    public void blockUser(@PathVariable int id) {
        try {
            userService.block(id, userService.getById(1));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

    @PostMapping("/{id}/unblock")
    public void unBlockUser(@PathVariable int id) {
        try {
            userService.unblock(id, userService.getById(1));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/{id}/uploadPhoto")
    public void uploadPhoto(@PathVariable int id, @Valid @RequestBody ProfilePhotoDto profilePhotoDto) {
        try {
            ProfilePhoto profilePhoto = profilePhotoMapper.fromDto(profilePhotoDto);
            userService.uploadProfilePhoto(profilePhoto, userService.getById(id));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

    @PutMapping("/{id}/updatePhoto")
    public void updatePhoto(@PathVariable int id, @Valid @RequestBody ProfilePhotoDto profilePhotoDto) {
        try {
            ProfilePhoto profilePhoto = profilePhotoMapper.fromDto(id, profilePhotoDto);
            userService.updateProfilePhoto(profilePhoto);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Transaction getTransaction(@PathVariable int id) {
        try {
            return transactionService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping()
    public Transaction createTransaction(@RequestHeader HttpHeaders headers, @Valid @RequestBody TransactionDto transactionDto) {
        try {
            User sender = getUser(1);
            Transaction outgoing = transactionMapper.outgoingFromDto(sender, transactionDto);
            Transaction ingoing = transactionMapper.ingoingFromDto(sender, transactionDto);
            transactionService.create(outgoing, ingoing, sender);
            return outgoing;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

}
