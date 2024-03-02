package com.telerikacademy.web.virtualwallet.controllers.rest;

import com.telerikacademy.web.virtualwallet.exceptions.AuthenticationException;
import com.telerikacademy.web.virtualwallet.exceptions.AuthorizationException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityDuplicateException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtualwallet.models.Transfer;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.TransferDto;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;
import com.telerikacademy.web.virtualwallet.services.contracts.UserService;
import com.telerikacademy.web.virtualwallet.services.contracts.WalletService;
import com.telerikacademy.web.virtualwallet.utils.AuthenticationHelper;
import com.telerikacademy.web.virtualwallet.utils.TransferMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/wallet")
public class WalletRestController {

    private final WalletService walletService;

    private final UserService userService;

    private final TransferMapper transferMapper;

    private final AuthenticationHelper authenticationHelper;

    public WalletRestController(WalletService walletService, UserService userService, TransferMapper transferMapper, AuthenticationHelper authenticationHelper) {
        this.walletService = walletService;
        this.userService = userService;
        this.transferMapper = transferMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public Wallet getWallet(@RequestHeader HttpHeaders headers){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return walletService.getByUser(user);
        }  catch (AuthenticationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/wallet/fund")
    public Transfer createTransfer(@RequestHeader HttpHeaders headers, @Valid @RequestBody TransferDto transferDto) {
        try {
            User user = userService.getById(1);
            Transfer ingoing = transferMapper.ingoingFromDto(user,transferDto);
            walletService.transfer(ingoing);
            return ingoing;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
