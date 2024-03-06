package com.telerikacademy.web.virtualwallet.controllers.rest;

import com.telerikacademy.web.virtualwallet.exceptions.*;
import com.telerikacademy.web.virtualwallet.models.Card;
import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.Transfer;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.TransactionDto;
import com.telerikacademy.web.virtualwallet.models.dtos.TransferDto;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;
import com.telerikacademy.web.virtualwallet.services.contracts.TransactionService;
import com.telerikacademy.web.virtualwallet.services.contracts.UserService;
import com.telerikacademy.web.virtualwallet.services.contracts.WalletService;
import com.telerikacademy.web.virtualwallet.utils.AuthenticationHelper;
import com.telerikacademy.web.virtualwallet.utils.TransactionMapper;
import com.telerikacademy.web.virtualwallet.utils.TransferMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/wallet")
public class WalletRestController {

    private final WalletService walletService;

    private final TransferMapper transferMapper;

    private final AuthenticationHelper authenticationHelper;

    private final TransactionMapper transactionMapper;

    private final TransactionService transactionService;



    public WalletRestController(WalletService walletService, TransferMapper transferMapper, AuthenticationHelper authenticationHelper, TransactionMapper transactionMapper, TransactionService transactionService) {
        this.walletService = walletService;
        this.transferMapper = transferMapper;
        this.authenticationHelper = authenticationHelper;
        this.transactionMapper = transactionMapper;
        this.transactionService = transactionService;
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

    @PostMapping("/fund")
    public Transfer fundWallet(@RequestHeader HttpHeaders headers, @Valid @RequestBody TransferDto transferDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
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
        catch (TransferFailedException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    @PostMapping("/withdraw")
    public Transfer withdrawToCard(@RequestHeader HttpHeaders headers, @Valid @RequestBody TransferDto transferDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Transfer outgoing = transferMapper.outgoingFromDto(user,transferDto);
            walletService.transfer(outgoing);
            return outgoing;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (TransferFailedException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }  catch (FundsSupplyException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }


    @PostMapping("/send")
    public Transaction createTransaction(@RequestHeader HttpHeaders headers, @Valid @RequestBody TransactionDto transactionDto) {
        try {
            User sender = authenticationHelper.tryGetUser(headers);
            Transaction outgoing = transactionMapper.fromDto(sender, transactionDto);
            Transaction ingoing = new Transaction(outgoing);
            ingoing.setDirection(1);
            transactionService.create(outgoing,ingoing, sender);
            return outgoing;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
        catch (FundsSupplyException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }
}
