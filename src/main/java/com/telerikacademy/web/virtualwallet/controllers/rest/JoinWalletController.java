package com.telerikacademy.web.virtualwallet.controllers.rest;

import com.telerikacademy.web.virtualwallet.exceptions.AuthenticationException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.JoinWalletDto;
import com.telerikacademy.web.virtualwallet.models.dtos.UserDto;
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
    public List<JoinWallet> getJoinWallets(@RequestHeader HttpHeaders headers){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            List<JoinWallet> allJoinWallets = new ArrayList<>(joinWalletService.getAllByUser(user));
            allJoinWallets.addAll(user.getJoinWallets());
            return allJoinWallets;
        }  catch (AuthenticationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping
    public void createJoinWallet(@RequestHeader HttpHeaders headers,@Valid @RequestBody JoinWalletDto walletDto){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            JoinWallet wallet = joinWalletService.createJoinWallet(user,walletDto.getName());
            joinWalletService.create(wallet);
        }  catch (AuthenticationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
