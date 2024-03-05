package com.telerikacademy.web.virtualwallet.controllers.rest;

import com.telerikacademy.web.virtualwallet.exceptions.AuthenticationException;
import com.telerikacademy.web.virtualwallet.exceptions.AuthorizationException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityDuplicateException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.JoinWalletDto;
import com.telerikacademy.web.virtualwallet.models.dtos.UserDto;
import com.telerikacademy.web.virtualwallet.models.dtos.UserToWalletDto;
import com.telerikacademy.web.virtualwallet.models.wallets.JoinWallet;
import com.telerikacademy.web.virtualwallet.services.contracts.JoinWalletService;
import com.telerikacademy.web.virtualwallet.utils.AuthenticationHelper;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/join wallet")
public class JoinWalletController {

    private final JoinWalletService joinWalletService;

    private final AuthenticationHelper authenticationHelper;

    public JoinWalletController(JoinWalletService joinWalletService, AuthenticationHelper authenticationHelper) {
        this.joinWalletService = joinWalletService;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<JoinWallet> getAllJoinWallets(@RequestHeader HttpHeaders headers){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            List<JoinWallet> allJoinWallets = new ArrayList<>(joinWalletService.getAllByUser(user));
            allJoinWallets.addAll(user.getJoinWallets());
            return allJoinWallets;
        }  catch (AuthenticationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public JoinWallet getJoinWallet(@RequestHeader HttpHeaders headers, @PathVariable int id){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return joinWalletService.get(id,user);
        } catch (AuthenticationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public void createJoinWallet(@RequestHeader HttpHeaders headers,@Valid @RequestBody JoinWalletDto walletDto){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            JoinWallet wallet = joinWalletService.createJoinWallet(user,walletDto.getName());
            joinWalletService.create(wallet,user);
        }  catch (AuthenticationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping("/{id}/add-user")
    public void addUserToWallet(@RequestHeader HttpHeaders headers, @Valid @RequestBody UserToWalletDto userToAdd
            ,@PathVariable int id){
        try {
            User owner = authenticationHelper.tryGetUser(headers);
            joinWalletService.addUser(id,userToAdd.getUser(),owner);
        }  catch (AuthenticationException | AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }

    @PostMapping("/{id}/remove-user")
    public void removeUserFromWallet(@RequestHeader HttpHeaders headers, @Valid @RequestBody UserToWalletDto userToAdd
            ,@PathVariable int id){
        try {
            User owner = authenticationHelper.tryGetUser(headers);
            joinWalletService.removeUser(id,userToAdd.getUser(),owner);
        }  catch (AuthenticationException | AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }
}
