package com.telerikacademy.web.virtualwallet.services;

import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.repositories.contracts.UserRepository;
import com.telerikacademy.web.virtualwallet.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    }

    @Override
    public void unblock(int id, User user) {

    }


}
