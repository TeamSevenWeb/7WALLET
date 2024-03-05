package com.telerikacademy.web.virtualwallet.utils;

import com.telerikacademy.web.virtualwallet.exceptions.AuthorizationException;
import com.telerikacademy.web.virtualwallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtualwallet.models.Card;
import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.Transfer;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.TransactionDto;
import com.telerikacademy.web.virtualwallet.models.dtos.TransferDto;
import com.telerikacademy.web.virtualwallet.models.wallets.Wallet;
import com.telerikacademy.web.virtualwallet.repositories.contracts.TransactionRepository;
import com.telerikacademy.web.virtualwallet.repositories.contracts.UserRepository;
import com.telerikacademy.web.virtualwallet.repositories.contracts.WalletRepository;
import com.telerikacademy.web.virtualwallet.services.contracts.CardService;
import com.telerikacademy.web.virtualwallet.services.contracts.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TransferMapper {
    private final WalletRepository walletRepository;

    private final WalletService walletService;
    private final UserRepository userRepository;
    private final CardService cardService;

    private final CardMapper cardMapper;

    @Autowired
    public TransferMapper(WalletRepository walletRepository, WalletService walletService, UserRepository userRepository, CardService cardService, CardMapper cardMapper) {
        this.walletRepository = walletRepository;
        this.walletService = walletService;
        this.userRepository = userRepository;
        this.cardService = cardService;
        this.cardMapper = cardMapper;
    }

    public Transfer outgoingFromDto(User holder, TransferDto transferDto) {
        Transfer transfer = new Transfer();
        transfer.setWallet(holder.getWallet());
        Card cardDto = cardMapper.fromDto(holder,transferDto.getCard());

        Card cardToSet = checkCardDetails(holder,cardDto);

        transfer.setCard(cardToSet);
        transfer.setAmount(transferDto.getAmount());
        transfer.setDirection(2);
        transfer.setDate(LocalDateTime.now());

        return transfer;
    }

    public Transfer ingoingFromDto(User holder, TransferDto transferDto) {
        Transfer transfer = new Transfer();
        transfer.setWallet(holder.getWallet());
        Card cardDto = cardMapper.fromDto(holder,transferDto.getCard());

        Card cardToSet = checkCardDetails(holder,cardDto);

        transfer.setCard(cardToSet);
        transfer.setAmount(transferDto.getAmount());
        transfer.setDirection(1);
        transfer.setDate(LocalDateTime.now());

        return transfer;
    }

    private Card checkCardDetails(User holder, Card card){
        return holder.getUserCards().stream().filter(card1 -> card1.getNumber().equals(card.getNumber())
                        && card1.getCvv().equals(card.getCvv()) && card1.getExpirationDate().equals(card.getExpirationDate()))
                .findFirst()
                .orElseThrow(() -> new AuthorizationException("Check card details."));
    }

}
