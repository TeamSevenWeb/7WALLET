package com.telerikacademy.web.virtualwallet.controllers.rest;

import com.telerikacademy.web.virtualwallet.exceptions.AuthorizationException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityDuplicateException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtualwallet.models.*;
import com.telerikacademy.web.virtualwallet.models.dtos.ProfilePhotoDto;
import com.telerikacademy.web.virtualwallet.models.dtos.TransactionDto;
import com.telerikacademy.web.virtualwallet.services.contracts.TransactionService;
import com.telerikacademy.web.virtualwallet.services.contracts.UserService;
import com.telerikacademy.web.virtualwallet.services.contracts.WalletService;
import com.telerikacademy.web.virtualwallet.utils.ProfilePhotoMapper;
import com.telerikacademy.web.virtualwallet.utils.TransactionMapper;
import com.telerikacademy.web.virtualwallet.utils.TransferMapper;
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

    private final TransferMapper transferMapper;

    private final WalletService walletService;

    @Autowired
    public UserRestController(UserService userService, TransactionService transactionService, ProfilePhotoMapper profilePhotoMapper, TransactionMapper transactionMapper, TransferMapper transferMapper, WalletService walletService) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.profilePhotoMapper = profilePhotoMapper;
        this.transactionMapper = transactionMapper;
        this.transferMapper = transferMapper;
        this.walletService = walletService;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id){
        try {
            return userService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping
    public List<User> getAllUsers(){
        try {
            return userService.getAll();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    @PostMapping("/{id}/block")
    public void blockUser(@PathVariable int id){
        try {
            userService.block(id,userService.getById(1));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

    @PostMapping("/{id}/unblock")
    public void unBlockUser(@PathVariable int id){
        try {
            userService.unblock(id,userService.getById(1));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/{id}/uploadPhoto")
    public void uploadPhoto(@PathVariable int id, @Valid @RequestBody ProfilePhotoDto profilePhotoDto){
        ProfilePhoto profilePhoto = profilePhotoMapper.fromDto(profilePhotoDto);
        userService.uploadProfilePhoto(profilePhoto,userService.getById(id));

    }

    @PostMapping("/{id}/updatePhoto")
    public void updatePhoto(@PathVariable int id, @Valid @RequestBody ProfilePhotoDto profilePhotoDto){
        ProfilePhoto profilePhoto = profilePhotoMapper.fromDto(id, profilePhotoDto);
        userService.updateProfilePhoto(profilePhoto);
    }

    @GetMapping("/transaction/{id}")
    public Transaction getTransaction(@PathVariable int id){
        try {
            return transactionService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/transaction")
    public Transaction createTransaction(@RequestHeader HttpHeaders headers, @Valid @RequestBody TransactionDto transactionDto) {
        try {
            User sender = getUser(1);
            Transaction outgoing = transactionMapper.outgoingFromDto(sender, transactionDto);
            Transaction ingoing = transactionMapper.ingoingFromDto(sender,transactionDto);
            transactionService.create(outgoing,ingoing, sender);
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
