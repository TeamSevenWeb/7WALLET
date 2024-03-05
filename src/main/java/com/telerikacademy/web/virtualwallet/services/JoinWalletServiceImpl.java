package com.telerikacademy.web.virtualwallet.services;

import com.telerikacademy.web.virtualwallet.exceptions.AuthorizationException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityDuplicateException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtualwallet.exceptions.FundsSupplyException;
import com.telerikacademy.web.virtualwallet.models.Currency;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.wallets.JoinWallet;
import com.telerikacademy.web.virtualwallet.repositories.contracts.JoinWalletRepository;
import com.telerikacademy.web.virtualwallet.repositories.contracts.UserRepository;
import com.telerikacademy.web.virtualwallet.services.contracts.CurrencyService;
import com.telerikacademy.web.virtualwallet.services.contracts.JoinWalletService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
@Service
public class JoinWalletServiceImpl implements JoinWalletService {

    public static final String NOT_OWNER_ERR = "Only owner can add/remove users from join wallets";
    public static final String UNAUTHORIZED = "Unauthorized";
    private final JoinWalletRepository joinWalletRepository;

    private final UserRepository userRepository;

    private final CurrencyService currencyService;

    public JoinWalletServiceImpl(JoinWalletRepository joinWalletRepository, UserRepository userRepository, CurrencyService currencyService) {
        this.joinWalletRepository = joinWalletRepository;
        this.userRepository = userRepository;
        this.currencyService = currencyService;
    }


    @Override
    public JoinWallet get(int id,User user) {
        JoinWallet joinWallet = joinWalletRepository.getById(id);
        if (joinWallet.getHolder().getId() != user.getId() && !joinWallet.getUsers().contains(user)){
            throw new AuthorizationException(UNAUTHORIZED);
        }
        return joinWalletRepository.getById(id);
    }

    @Override
    public List<JoinWallet> getAll() {
        return joinWalletRepository.getAll();
    }

    public List<JoinWallet>getAllByUser(User user){
        return  joinWalletRepository.getAllByUser(user);
    }

    @Override
    public void create(JoinWallet wallet, User user) {
        try {
            joinWalletRepository.getByUserAndName(user,wallet.getName());
            throw new EntityDuplicateException("Wallet","name",wallet.getName());
        } catch (EntityNotFoundException ignored){}
        joinWalletRepository.create(wallet);
    }

    @Override
    public void update(JoinWallet wallet) {
        joinWalletRepository.update(wallet);
    }

    @Override
    public void delete(int id) {
        joinWalletRepository.delete(id);
    }

    @Override
    public void addFunds(int walletId, double funds) {
        JoinWallet wallet = joinWalletRepository.getById(walletId);
        wallet.setHoldings(wallet.getHoldings() + funds);
        joinWalletRepository.update(wallet);
    }

    @Override
    public void subtractFunds(int walletId, double funds) {
        JoinWallet wallet = joinWalletRepository.getById(walletId);
        if (wallet.getHoldings() < funds){
            throw new FundsSupplyException();
        }
        wallet.setHoldings(wallet.getHoldings() - funds);
        joinWalletRepository.update(wallet);
    }

    @Override
    public void changeCurrency(int walletId, Currency currency) {
        JoinWallet wallet = joinWalletRepository.getById(walletId);
        wallet.setCurrency(currency);
        joinWalletRepository.update(wallet);
    }

    @Override
    public List<User> getAllUsers(int walletId) {
        JoinWallet joinWallet = joinWalletRepository.getById(walletId);
        return joinWallet.getUsers().stream().toList();
    }

    @Override
    public void addUser(int walletId,String user, User owner) {
        JoinWallet joinWallet = joinWalletRepository.getById(walletId);
        if (joinWallet.getHolder().getId() != owner.getId()){
            throw new AuthorizationException(NOT_OWNER_ERR);
        }
        User userToAdd = userRepository.searchByAnyMatch(user);
        joinWallet.getUsers().add(userToAdd);
        joinWalletRepository.update(joinWallet);
    }

    @Override
    public void removeUser(int walletId, String user, User owner) {
        JoinWallet joinWallet = joinWalletRepository.getById(walletId);
        if (joinWallet.getHolder().getId() != owner.getId()){
            throw new AuthorizationException(NOT_OWNER_ERR);
        }
        User userToRemove = userRepository.searchByAnyMatch(user);
        joinWallet.getUsers().remove(userToRemove);
        joinWalletRepository.update(joinWallet);
    }

    public JoinWallet createJoinWallet(User user, String name){
        JoinWallet wallet = new JoinWallet();
        wallet.setName(name);
        wallet.setHolder(user);
        wallet.setHoldings(0.0);
        wallet.setCurrency(currencyService.getById(1));
        return wallet;
    }
}
