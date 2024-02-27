package com.telerikacademy.web.virtualwallet.services.contracts;

import com.telerikacademy.web.virtualwallet.models.ProfilePhoto;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.utils.UserRole;

import java.util.List;

public interface UserService {

    User getById(int id);

    User getByUsername(String username);

    User getByFirstName(String firstName);

    User getByLastName(String firstName);

    User getByEmail(String email);

    User getByPhoneNumber(String phoneNumber);

    List<User> getAll();

    void create(User user);

    void update(User user);

    void delete (User user);

    void block(int id, User user);

    void unblock(int id, User user);

    void makeAdmin(int id, User user);
    void removeAdmin(int id, User user);

    void uploadProfilePhoto(ProfilePhoto profilePhoto, User user);

    void updateProfilePhoto(ProfilePhoto profilePhoto);



}
