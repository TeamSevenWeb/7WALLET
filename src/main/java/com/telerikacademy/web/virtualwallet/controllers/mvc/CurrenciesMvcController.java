package com.telerikacademy.web.virtualwallet.controllers.mvc;

import com.telerikacademy.web.virtualwallet.exceptions.AuthenticationException;
import com.telerikacademy.web.virtualwallet.exceptions.AuthorizationException;
import com.telerikacademy.web.virtualwallet.models.Currency;
import com.telerikacademy.web.virtualwallet.models.Role;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.services.contracts.CurrencyService;
import com.telerikacademy.web.virtualwallet.utils.AuthenticationHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/currencies")
public class CurrenciesMvcController {

    private final CurrencyService currencyService;

    private final AuthenticationHelper authenticationHelper;

    public CurrenciesMvcController(CurrencyService currencyService, AuthenticationHelper authenticationHelper) {
        this.currencyService = currencyService;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("isAdmin")
    public boolean populateIsAdmin(HttpSession session) {
        return session.getAttribute("isAdmin") != null;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @GetMapping
    public String showAllCurrencies(Model model, HttpSession session) {
        try {
            User  user = authenticationHelper.tryGetCurrentUser(session);
            List<Currency> currencies = currencyService.getAll();
            model.addAttribute("currentUser",user);
            model.addAttribute("currencies", currencies);
            return "CurrenciesView";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }
    }
}


