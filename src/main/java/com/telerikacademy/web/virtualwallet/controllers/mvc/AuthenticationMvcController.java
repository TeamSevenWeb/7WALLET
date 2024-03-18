package com.telerikacademy.web.virtualwallet.controllers.mvc;

import com.mailjet.client.errors.MailjetException;
import com.telerikacademy.web.virtualwallet.exceptions.AuthenticationException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityDuplicateException;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.LoginDto;
import com.telerikacademy.web.virtualwallet.models.dtos.RegisterDto;
import com.telerikacademy.web.virtualwallet.services.contracts.VerificationService;
import com.telerikacademy.web.virtualwallet.services.contracts.UserService;
import com.telerikacademy.web.virtualwallet.utils.AuthenticationHelper;
import com.telerikacademy.web.virtualwallet.utils.UserRegisterMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/auth")
public class AuthenticationMvcController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    private final UserRegisterMapper userRegisterMapper;

    private final VerificationService verificationService;

    @Autowired
    public AuthenticationMvcController(UserService userService,
                                       AuthenticationHelper authenticationHelper,
                                        UserRegisterMapper userRegisterMapper,  VerificationService verificationService) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userRegisterMapper = userRegisterMapper;
        this.verificationService = verificationService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("isBlocked")
    public boolean populateIsBlocked(HttpSession session) {
        return session.getAttribute("isBlocked") != null;
    }

    @ModelAttribute("isAdmin")
    public boolean populateIsAdmin(HttpSession session) {
        return session.getAttribute("isAdmin") != null;
    }

    @ModelAttribute("isRegular")
    public boolean populateIsRegular(HttpSession session) {
        return session.getAttribute("isRegular") != null;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("login", new LoginDto());
        return "LoginView";
    }

    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") LoginDto login,
                              BindingResult bindingResult,
                              HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "LoginView";
        }

        try {
            User user = authenticationHelper.verifyAuthentication(login.getUsername(), login.getPassword());
            session.setAttribute("currentUser", login.getUsername());
            session.setAttribute("isAdmin",userService.isAdmin(user));
            session.setAttribute("isBlocked", userService.isBlocked(user));
            session.setAttribute("isRegular", userService.isRegular(user));
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            return "redirect:/";
        } catch (AuthenticationException e) {
            bindingResult.rejectValue("username", "auth_error", e.getMessage());
            return "LoginView";
        }
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.removeAttribute("currentUser");
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("register", new RegisterDto());
        return "RegisterView";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("register") RegisterDto register,
                                 BindingResult bindingResult,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "RegisterView";
        }

        if (!register.getPassword().equals(register.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "password_error", "Password confirmation should match password.");
            return "RegisterView";
        }

        try {
            User user = userRegisterMapper.fromDto(register);
            userService.create(user);
            verificationService.sendUserCode(user);
            redirectAttributes.addFlashAttribute("error", "Please check your inbox to confirm your account!");
            session.setAttribute("isAdmin", false);

            return "redirect:/";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("username", "username_error", e.getMessage());
            return "RegisterView";
        } catch (MailjetException e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping("/verify/{verificationCode}")
    public String verifyRegistration(@PathVariable String verificationCode, RedirectAttributes redirectAttributes){
        verificationService.verifyUser(verificationCode);
        redirectAttributes.addFlashAttribute("success", "Successfully confirmed account.");
        return "redirect:/";
    }
}
