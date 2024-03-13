package com.telerikacademy.web.virtualwallet.utils;

import com.telerikacademy.web.virtualwallet.exceptions.AuthorizationException;
import com.telerikacademy.web.virtualwallet.models.Card;
import com.telerikacademy.web.virtualwallet.models.Transfer;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.TransferDto;
import com.telerikacademy.web.virtualwallet.models.wallets.JoinWallet;
import com.telerikacademy.web.virtualwallet.services.contracts.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TransferMapper {

    private final CardService cardService;

    @Autowired
    public TransferMapper(CardService cardService) {
        this.cardService = cardService;
    }

    public Transfer outgoingFromDto(User holder, TransferDto transferDto) {
        Transfer transfer = new Transfer();
        transfer.setWallet(holder.getWallet());

        Card card = cardService.get(holder,transferDto.getCardId());

        transfer.setCard(card);
        transfer.setAmount(transferDto.getAmount());
        transfer.setDirection(2);
        transfer.setDate(LocalDateTime.now());

        return transfer;
    }
    public Transfer outgoingFromDto(User holder,JoinWallet wallet, TransferDto transferDto) {
        Transfer transfer = new Transfer();
        transfer.setWallet(wallet);

        Card card = cardService.get(holder,transferDto.getCardId());

        transfer.setCard(card);
        transfer.setAmount(transferDto.getAmount());
        transfer.setDirection(2);
        transfer.setDate(LocalDateTime.now());

        return transfer;
    }

    public Transfer ingoingFromDto(User holder, TransferDto transferDto) {
        Transfer transfer = new Transfer();
        transfer.setWallet(holder.getWallet());
        Card card = cardService.get(holder,transferDto.getCardId());

        transfer.setCard(card);
        transfer.setAmount(transferDto.getAmount());
        transfer.setDirection(1);
        transfer.setDate(LocalDateTime.now());

        return transfer;
    }


    public Transfer ingoingFromDto(User holder, JoinWallet wallet, TransferDto transferDto) {
        Transfer transfer = new Transfer();
        transfer.setWallet(wallet);
        Card card = cardService.get(holder,transferDto.getCardId());

        transfer.setCard(card);
        transfer.setAmount(transferDto.getAmount());
        transfer.setDirection(1);
        transfer.setDate(LocalDateTime.now());

        return transfer;
    }

}
