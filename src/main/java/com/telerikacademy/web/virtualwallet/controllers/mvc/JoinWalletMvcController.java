package com.telerikacademy.web.virtualwallet.controllers.mvc;

import com.telerikacademy.web.virtualwallet.exceptions.*;
import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.Transfer;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.TransactionDto;
import com.telerikacademy.web.virtualwallet.models.dtos.TransactionToJoinDto;
import com.telerikacademy.web.virtualwallet.models.dtos.TransferDto;
import com.telerikacademy.web.virtualwallet.models.wallets.JoinWallet;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;
import com.telerikacademy.web.virtualwallet.services.contracts.*;
import com.telerikacademy.web.virtualwallet.utils.AuthenticationHelper;
import com.telerikacademy.web.virtualwallet.utils.TransactionMapper;
import com.telerikacademy.web.virtualwallet.utils.TransferMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/join wallet")
public class JoinWalletMvcController {

    private final WalletService walletService;

    private final JoinWalletService joinWalletService;

    private final UserService userService;

    private final AuthenticationHelper authenticationHelper;

    private final TransactionMapper transactionMapper;

    private final TransferMapper transferMapper;

    private final CardService cardService;

    private final TransactionService transactionService;


    public JoinWalletMvcController(WalletService walletService, JoinWalletService joinWalletService, UserService userService, AuthenticationHelper authenticationHelper, TransactionMapper transactionMapper, TransferMapper transferMapper, CardService cardService, TransactionService transactionService) {
        this.walletService = walletService;
        this.joinWalletService = joinWalletService;
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


    @GetMapping("/{id}")
    public String showJoinWallet(Model model, HttpSession session,@PathVariable int id){
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
            JoinWallet wallet = joinWalletService.get(id,user);
            int userWalletsCount = joinWalletService.getAllByUser(user).size() + 1;
            model.addAttribute("userWalletsCount", userWalletsCount);
            model.addAttribute("wallet", wallet);
            model.addAttribute("userId", user.getId());
            return "JoinWalletView";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        } catch (AuthorizationException e){
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }


    @GetMapping("/{id}/transactions/new")
    public String showCreateTransaction(@PathVariable int id, Model model, HttpSession session){
        try {
            User user =  authenticationHelper.tryGetCurrentUser(session);
            JoinWallet joinWallet = joinWalletService.get(id,user);
            model.addAttribute("transaction",new TransactionDto());
            return "NewTransactionView";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }  catch (AuthorizationException e) {
            model.addAttribute("statusCode", (HttpStatus.UNAUTHORIZED));
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/transactions/new")
    public String createTransaction(@Valid @ModelAttribute("transaction") TransactionDto transactionDto
            ,BindingResult errors, HttpSession session, Model model,@PathVariable int id) {
        if (errors.hasErrors()) {
            return "NewTransactionView";
        }
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            JoinWallet joinWallet = joinWalletService.get(id,user);
            Transaction transaction = transactionMapper.fromDto(user, transactionDto);
            transactionService.create(transaction,joinWallet,transaction.getReceiver().getWallet());
            return "redirect:/users/transactions";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }  catch (AuthorizationException e) {
            model.addAttribute("statusCode", (HttpStatus.UNAUTHORIZED));
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (FundsSupplyException e) {
            errors.rejectValue("amount", "insufficient.funds", e.getMessage());
            return "NewTransactionView";
        }
    }

    @GetMapping("/{id}/transfer/new")
    public String transferToOtherWallet(@PathVariable int id, Model model, HttpSession session){
        try {
            User user =  authenticationHelper.tryGetCurrentUser(session);
            List<Wallet> walletList = getWallets(id, user);
            model.addAttribute("wallets", walletList);
            model.addAttribute("transfer", new TransactionToJoinDto());
            return "TransactionToOtherWalletView";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }  catch (AuthorizationException e) {
            model.addAttribute("statusCode", (HttpStatus.UNAUTHORIZED));
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{id}/transfer/new")
    public String createTransferToOtherWallet(@Valid @ModelAttribute("transfer") TransactionToJoinDto transactionDto
            , BindingResult errors, HttpSession session, Model model, @PathVariable int id) {
        if (errors.hasErrors()) {
            return "TransactionToOtherWalletView";
        }
        User user = null;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
            Transaction transaction = transactionMapper.fromDtoToJoin(user, transactionDto);
            JoinWallet wallet = joinWalletService.get(id,user);
            Wallet walletToReceive = findRightWallet(transactionDto.getWalletId(), user);
            transactionService.create(transaction, wallet, walletToReceive);
            return "redirect:/users/transactions";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }  catch (AuthorizationException e) {
            model.addAttribute("statusCode", (HttpStatus.UNAUTHORIZED));
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (FundsSupplyException e) {
            errors.rejectValue("amount", "insufficient.funds", e.getMessage());
            List<Wallet> walletList = getWallets(id, user);
            model.addAttribute("wallets", walletList);
            return "TransactionToOtherWalletView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    private Wallet findRightWallet(int id, User user){
        Wallet wallet;
        try {
           wallet = joinWalletService.get(id, user);
        } catch (EntityNotFoundException e){
            wallet = walletService.get(id,user);
        }
        return wallet;
    }

    private List<Wallet> getWallets(int id, User user) {
        JoinWallet wallet = joinWalletService.get(id, user);
        List<Wallet> walletList = new ArrayList<>();
        walletList.add(user.getWallet());
        List<JoinWallet> wallets = joinWalletService.getAllByUser(user);
        wallets.remove(wallet);
        walletList.addAll(wallets);
        return walletList;
    }

    @GetMapping("/{id}/fund")
    public String showWalletFundPage(Model model){
        model.addAttribute("transfer",new TransferDto());
        return "FundWalletView";
    }

    @GetMapping("/{id}/withdraw")
    public String showWalletWithdrawPage(Model model){
        model.addAttribute("transfer",new TransferDto());
        return "WithdrawFromWalletView";
    }

    @PostMapping("/{id}/withdraw")
    public String withdrawFromWallet(@Valid @ModelAttribute("transfer") TransferDto transferDto,BindingResult errors, HttpSession session, Model model) {
        if (errors.hasErrors()) {
            return "WithdrawFromWalletView";
        }
        try {
            User user2 = userService.getById(1);
            Transfer outgoing = transferMapper.outgoingFromDto(user2,transferDto);
            walletService.transfer(outgoing);
            return "redirect:/wallet";
        }  catch (AuthenticationException | AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (TransferFailedException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
