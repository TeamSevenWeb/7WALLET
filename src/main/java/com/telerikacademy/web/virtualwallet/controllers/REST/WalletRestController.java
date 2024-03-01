package com.telerikacademy.web.virtualwallet.controllers.REST;

import com.telerikacademy.web.virtualwallet.services.contracts.WalletService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/wallet")
public class WalletRestController {

    private final WalletService walletService;

    public WalletRestController(WalletService walletService) {
        this.walletService = walletService;
    }
}
