package com.telerikacademy.web.virtualwallet.controllers.mvc;

import com.telerikacademy.web.virtualwallet.exceptions.AuthenticationException;
import com.telerikacademy.web.virtualwallet.exceptions.AuthorizationException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;
import com.telerikacademy.web.virtualwallet.services.contracts.WalletService;
import com.telerikacademy.web.virtualwallet.utils.AuthenticationHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequestMapping("/api/wallet")
public class WalletMVC {

    private final WalletService walletService;

    private final AuthenticationHelper authenticationHelper;

    public WalletMVC(WalletService walletService, AuthenticationHelper authenticationHelper) {
        this.walletService = walletService;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @GetMapping("/{id}")
    public String showSingleWallet(@PathVariable int id, Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
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
}
