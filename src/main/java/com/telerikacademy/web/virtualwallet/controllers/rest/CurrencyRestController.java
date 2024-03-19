package com.telerikacademy.web.virtualwallet.controllers.rest;

import com.telerikacademy.web.virtualwallet.exceptions.AuthenticationException;
import com.telerikacademy.web.virtualwallet.exceptions.AuthorizationException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityDuplicateException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtualwallet.models.Currency;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.CurrencyDto;
import com.telerikacademy.web.virtualwallet.services.contracts.CurrencyService;
import com.telerikacademy.web.virtualwallet.utils.AuthenticationHelper;
import com.telerikacademy.web.virtualwallet.utils.CurrencyMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/currency")
public class CurrencyRestController {

    private final CurrencyService currencyService;

    private final CurrencyMapper currencyMapper;

    private final AuthenticationHelper authenticationHelper;

    public CurrencyRestController(CurrencyService currencyService, CurrencyMapper currencyMapper, AuthenticationHelper authenticationHelper) {
        this.currencyService = currencyService;
        this.currencyMapper = currencyMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<Currency> getAllCurrencies(@RequestHeader(name = "Authentication")
                                               String authentication){
        try {
            User user = authenticationHelper.tryGetUser(authentication);
            return currencyService.getAll();
        }  catch (AuthenticationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Currency getCurrency(@RequestHeader(name = "Authentication")
                                    String authentication, @PathVariable int id){
        try {
            User user = authenticationHelper.tryGetUser(authentication);
            return currencyService.getById(id);
        }  catch (AuthenticationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public void createCurrency(@RequestHeader(name = "Authentication")
                                   String authentication, @Valid @RequestBody CurrencyDto currencyDto){
        try {
            User user = authenticationHelper.tryGetUser(authentication);
            Currency currency = currencyMapper.fromDto(currencyDto);
            currencyService.create(currency,user);
        }  catch (AuthenticationException | AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public void updateCurrency(@RequestHeader(name = "Authentication")
                                   String authentication, @Valid @RequestBody CurrencyDto currencyDto
            ,@PathVariable int id){
        try {
            User user = authenticationHelper.tryGetUser(authentication);
            Currency currency = currencyMapper.fromDto(id,currencyDto);
            currencyService.update(currency,user);
        }  catch (AuthenticationException | AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteCurrency(@RequestHeader(name = "Authentication")
                                   String authentication, @PathVariable int id){
        try {
            User user = authenticationHelper.tryGetUser(authentication);
            currencyService.delete(id,user);
        }  catch (AuthenticationException | AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
