package com.telerikacademy.web.virtualwallet.services;

import com.telerikacademy.web.virtualwallet.exceptions.*;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.wallets.JoinWallet;
import com.telerikacademy.web.virtualwallet.repositories.contracts.JoinWalletRepository;
import com.telerikacademy.web.virtualwallet.repositories.contracts.UserRepository;
import com.telerikacademy.web.virtualwallet.repositories.contracts.WalletRepository;
import com.telerikacademy.web.virtualwallet.services.contracts.CurrencyService;
import com.telerikacademy.web.virtualwallet.services.contracts.JoinWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JoinWalletServiceImpl implements JoinWalletService {

    public static final String NOT_OWNER_ERR = "Only owner can add/remove users from join wallets";

    public static final String UNAUTHORIZED = "Unauthorized";

    private final JoinWalletRepository joinWalletRepository;

    private final UserRepository userRepository;

    @Autowired
    public JoinWalletServiceImpl(JoinWalletRepository joinWalletRepository, UserRepository userRepository) {
        this.joinWalletRepository = joinWalletRepository;
        this.userRepository = userRepository;
    }


    @Override
    public JoinWallet get(int id,User user) {
        JoinWallet joinWallet = joinWalletRepository.getById(id);
        checkAllUsersPermission(user, joinWallet);
        return joinWalletRepository.getById(id);
    }

    @Override
    public List<JoinWallet> getAll() {
        return joinWalletRepository.getAll();
    }

    public List<JoinWallet>getAllByUser(User user){
        List<JoinWallet> allJoinWallets = new ArrayList<>(joinWalletRepository.getAllByUser(user));
        allJoinWallets.addAll(user.getJoinWallets());
        return allJoinWallets;
    }

    @Override
    public void create(JoinWallet wallet, User user) {
        checkIsDuplicated(wallet, user);
        joinWalletRepository.create(wallet);
    }

    @Override
    public void update(JoinWallet wallet,User user, int id) {
        checkIsOwner(user,wallet);
        checkIsDuplicated(wallet,user,id);
        joinWalletRepository.update(wallet);
    }

    @Override
    public void delete(int id) {
        joinWalletRepository.delete(id);
    }

    @Override
    public List<User> getAllUsers(int walletId) {
        JoinWallet joinWallet = joinWalletRepository.getById(walletId);
        List<User> walletUsers = new ArrayList<>();
        walletUsers.add(joinWallet.getHolder());
        walletUsers.addAll(joinWallet.getUsers());
        return walletUsers;
    }

    @Override
    public void addUser(int walletId,String user, User owner) {
        JoinWallet joinWallet = joinWalletRepository.getById(walletId);
        checkIsOwner(owner, joinWallet);
        User userToAdd = userRepository.searchByAnyMatch(user);
        joinWallet.getUsers().add(userToAdd);
        joinWalletRepository.update(joinWallet);
    }

    @Override
    public void removeOtherUser(int walletId, String user, User owner) {
        JoinWallet joinWallet = joinWalletRepository.getById(walletId);
        checkIsOwner(owner, joinWallet);
        User userToRemove = userRepository.searchByAnyMatch(user);
        joinWallet.getUsers().remove(userToRemove);
        joinWalletRepository.update(joinWallet);
    }

    @Override
    public void removeWallet(int walletId, User user) {
        JoinWallet joinWallet = get(walletId,user);
        checkAllUsersPermission(user,joinWallet);
        joinWallet.getUsers().remove(user);
        joinWalletRepository.update(joinWallet);
    }

    private static void checkIsOwner(User owner, JoinWallet joinWallet) {
        if (joinWallet.getHolder().getId() != owner.getId()){
            throw new AuthorizationException(NOT_OWNER_ERR);
        }
    }

    private static void checkAllUsersPermission(User user, JoinWallet joinWallet) {
        if (joinWallet.getHolder().getId() != user.getId() && !joinWallet.getUsers().contains(user)){
            throw new AuthorizationException(UNAUTHORIZED);
        }
    }

    private void checkIsDuplicated(JoinWallet wallet, User user) {
        if (wallet.getName().equalsIgnoreCase("Personal")){
            throw new EntityDuplicateException("Wallet","name", wallet.getName());
        }
        try {
            joinWalletRepository.getByUserAndName(user, wallet.getName());
            throw new EntityDuplicateException("Wallet","name", wallet.getName());
        } catch (EntityNotFoundException ignored){}
    }

    private void checkIsDuplicated(JoinWallet wallet, User user,int id) {
        if (wallet.getName().equalsIgnoreCase("Personal")){
            throw new EntityDuplicateException("Wallet","name", wallet.getName());
        }
        try {
            JoinWallet duplicateWallet = joinWalletRepository.getByUserAndName(user, wallet.getName());
            if (duplicateWallet.getId() != id) {
                throw new EntityDuplicateException("Wallet", "name", wallet.getName());
            }
        } catch (EntityNotFoundException ignored){}
    }
}
