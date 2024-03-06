package com.telerikacademy.web.virtualwallet.controllers.rest;

import com.telerikacademy.web.virtualwallet.exceptions.*;
import com.telerikacademy.web.virtualwallet.models.Transfer;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.JoinWalletDto;
import com.telerikacademy.web.virtualwallet.models.dtos.TransferDto;
import com.telerikacademy.web.virtualwallet.models.dtos.UserToWalletDto;
import com.telerikacademy.web.virtualwallet.models.wallets.JoinWallet;
import com.telerikacademy.web.virtualwallet.services.contracts.JoinWalletService;
import com.telerikacademy.web.virtualwallet.services.contracts.WalletService;
import com.telerikacademy.web.virtualwallet.utils.AuthenticationHelper;
import com.telerikacademy.web.virtualwallet.utils.TransferMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/join wallet")
public class JoinWalletRestController {

    private final JoinWalletService joinWalletService;

    private final WalletService walletService;

    private final TransferMapper transferMapper;

    private final AuthenticationHelper authenticationHelper;

    public JoinWalletRestController(JoinWalletService joinWalletService, WalletService walletService, TransferMapper transferMapper, AuthenticationHelper authenticationHelper) {
        this.joinWalletService = joinWalletService;
        this.walletService = walletService;
        this.transferMapper = transferMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<JoinWallet> getAllJoinWallets(@RequestHeader HttpHeaders headers){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return joinWalletService.getAllByUser(user);
        }  catch (AuthenticationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public JoinWallet getJoinWallet(@RequestHeader HttpHeaders headers, @PathVariable int id){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            return joinWalletService.get(id,user);
        } catch (AuthenticationException | AuthorizationException e){
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

    @PostMapping("/{id}/fund")
    public Transfer fundWallet(@RequestHeader HttpHeaders headers, @PathVariable int id,
                               @Valid @RequestBody TransferDto transferDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            JoinWallet wallet = joinWalletService.get(id,user);
            Transfer ingoing = transferMapper.ingoingFromDto(user,wallet,transferDto);
            walletService.transfer(ingoing);
            return ingoing;
        }  catch (AuthenticationException | AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (TransferFailedException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    @PostMapping("/{id}/withdraw")
    public Transfer withdrawToCard(@RequestHeader HttpHeaders headers, @PathVariable int id,
                                   @Valid @RequestBody TransferDto transferDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            JoinWallet wallet = joinWalletService.get(id,user);
            Transfer outgoing = transferMapper.outgoingFromDto(user,wallet,transferDto);
            walletService.transfer(outgoing);
            return outgoing;
        }  catch (AuthenticationException | AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (TransferFailedException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (FundsSupplyException e){
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
            joinWalletService.removeOtherUser(id,userToAdd.getUser(),owner);
        }  catch (AuthenticationException | AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }

    @PostMapping("/{id}/remove")
    public void removeWallet(@RequestHeader HttpHeaders headers, @PathVariable int id){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            joinWalletService.removeWallet(id,user);
        }  catch (AuthenticationException | AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }
}
