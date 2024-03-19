package com.telerikacademy.web.virtualwallet.controllers.mvc;

import com.telerikacademy.web.virtualwallet.exceptions.*;
import com.telerikacademy.web.virtualwallet.filters.TransactionFilterOptions;
import com.telerikacademy.web.virtualwallet.filters.UserFilterOptions;
import com.telerikacademy.web.virtualwallet.filters.dtos.TransactionFilterDto;
import com.telerikacademy.web.virtualwallet.filters.dtos.UserFilterOptionsDto;
import com.telerikacademy.web.virtualwallet.models.ProfilePhoto;
import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.UserPasswordDto;
import com.telerikacademy.web.virtualwallet.models.dtos.UserProfilePhotoDto;
import com.telerikacademy.web.virtualwallet.models.dtos.UserUpdateDto;
import com.telerikacademy.web.virtualwallet.models.wallets.JoinWallet;
import com.telerikacademy.web.virtualwallet.services.contracts.JoinWalletService;
import com.telerikacademy.web.virtualwallet.services.contracts.TransactionService;
import com.telerikacademy.web.virtualwallet.services.contracts.UserService;
import com.telerikacademy.web.virtualwallet.utils.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserMvcController {

    public static final String AUTHORIZATION_ERR = "You are not authorized to perform this action.";
    private final ProfilePhotoMapper profilePhotoMapper;
    private final CloudinaryHelper cloudinaryHelper;

    private final TransactionService transactionService;

    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;

    private final JoinWalletService joinWalletService;

    private final UserUpdateMapper userUpdateMapper;
    private final UserPasswordMapper userPasswordMapper;

    private final UserFilterOptionsMapper userFilterOptionsMapper;

    public UserMvcController(ProfilePhotoMapper profilePhotoMapper, CloudinaryHelper cloudinaryHelper, TransactionService transactionService, AuthenticationHelper authenticationHelper, UserService userService, JoinWalletService joinWalletService, UserUpdateMapper userUpdateMapper, UserPasswordMapper userPasswordMapper, UserFilterOptionsMapper userFilterOptionsMapper) {
        this.profilePhotoMapper = profilePhotoMapper;
        this.cloudinaryHelper = cloudinaryHelper;
        this.transactionService = transactionService;
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
        this.joinWalletService = joinWalletService;
        this.userUpdateMapper = userUpdateMapper;
        this.userPasswordMapper = userPasswordMapper;
        this.userFilterOptionsMapper = userFilterOptionsMapper;
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
    public String showAllUsers(Model model
            , HttpSession session
            , @ModelAttribute("userFilterOptionsDto") UserFilterOptionsDto filterDto
            , RedirectAttributes redirectAttributes) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            UserFilterOptions userFilterOptions = userFilterOptionsMapper.fromDto(filterDto);
            model.addAttribute("allUsers", userService.getAll(userFilterOptions, user));
            return "UsersView";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }
        catch (AuthorizationException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/{username}")
    public String showUserPage(@PathVariable String username, Model model, HttpSession session) {
        try {
            User currentUser = authenticationHelper.tryGetCurrentUser(session);
            User viewedUser = userService.getByUsername(username);
            boolean isBlocked = userService.isBlocked(viewedUser);
            List<JoinWallet> userJoinWallets = joinWalletService.getAllByUser(viewedUser);
            model.addAttribute("isBlocked", isBlocked);
            model.addAttribute("viewedUser", viewedUser);
            model.addAttribute("userJoinWallets",userJoinWallets);
            model.addAttribute("currentUser", currentUser);
            return "UserView";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/{username}/update")
    public String showUpdatePage(@PathVariable String username, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            User loggedInUser = authenticationHelper.tryGetCurrentUser(session);
            User userToBeUpdated = userService.getByUsername(username);
            if (!loggedInUser.equals(userToBeUpdated)){
                throw new AuthorizationException(AUTHORIZATION_ERR);
            }
            UserUpdateDto userDto = userUpdateMapper.toDto(userToBeUpdated);
            model.addAttribute("userUpdateDto", userDto);
            return "UserUpdateView";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }
        catch (AuthorizationException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/{username}/changeProfilePhoto")
    public String showChangeProfilePhoto(@PathVariable String username, Model model, HttpSession session,RedirectAttributes redirectAttributes) {
        try {
            User loggedInUser = authenticationHelper.tryGetCurrentUser(session);
            User userToBeUpdated = userService.getByUsername(username);
            if (!loggedInUser.equals(userToBeUpdated)){
                throw new AuthorizationException(AUTHORIZATION_ERR);
            }
            UserProfilePhotoDto userProfilePhotoDto = new UserProfilePhotoDto();
            model.addAttribute("userProfilePhotoDto", userProfilePhotoDto);
            return "UserProfilePhotoUpdateView";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }
        catch (AuthorizationException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/{username}/changePassword")
    public String showChangePassword(@PathVariable String username, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            User loggedInUser = authenticationHelper.tryGetCurrentUser(session);
            User userToBeUpdated = userService.getByUsername(username);
            if (!loggedInUser.equals(userToBeUpdated)){
                throw new AuthorizationException(AUTHORIZATION_ERR);
            }
            UserPasswordDto userPasswordDto = new UserPasswordDto();
            model.addAttribute("userPasswordDto", userPasswordDto);
            return "UserPasswordUpdateView";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }
        catch (AuthorizationException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }

    @PostMapping("/{username}/update")
    public String handleUpdate(@PathVariable String username
            , @Valid @ModelAttribute("userUpdateDto") UserUpdateDto userDto
            , BindingResult errors
            , HttpSession session

    ) {
        if (errors.hasErrors()){
            return "UserUpdateView";
        }
        User loggedInUser = authenticationHelper.tryGetCurrentUser(session);
        User viewedUser = userService.getByUsername(username);
        User userToBeUpdated = userUpdateMapper.fromDto(viewedUser.getId(), userDto);
        userService.update(userToBeUpdated, loggedInUser);
        return "redirect:/users/{username}";
    }

    @PostMapping("/{username}/changePassword")
    public String handleChangePassword(@PathVariable String username, Model model, HttpSession session,
                                       @Valid @ModelAttribute("userPasswordDto") UserPasswordDto userPasswordDto,
                                       BindingResult errors) {
        if (!userPasswordDto.getPassword().equals(userPasswordDto.getPasswordConfirm())){
            errors.rejectValue("passwordConfirm","passwordConfirm.noMatch","Entered passwords do not match.");
        }
        if (errors.hasErrors()) {
            return "UserPasswordUpdateView";
        }
        User loggedInUser = authenticationHelper.tryGetCurrentUser(session);
        User viewedUser = userService.getByUsername(username);
        User userToBeUpdated = userPasswordMapper.fromDto(viewedUser.getId(), userPasswordDto);
        userService.update(userToBeUpdated, loggedInUser);

        return "redirect:/users/{username}";
    }

    @PostMapping("/{username}/changeProfilePhoto")
    public String handleChangeProfilePhoto(@PathVariable String username, Model model, HttpSession session,
                                           @ModelAttribute("userProfilePhotoDto") UserProfilePhotoDto userProfilePhotoDto,
                                           @RequestParam("avatar") MultipartFile file,
                                           BindingResult errors) throws IOException {
        try {
            User loggedInUser = authenticationHelper.tryGetCurrentUser(session);
            User viewedUser = userService.getByUsername(username);
            String url = cloudinaryHelper.cloudinaryUpload(file);
            userProfilePhotoDto.setUrl(url);
            ProfilePhoto profilePhoto = profilePhotoMapper.fromDto(viewedUser.getProfilePhoto().getProfilePhotoId(), userProfilePhotoDto);
            userService.updateProfilePhoto(profilePhoto, viewedUser, loggedInUser);
        } catch (IOException | InvalidFileException e) {
            model.addAttribute("avatarError", e.getMessage());
            return "UserProfilePhotoUpdateView";
        }
        return "redirect:/users/{username}";

    }


    @GetMapping("/transactions")
    public String get(@ModelAttribute("transactionFilterOptions") TransactionFilterDto filterDto,
                      @RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int pageSize, Model model, HttpSession session) {

        TransactionFilterOptions transactionFilterOptions = new TransactionFilterOptions(filterDto.getDate()
                ,filterDto.getSender(),filterDto.getReceiver(),filterDto.getDirection(),
                filterDto.getSortBy(),filterDto.getSortOrder());

        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            Pageable pageable = PageRequest.of(page, pageSize);
            Page<Transaction> transactions = transactionService.getAll(user, transactionFilterOptions,pageable);
            model.addAttribute("transactionFilterOptions", filterDto);
            model.addAttribute("transactions", transactions.getContent());
            model.addAttribute("currentUser", user);
            model.addAttribute("currentPage", transactions.getNumber());
            model.addAttribute("lastPage", transactions.getTotalPages() - 1);
            return "TransactionsView";
        }catch (AuthenticationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException | TransactionsNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "TransactionsView";
        }
    }

    @GetMapping("/{username}/block")
    public String block(@PathVariable String username
            , HttpSession session
            ,RedirectAttributes redirectAttributes

    ) {
        try {
            User loggedInUser = authenticationHelper.tryGetCurrentUser(session);
            User viewedUser = userService.getByUsername(username);
            userService.block(viewedUser.getId(),loggedInUser);
            return "redirect:/users";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }
        catch (AuthorizationException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/{username}/unblock")
    public String unblock(@PathVariable String username
            , HttpSession session
            , RedirectAttributes redirectAttributes

    ) {
        try {
            User loggedInUser = authenticationHelper.tryGetCurrentUser(session);
            User viewedUser = userService.getByUsername(username);
            userService.unblock(viewedUser.getId(),loggedInUser);
            return "redirect:/users";
        } catch (AuthenticationException e) {
            return "redirect:/auth/login";
        }
        catch (AuthorizationException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }
}
