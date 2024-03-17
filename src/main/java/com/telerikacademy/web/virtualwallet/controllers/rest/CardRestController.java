package com.telerikacademy.web.virtualwallet.controllers.rest;

import com.telerikacademy.web.virtualwallet.exceptions.AuthorizationException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityDuplicateException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtualwallet.models.Card;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.CardDto;
import com.telerikacademy.web.virtualwallet.services.contracts.CardService;
import com.telerikacademy.web.virtualwallet.utils.AuthenticationHelper;
import com.telerikacademy.web.virtualwallet.utils.CardMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestController
@RequestMapping("api/users/cards")
public class CardRestController {

    private final CardService cardService;

    private final CardMapper cardMapper;

    private final AuthenticationHelper authenticationHelper;


    @Autowired
    public CardRestController(CardService cardService, CardMapper cardMapper, AuthenticationHelper authenticationHelper) {
        this.cardService = cardService;
        this.cardMapper = cardMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping("/{id}")
    public Card getById(@RequestHeader(name = "Authentication")
                            String authentication, @PathVariable int id){
        try {
            User holder =  authenticationHelper.tryGetUser(authentication);
            return cardService.get(holder,id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping()
    public void create(@RequestHeader(name = "Authentication")
                           String authentication,@Valid @RequestBody CardDto cardDto) {
        try {
            User holder =  authenticationHelper.tryGetUser(authentication);
            Card card = cardMapper.fromDto(holder,cardDto);
            cardService.create(holder,card);
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader(name = "Authentication")
                           String authentication,@PathVariable int id){
        try {
            User holder =  authenticationHelper.tryGetUser(authentication);
            cardService.delete(holder,id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}
