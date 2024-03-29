package com.telerikacademy.web.virtualwallet.controllers.rest;

import com.telerikacademy.web.virtualwallet.exceptions.*;
import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.Transfer;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.TransactionDto;
import com.telerikacademy.web.virtualwallet.models.dtos.TransactionToJoinDto;
import com.telerikacademy.web.virtualwallet.models.dtos.TransferDto;
import com.telerikacademy.web.virtualwallet.models.wallets.JoinWallet;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;
import com.telerikacademy.web.virtualwallet.services.contracts.JoinWalletService;
import com.telerikacademy.web.virtualwallet.services.contracts.TransactionService;
import com.telerikacademy.web.virtualwallet.services.contracts.WalletService;
import com.telerikacademy.web.virtualwallet.utils.AuthenticationHelper;
import com.telerikacademy.web.virtualwallet.utils.TransactionMapper;
import com.telerikacademy.web.virtualwallet.utils.TransferMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/wallet")
public class WalletRestController {

    private final WalletService walletService;

    private final JoinWalletService joinWalletService;

    private final TransferMapper transferMapper;

    private final AuthenticationHelper authenticationHelper;

    private final TransactionMapper transactionMapper;

    private final TransactionService transactionService;


    @Autowired
    public WalletRestController(WalletService walletService, JoinWalletService joinWalletService, TransferMapper transferMapper, AuthenticationHelper authenticationHelper, TransactionMapper transactionMapper, TransactionService transactionService) {
        this.walletService = walletService;
        this.joinWalletService = joinWalletService;
        this.transferMapper = transferMapper;
        this.authenticationHelper = authenticationHelper;
        this.transactionMapper = transactionMapper;
        this.transactionService = transactionService;
    }

    @GetMapping
    public Wallet getWallet(@RequestHeader(name = "Authentication")
                                String authentication){
        try {
            User user = authenticationHelper.tryGetUser(authentication);
            return walletService.getByUser(user);
        }  catch (AuthenticationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/fund")
    public Transfer fundWallet(@RequestHeader(name = "Authentication")
                                   String authentication, @Valid @RequestBody TransferDto transferDto) {
        try {
            User user = authenticationHelper.tryGetUser(authentication);
            Transfer ingoing = transferMapper.ingoingFromDto(user,transferDto);
            walletService.transfer(ingoing);
            return ingoing;
        }  catch (AuthenticationException | AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (TransferFailedException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    @PostMapping("/withdraw")
    public Transfer withdrawToCard(@RequestHeader(name = "Authentication")
                                       String authentication, @Valid @RequestBody TransferDto transferDto) {
        try {
            User user = authenticationHelper.tryGetUser(authentication);
            Transfer outgoing = transferMapper.outgoingFromDto(user,transferDto);
            walletService.transfer(outgoing);
            return outgoing;
        }  catch (AuthenticationException | AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (TransferFailedException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }  catch (FundsSupplyException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }


    @PostMapping("/send")
    public Transaction createTransaction(@RequestHeader(name = "Authentication")
                                             String authentication, @Valid @RequestBody TransactionDto transactionDto) {
        try {
            User sender = authenticationHelper.tryGetUser(authentication);
            Transaction transaction = transactionMapper.fromDto(sender, transactionDto);
            transactionService.create(transaction,sender.getWallet(),transaction.getReceiver().getWallet());
            return transaction;
        }  catch (AuthenticationException | AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (FundsSupplyException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT,e.getMessage());
        }
    }

    @PostMapping("/send/{id}")
    public Transaction sendToJoinWallet(@RequestHeader(name = "Authentication")
                                            String authentication,@PathVariable int id
            ,@Valid @RequestBody TransactionToJoinDto transactionDto) {
        try{
            User user = authenticationHelper.tryGetUser(authentication);
            JoinWallet joinWallet = joinWalletService.get(id,user);
            Transaction transaction = transactionMapper.fromDtoToJoin(user,transactionDto);
            transactionService.create(transaction,user.getWallet(),joinWallet);
            return transaction;
        }  catch (AuthenticationException | AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (FundsSupplyException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT,e.getMessage());
        }
    }

    @PutMapping("/currency/{id}")
    public void changeCurrency(@RequestHeader(name = "Authentication")
                                   String authentication,@PathVariable int id){
        try {
            User user = authenticationHelper.tryGetUser(authentication);
            Wallet wallet = walletService.getByUser(user);
            walletService.changeCurrency(wallet.getId(),id);
        }  catch (AuthenticationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
