package com.telerikacademy.web.virtualwallet.services;

import com.telerikacademy.web.virtualwallet.models.ProfilePhoto;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.repositories.contracts.ProfilePhotoRepository;
import com.telerikacademy.web.virtualwallet.repositories.contracts.UserRepository;
import com.telerikacademy.web.virtualwallet.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    public static final String BLOCK_UNBLOCK_PERMISSIONS_ERR = "Only admins are allowed to block or unblock users.";
    private final UserRepository userRepository;

    private final ProfilePhotoRepository profilePhotoRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ProfilePhotoRepository profilePhotoRepository) {
        this.userRepository = userRepository;
        this.profilePhotoRepository = profilePhotoRepository;
    }

    @Override
    public User getById(int id) {
        return userRepository.getByField("id", id);

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
        userRepository.create(user);
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
    public void block(int id, User user) {
        User userToBeBlocked = userRepository.getById(id);
        if (!user.isAdmin()){
            throw new RuntimeException(BLOCK_UNBLOCK_PERMISSIONS_ERR);
        }
        userToBeBlocked.setBlocked(true);
        userRepository.update(user);
    }

    @Override
    public void unblock(int id, User user) {
        User userToBeUnblocked = userRepository.getById(id);
        if (!user.isAdmin()){
            throw new RuntimeException(BLOCK_UNBLOCK_PERMISSIONS_ERR);
        }
        userToBeUnblocked.setBlocked(false);
        userRepository.update(user);

    }

    @Override
    public void uploadProfilePhoto(ProfilePhoto profilePhoto, User user) {
        if (user.getProfilePhoto()!=null){
            profilePhotoRepository.delete(user.getProfilePhoto().getProfilePhotoId());
        }
        profilePhotoRepository.create(profilePhoto);
    }

    @Override
    public void updateProfilePhoto(ProfilePhoto profilePhoto) {
        profilePhotoRepository.update(profilePhoto);
    }


}
