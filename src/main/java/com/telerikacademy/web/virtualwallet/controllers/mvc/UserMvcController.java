package com.telerikacademy.web.virtualwallet.controllers.mvc;

import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.UserUpdateDto;
import com.telerikacademy.web.virtualwallet.services.contracts.UserService;
import com.telerikacademy.web.virtualwallet.utils.AuthenticationHelper;
import com.telerikacademy.web.virtualwallet.utils.UserUpdateMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserMvcController {

    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;
    private final UserUpdateMapper userUpdateMapper;
    public UserMvcController(AuthenticationHelper authenticationHelper, UserService userService, UserUpdateMapper userUpdateMapper) {
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;this.userUpdateMapper = userUpdateMapper;
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

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @GetMapping
    public String showAllUsers(Model model, HttpSession session) {
        model.addAttribute("allUsers", userService.getAll());
        return "UsersView";
    }

    @GetMapping("/{username}")
    public String showUserPage(@PathVariable String username, Model model, HttpSession session) {
        User user = userService.getByUsername(username);
        boolean isBlocked = userService.isBlocked(user);
        model.addAttribute("isBlocked",isBlocked);
        model.addAttribute("viewedUser", user);
        return "UserView";
    }

    @GetMapping("/{username}/update")
    public String showUpdatePage(@PathVariable String username, Model model, HttpSession session) {
        User loggedInUser = authenticationHelper.tryGetCurrentUser(session);
        User userToBeUpdated = userService.getByUsername(username);
        UserUpdateDto userDto = userUpdateMapper.toDto(userToBeUpdated);
        model.addAttribute("userUpdateDto", userDto);
        return "UserUpdateView";
    }

    @GetMapping("/{username}/changeProfilePhoto")
    public String showChangeProfilePhoto(@PathVariable String username, Model model, HttpSession session) {
        User loggedInUser = authenticationHelper.tryGetCurrentUser(session);
        User userToBeUpdated = userService.getByUsername(username);
        UserUpdateDto userDto = userUpdateMapper.toDto(userToBeUpdated);
        model.addAttribute("userUpdateDto", userDto);
            return "UserProfilePhotoUpdateView";
    }

    @GetMapping("/{username}/changePassword")
    public String showChangePassword(@PathVariable String username, Model model, HttpSession session) {
        User loggedInUser = authenticationHelper.tryGetCurrentUser(session);
        User userToBeUpdated = userService.getByUsername(username);
        UserUpdateDto userDto = userUpdateMapper.toDto(userToBeUpdated);
        model.addAttribute("userUpdateDto", userDto);
        return "UserPasswordUpdateView";
    }

    @PostMapping("/{username}/update")
    public String handleUpdate(@PathVariable String username
            , @Valid @ModelAttribute("userUpdateDto") UserUpdateDto userDto
            ,HttpSession session

            ){
        User loggedInUser = authenticationHelper.tryGetCurrentUser(session);
        User existingUser = userService.getByUsername(username);
        User userToBeUpdated = userUpdateMapper.fromDto(existingUser.getId(),userDto);
        userService.update(userToBeUpdated, loggedInUser);
        return "redirect:/users/{username}";
    }



}
