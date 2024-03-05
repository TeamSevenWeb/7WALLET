package com.telerikacademy.web.virtualwallet.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.telerikacademy.web.virtualwallet.exceptions.AuthorizationException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityDuplicateException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtualwallet.models.ProfilePhoto;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.repositories.contracts.*;
import com.telerikacademy.web.virtualwallet.services.contracts.UserService;
import com.telerikacademy.web.virtualwallet.services.contracts.WalletService;
import com.telerikacademy.web.virtualwallet.utils.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private static final String MODIFY_USER_ERROR_MESSAGE = "Only admin or account holder can modify a user.";
    public static final String BLOCK_UNBLOCK_PERMISSIONS_ERR = "Only admins are allowed to block or unblock users.";
    private final UserRepository userRepository;
    private final ProfilePhotoRepository profilePhotoRepository;
    private final RoleRepository roleRepository;
    private final WalletService walletService;
    private final Cloudinary cloudinary;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, ProfilePhotoRepository profilePhotoRepository, RoleRepository roleRepository, WalletService walletService, Cloudinary cloudinary) {
        this.userRepository = userRepository;
        this.profilePhotoRepository = profilePhotoRepository;
        this.roleRepository = roleRepository;
        this.walletService = walletService;
        this.cloudinary = cloudinary;
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
        try {
            setDefaultProfilePhoto(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        userRepository.create(user);
        walletService.create(walletService.createDefaultWallet(user));
    }

    @Override
    public void update(User userToBeUpdated, User user) {
        checkAdminOrOwner(userToBeUpdated, user);
        userRepository.update(userToBeUpdated);
    }

    @Override
    public void delete(int id, User user) {
        checkAdminOrOwner(getById(id),user);
        userRepository.delete(id);
    }

    @Override
    public void block(int userId, User admin) {
        checkAdmin(admin,BLOCK_UNBLOCK_PERMISSIONS_ERR);
        User userToBeBlocked = userRepository.getById(userId);
        userToBeBlocked.getUserRoles().add(roleRepository.getByField("roleType", UserRole.blocked.toString()));
        userRepository.update(userToBeBlocked);

    }

    @Override
    public void unblock(int userId, User admin) {
        checkAdmin(admin,BLOCK_UNBLOCK_PERMISSIONS_ERR);
        User userToBeUnBlocked = userRepository.getById(userId);
        userToBeUnBlocked.getUserRoles().remove(roleRepository.getByField("roleType", UserRole.blocked.toString()));
        userRepository.update(userToBeUnBlocked);
    }

    @Override
    public void uploadProfilePhoto(ProfilePhoto profilePhoto, User userToBeUpdated, User user) {
        checkAdminOrOwner(userToBeUpdated, userToBeUpdated);
        if (userToBeUpdated.getProfilePhoto() != null) {
            profilePhotoRepository.delete(userToBeUpdated.getProfilePhoto().getProfilePhotoId());
        }
        profilePhoto.setUser(userToBeUpdated);
        profilePhotoRepository.create(profilePhoto);
    }

    @Override
    public void updateProfilePhoto(ProfilePhoto profilePhoto, User userToBeUpdated, User user) {
        checkAdminOrOwner(userToBeUpdated,user);
        profilePhotoRepository.update(profilePhoto);
    }

    @Override
    public boolean isAdmin(User user) {
        return user.getUserRoles().stream().anyMatch(r -> r.getRoleType().equals(UserRole.admin.toString()));
    }

    @Override
    public boolean isBlocked(User user) {
        return user.getUserRoles().stream().anyMatch(r -> r.getRoleType().equals(UserRole.blocked.toString()));

    }

    @Override
    public boolean isRegular(User user) {
        return user.getUserRoles().stream().anyMatch(r -> r.getRoleType().equals(UserRole.regular.toString()));

    }

    @Override
    public User searchByAnyMatch(String parameter) {
            return userRepository.searchByAnyMatch(parameter);
    }

    private void checkAdminOrOwner(User userToBeUpdated, User user) {
        if (!isAdmin(user) && userToBeUpdated.getId() != user.getId()) {
            throw new AuthorizationException(MODIFY_USER_ERROR_MESSAGE);
        }
    }

    private void checkAdmin(User user, String errorMessage){
        if (!isAdmin(user)){
            throw new AuthorizationException(errorMessage);
        }
    }

    private void setDefaultProfilePhoto(User user) throws IOException {
        ProfilePhoto userProfilePhoto = new ProfilePhoto();
        Map upload = cloudinary.uploader()
                .upload("./src/main/resources/static/images/default_profile.jpg"
                        , ObjectUtils.asMap("resource_type", "auto"));
        String url = (String) upload.get("url");
        userProfilePhoto.setProfilePhoto(url);
        userProfilePhoto.setUser(user);
        user.setProfilePhoto(userProfilePhoto);
        uploadProfilePhoto(userProfilePhoto, user, user);
    }

}
