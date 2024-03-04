package com.telerikacademy.web.virtualwallet.models.wallets;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("3")
public class SavingWallet extends Wallet{


}
