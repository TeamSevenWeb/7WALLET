package com.telerikacademy.web.virtualwallet.controllers.mvc;

import com.telerikacademy.web.virtualwallet.exceptions.*;
import com.telerikacademy.web.virtualwallet.models.Card;
import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.Transfer;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.CardDto;
import com.telerikacademy.web.virtualwallet.models.dtos.TransactionDto;
import com.telerikacademy.web.virtualwallet.models.dtos.TransferDto;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;
import com.telerikacademy.web.virtualwallet.services.contracts.CardService;
import com.telerikacademy.web.virtualwallet.services.contracts.TransactionService;
import com.telerikacademy.web.virtualwallet.services.contracts.UserService;
import com.telerikacademy.web.virtualwallet.services.contracts.WalletService;
import com.telerikacademy.web.virtualwallet.utils.AuthenticationHelper;
import com.telerikacademy.web.virtualwallet.utils.TransactionMapper;
import com.telerikacademy.web.virtualwallet.utils.TransferMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/wallet")
public class WalletMVC {

    private final WalletService walletService;

    private final UserService userService;

    private final AuthenticationHelper authenticationHelper;

    private final TransactionMapper transactionMapper;

    private final TransferMapper transferMapper;

    private final CardService cardService;

    private final TransactionService transactionService;

    private static User user;


    public WalletMVC(WalletService walletService, UserService userService, AuthenticationHelper authenticationHelper, TransactionMapper transactionMapper, TransferMapper transferMapper, CardService cardService, TransactionService transactionService) {
        this.walletService = walletService;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.transactionMapper = transactionMapper;
        this.transferMapper = transferMapper;
        this.cardService = cardService;
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

    @ModelAttribute("userCards")
    public List<Card> userCards(Model model) {
        User user2 = userService.getById(1);
        List<Card> cards = cardService.getUsersCards(user2);
        model.addAttribute("userCards", cards);
        return cards;
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
    public String showNewPostPage(Model model, HttpSession session){
        model.addAttribute("transaction",new TransactionDto());
        try {
//            user =  authenticationHelper.tryGetCurrentUser(session);
            user = userService.getById(1);
            return "NewTransactionView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/transactions/new")
    public String createTransaction(@Valid @ModelAttribute("transaction") TransactionDto transactionDto, BindingResult errors, HttpSession session, Model model) {
        if (errors.hasErrors()) {
            return "NewTransactionView";
        }
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
            Transaction transaction = transactionMapper.fromDto(user, transactionDto);
            transactionService.create(transaction,user.getWallet(),transaction.getReceiver().getWallet());
            return "redirect:/wallet/transactions";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        } catch (FundsSupplyException e) {
            errors.rejectValue("amount", "insufficient.funds", e.getMessage());
            return "NewTransactionView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

        @GetMapping("/fund")
        public String showWalletFundPage(Model model){
            model.addAttribute("transfer",new TransferDto());
            return "FundWalletView";
        }

        @PostMapping("/fund")
        public String fundWallet(@Valid @ModelAttribute("transfer") TransferDto transferDto
                ,BindingResult errors, HttpSession session) {
            if (errors.hasErrors()) {
                return "FundWalletView";
            }
        try {
            User user2 = userService.getById(1);
            Transfer ingoing = transferMapper.ingoingFromDto(user2,transferDto);
                walletService.transfer(ingoing);
                return "redirect:/";
            }  catch (AuthenticationException | AuthorizationException e){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
            } catch (TransferFailedException e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        }

    @GetMapping("/withdraw")
    public String showWalletWithdrawPage(Model model){
        model.addAttribute("transfer",new TransferDto());
        return "WithdrawFromWalletView";
    }

    @PostMapping("/withdraw")
    public String withdrawFromWallet(@Valid @ModelAttribute("transfer") TransferDto transferDto,BindingResult errors, HttpSession session, Model model) {
        if (errors.hasErrors()) {
            return "WithdrawFromWalletView";
        }
        try {
            User user2 = userService.getById(1);
            Transfer outgoing = transferMapper.outgoingFromDto(user2,transferDto);
            walletService.transfer(outgoing);
            return "redirect:/";
        }  catch (AuthenticationException | AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (TransferFailedException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    }
