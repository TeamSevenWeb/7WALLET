package com.telerikacademy.web.virtualwallet.services;

import com.telerikacademy.web.virtualwallet.exceptions.EntityDuplicateException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtualwallet.models.ProfilePhoto;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;
import com.telerikacademy.web.virtualwallet.repositories.contracts.*;
import com.telerikacademy.web.virtualwallet.services.contracts.CurrencyService;
import com.telerikacademy.web.virtualwallet.services.contracts.UserService;
import com.telerikacademy.web.virtualwallet.services.contracts.WalletService;
import com.telerikacademy.web.virtualwallet.utils.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    public static final String BLOCK_UNBLOCK_PERMISSIONS_ERR = "Only admins are allowed to block or unblock users.";
    private final UserRepository userRepository;
    private final ProfilePhotoRepository profilePhotoRepository;
    private final RoleRepository roleRepository;
    private final WalletService walletService;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, ProfilePhotoRepository profilePhotoRepository, RoleRepository roleRepository, WalletService walletService) {
        this.userRepository = userRepository;
        this.profilePhotoRepository = profilePhotoRepository;
        this.roleRepository = roleRepository;
        this.walletService = walletService;
    }

    @Override
    public User getById(int id) {
        return userRepository.getById(id);

    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByField("username", username);

    }

    @Override
    public User getByFirstName(String firstName) {
        return userRepository.getByField("firstName", firstName);
    }

    @Override
    public User getByLastName(String lastName) {
        return userRepository.getByField("lastName", lastName);

    }

    @Override
    public User getByEmail(String email) {
        return userRepository.getByField("email", email);
    }

    @Override
    public User getByPhoneNumber(String phoneNumber) {
        return userRepository.getByField("phoneNumber", phoneNumber);
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public void create(User user) {
        try {
            getByUsername(user.getUsername());
            throw new EntityDuplicateException("User","Username",user.getUsername());
        } catch (EntityNotFoundException ignored) {

        }
        try {
            getByEmail(user.getEmail());
            throw new EntityDuplicateException("User","Email",user.getEmail());
        } catch (EntityNotFoundException ignored) {
        }

        userRepository.create(user);
        walletService.create(walletService.createDefaultWallet(user));
    }

    @Override
    public void update(User user) {
        userRepository.update(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user.getId());
    }

    @Override
    public void block(int userId, User admin) {
        User userToBeBlocked = userRepository.getById(userId);
        userToBeBlocked.getUserRoles().add(roleRepository.getByField("roleType", UserRole.blocked.toString()));
        userRepository.update(userToBeBlocked);

    }

    @Override
    public void unblock(int userId, User admin) {
        User userToBeUnBlocked = userRepository.getById(userId);
        userToBeUnBlocked.getUserRoles().remove(roleRepository.getByField("roleType", UserRole.blocked.toString()));
        userRepository.update(userToBeUnBlocked);
    }

    @Override
    public void uploadProfilePhoto(ProfilePhoto profilePhoto, User user) {
        if (user.getProfilePhoto() != null) {
            profilePhotoRepository.delete(user.getProfilePhoto().getProfilePhotoId());
        }
        profilePhoto.setUser(user);
        profilePhotoRepository.create(profilePhoto);
    }

    @Override
    public void updateProfilePhoto(ProfilePhoto profilePhoto) {
        profilePhotoRepository.update(profilePhoto);
    }


}
