package com.telerikacademy.web.virtualwallet.utils;

import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.wallets.JoinWallet;
import com.telerikacademy.web.virtualwallet.services.contracts.CurrencyService;
import com.telerikacademy.web.virtualwallet.services.contracts.JoinWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JoinWalletMapper {

    JoinWalletService joinWalletService;

    CurrencyService currencyService;

    @Autowired
    public JoinWalletMapper(JoinWalletService joinWalletService,CurrencyService currencyService) {
        this.joinWalletService = joinWalletService;
        this.currencyService = currencyService;
    }

    public JoinWallet fromDto(User user, String name, int id){
        JoinWallet wallet = fromDto(user,name);
        JoinWallet oldWallet = joinWalletService.get(id,user);
        wallet.setId(id);
        wallet.setUsers(oldWallet.getUsers());
        return wallet;
    }

    public JoinWallet fromDto(User user, String name){
        JoinWallet wallet = new JoinWallet();
        wallet.setName(name);
        wallet.setHolder(user);
        wallet.setHoldings(0.0);
        wallet.setCurrency(currencyService.getById(1));
        return wallet;
    }
}
