package com.telerikacademy.web.virtualwallet.controllers.mvc;

import com.telerikacademy.web.virtualwallet.exceptions.*;
import com.telerikacademy.web.virtualwallet.models.*;
import com.telerikacademy.web.virtualwallet.models.dtos.ChangeCurrencyDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/wallet")
public class WalletMvcController {

    private final WalletService walletService;

    private final JoinWalletService joinWalletService;

    private final UserService userService;

    private final CurrencyService currencyService;

    private final TransactionService transactionService;

    private final VerificationService verificationService;

    private final AuthenticationHelper authenticationHelper;

    private final TransactionMapper transactionMapper;

    private final TransferMapper transferMapper;

    public WalletMvcController(WalletService walletService, JoinWalletService joinWalletService, UserService userService
            , CurrencyService currencyService, AuthenticationHelper authenticationHelper, TransactionMapper transactionMapper
            , TransferMapper transferMapper, TransactionService transactionService
            , VerificationService verificationService) {
        this.walletService = walletService;
        this.joinWalletService = joinWalletService;
        this.userService = userService;
        this.currencyService = currencyService;
        this.authenticationHelper = authenticationHelper;
        this.transactionMapper = transactionMapper;
        this.transferMapper = transferMapper;
        this.transactionService = transactionService;
        this.verificationService = verificationService;
    }
    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }


    @GetMapping
    public String showPersonalWallet(Model model, HttpSession session){
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
            int userWalletsCount = joinWalletService.getAllByUser(user).size() + 1;
            model.addAttribute("userWalletsCount", userWalletsCount);
            model.addAttribute("wallet", user.getWallet());
            model.addAttribute("currency", new ChangeCurrencyDto());
            model.addAttribute("allCurrencies", currencyService.getAll());
            return "WalletView";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }
    }


    @GetMapping("/transactions/new")
    public String showNewPostPage(Model model, HttpSession session){
        model.addAttribute("transaction",new TransactionDto());
        try {
            User user =  authenticationHelper.tryGetCurrentUser(session);
            return "NewTransactionView";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/transactions/new")
    public String createTransaction(@Valid @ModelAttribute("transaction") TransactionDto transactionDto, BindingResult errors, HttpSession session, Model model,RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            return "NewTransactionView";
        }
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            Transaction transaction = transactionMapper.fromDto(user, transactionDto);
            transactionService.create(transaction,user.getWallet(),transaction.getReceiver().getWallet());
            return "redirect:/users/transactions";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        } catch (FundsSupplyException e) {
            errors.rejectValue("amount", "insufficient.funds", e.getMessage());
            return "NewTransactionView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (TransactionConfirmationException | TransactionExpiredException e) {
            redirectAttributes.addFlashAttribute("success", e.getMessage());
            return "redirect:/users/transactions";
        }

    }

    @GetMapping("/transfer/new")
    public String transferToOtherWallet(Model model, HttpSession session){
        try {
            User user =  authenticationHelper.tryGetCurrentUser(session);
            List<JoinWallet> wallets = joinWalletService.getAllByUser(user);
            model.addAttribute("transfer", new TransactionToJoinDto());
            model.addAttribute("wallets", wallets);
            return "TransactionToOtherWalletView";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/transfer/new")
    public String createTransferToOtherWallet(@Valid @ModelAttribute("transfer") TransactionToJoinDto transactionDto
            , BindingResult errors, RedirectAttributes redirectAttributes, HttpSession session, Model model) {
        if (errors.hasErrors()) {
            return "TransactionToOtherWalletView";
        }
        User user = null;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
            Transaction transaction = transactionMapper.fromDtoToJoin(user, transactionDto);
            JoinWallet walletToReceive = joinWalletService.get(transactionDto.getWalletId(), user);
            transactionService.create(transaction, user.getWallet(), walletToReceive);
            return "redirect:/users/transactions";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        } catch (FundsSupplyException e) {
            errors.rejectValue("amount", "insufficient.funds", e.getMessage());
            List<JoinWallet> wallets = joinWalletService.getAllByUser(user);
            model.addAttribute("wallets", wallets);
            return "TransactionToOtherWalletView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (TransactionConfirmationException | TransactionExpiredException e) {
            redirectAttributes.addFlashAttribute("success", e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/currency")
    public String changeCurrency(HttpSession session,Model model
            , @Valid @ModelAttribute("currency") ChangeCurrencyDto currencyDto, BindingResult errors){
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            walletService.changeCurrency(user.getWallet().getId(),currencyDto.getCurrencyId());
            return "redirect:/wallet" ;
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", (HttpStatus.UNAUTHORIZED));
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/fund")
    public String showWalletFundPage(HttpSession session, Model model){
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            model.addAttribute("currentUser", user);
            return "FundWalletView";
    }catch (AuthenticationException | AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/withdraw")
    public String showWalletWithdrawPage(HttpSession session, Model model){
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            model.addAttribute("currentUser", user);
            return "WithdrawFromWalletView";
        }catch (AuthenticationException | AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }


    @GetMapping("/transactions/verify/{verificationCode}")
    public String verifyTransaction(@PathVariable String verificationCode) {
        TransactionVerificationCodes transactionVerificationCodes = verificationService.getByCode(verificationCode);
        Wallet senderWallet = transactionVerificationCodes.getSenderWallet();
        Wallet receiverWallet = transactionVerificationCodes.getReceiverWallet();
        Transaction transaction = verificationService.verifyTransaction(verificationCode);
        transactionService.processTransaction(transaction, senderWallet
                , receiverWallet);
        return "redirect:/users/transactions";
    }

}
