package com.telerikacademy.web.virtualwallet.controllers.mvc;

import com.telerikacademy.web.virtualwallet.exceptions.*;
import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.TransactionDto;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;
import com.telerikacademy.web.virtualwallet.services.contracts.TransactionService;
import com.telerikacademy.web.virtualwallet.services.contracts.WalletService;
import com.telerikacademy.web.virtualwallet.utils.AuthenticationHelper;
import com.telerikacademy.web.virtualwallet.utils.TransactionMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/wallet")
public class WalletMVC {

    private final WalletService walletService;

    private final AuthenticationHelper authenticationHelper;

    private final TransactionMapper transactionMapper;

    private final TransactionService transactionService;

    private static User user;


    public WalletMVC(WalletService walletService, AuthenticationHelper authenticationHelper, TransactionMapper transactionMapper, TransactionService transactionService) {
        this.walletService = walletService;
        this.authenticationHelper = authenticationHelper;
        this.transactionMapper = transactionMapper;
        this.transactionService = transactionService;
    }
    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }


    @GetMapping("/{id}")
    public String showSingleWallet(@PathVariable int id, Model model, HttpSession session) {
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
            Wallet wallet = walletService.get(id, user);
            model.addAttribute("wallet", wallet);
            return "PostView";
        } catch (AuthenticationException e){
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }catch (AuthorizationException e){
            model.addAttribute("statusCode",HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error",e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/transactions/new")
    public String showNewPostPage(Model model,HttpSession session){
        model.addAttribute("transaction",new TransactionDto());
        return "NewTransactionView";
    }

    @PostMapping("/transactions/new")
    public String createTransaction(@Valid @ModelAttribute("transaction") TransactionDto transactionDto, BindingResult errors, HttpSession session, Model model){
        if(errors.hasErrors()){
            return "NewTransactionView";
        }
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
            Transaction ingoing = transactionMapper.fromDto(user,transactionDto);
            Transaction outgoing = new Transaction(ingoing);

            transactionService.create(ingoing,outgoing);
            return "redirect:/wallet/transactions/"+outgoing.getId();
        }catch (AuthenticationException e){
            return "redirect:/auth/login";
        }catch (FundsSupplyException e){
            errors.rejectValue("amount","insufficient.funds",e.getMessage());
            return "NewTransactionView";
        }
        catch (EntityNotFoundException e){
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }
}
