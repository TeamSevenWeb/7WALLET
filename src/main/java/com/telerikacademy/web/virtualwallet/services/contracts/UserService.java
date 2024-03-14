package com.telerikacademy.web.virtualwallet.services.contracts;

import com.telerikacademy.web.virtualwallet.filters.UserFilterOptions;
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

    List<User> getAll(UserFilterOptions userFilterOptions,User user);

    void create(User user);

    void update(User userToBeUpdated, User user);

    void delete (int id, User user);

    void block(int id, User user);

    void unblock(int id, User user);

    void uploadProfilePhoto(ProfilePhoto profilePhoto, User userToBeUpdated, User user);

    void updateProfilePhoto(ProfilePhoto profilePhoto, User userToBeUpdated, User user);

    boolean isAdmin(User user);
    boolean isBlocked(User user);
    boolean isRegular(User user);

    User searchByAnyMatch(String parameter);
}
