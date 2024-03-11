package com.telerikacademy.web.virtualwallet.controllers.rest;

import com.telerikacademy.web.virtualwallet.exceptions.*;
import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.Transfer;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.JoinWalletDto;
import com.telerikacademy.web.virtualwallet.models.dtos.TransactionToJoinDto;
import com.telerikacademy.web.virtualwallet.models.dtos.TransferDto;
import com.telerikacademy.web.virtualwallet.models.dtos.UserToWalletDto;
import com.telerikacademy.web.virtualwallet.models.wallets.JoinWallet;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;
import com.telerikacademy.web.virtualwallet.services.contracts.JoinWalletService;
import com.telerikacademy.web.virtualwallet.services.contracts.TransactionService;
import com.telerikacademy.web.virtualwallet.services.contracts.WalletService;
import com.telerikacademy.web.virtualwallet.utils.AuthenticationHelper;
import com.telerikacademy.web.virtualwallet.utils.JoinWalletMapper;
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
@RequestMapping("/api/join wallet")
public class JoinWalletRestController {

    private final JoinWalletService joinWalletService;

    private final TransactionService transactionService;

    private final WalletService walletService;

    private final TransferMapper transferMapper;

    private final JoinWalletMapper joinWalletMapper;

    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public JoinWalletRestController(JoinWalletService joinWalletService, TransactionService transactionService, WalletService walletService, TransferMapper transferMapper, TransactionMapper transactionMapper, JoinWalletMapper joinWalletMapper, AuthenticationHelper authenticationHelper) {
        this.joinWalletService = joinWalletService;
        this.transactionService = transactionService;
        this.walletService = walletService;
        this.transferMapper = transferMapper;
        this.joinWalletMapper = joinWalletMapper;
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
            JoinWallet wallet = joinWalletMapper.fromDto(user,walletDto.getName());
            joinWalletService.create(wallet,user);
        }  catch (AuthenticationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public void updateJoinWallet(@RequestHeader HttpHeaders headers,@Valid @RequestBody JoinWalletDto walletDto
            ,@PathVariable int id){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            JoinWallet wallet = joinWalletMapper.fromDto(user,walletDto.getName(),id);
            joinWalletService.update(wallet,user,id);
        }  catch (AuthenticationException | AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
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

    @PostMapping("/{idFrom}/send/{idTo}")
    public Transaction sendToJoinWallet(@RequestHeader HttpHeaders headers, @PathVariable int idFrom
            ,@PathVariable int idTo,@Valid @RequestBody TransactionToJoinDto transactionDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            JoinWallet joinWalletOutgoing = joinWalletService.get(idFrom,user);
            JoinWallet joinWalletIngoing = joinWalletService.get(idTo,user);
            return transactionService.getTransaction(transactionDto, user, joinWalletOutgoing, joinWalletIngoing);
        }  catch (AuthenticationException | AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (FundsSupplyException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT,e.getMessage());
        }
    }
    @PostMapping("/{idFrom}/send")
    public Transaction sendToDefaultWallet(@RequestHeader HttpHeaders headers, @PathVariable int idFrom
            ,@Valid @RequestBody TransactionToJoinDto transactionDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            JoinWallet joinWalletOutgoing = joinWalletService.get(idFrom,user);
            Wallet walletIngoing = walletService.getByUser(user);
            return transactionService.getTransaction(transactionDto, user, joinWalletOutgoing, walletIngoing);
        }  catch (AuthenticationException | AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (FundsSupplyException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT,e.getMessage());
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

    @PutMapping("/{walletId}/currency/{currencyId}")
    public void changeCurrency(@RequestHeader HttpHeaders headers,@PathVariable int walletId
            ,@PathVariable int currencyId){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            JoinWallet wallet = joinWalletService.get(walletId,user);
            walletService.changeCurrency(wallet.getId(),currencyId);
        }  catch (AuthenticationException | AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
