package com.telerikacademy.web.virtualwallet.utils;

import com.telerikacademy.web.virtualwallet.models.Card;
import com.telerikacademy.web.virtualwallet.models.Transaction;
import com.telerikacademy.web.virtualwallet.models.Transfer;
import com.telerikacademy.web.virtualwallet.models.User;
import com.telerikacademy.web.virtualwallet.models.dtos.TransactionDto;
import com.telerikacademy.web.virtualwallet.models.dtos.TransferDto;
import com.telerikacademy.web.virtualwallet.repositories.contracts.TransactionRepository;
import com.telerikacademy.web.virtualwallet.repositories.contracts.UserRepository;
import com.telerikacademy.web.virtualwallet.repositories.contracts.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TransferMapper {
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    private final CardMapper cardMapper;

    @Autowired
    public TransferMapper(WalletRepository walletRepository, UserRepository userRepository, CardMapper cardMapper) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.cardMapper = cardMapper;
    }

    public Transfer outgoingFromDto(User user, TransferDto transferDto) {
        Transfer transfer = new Transfer();
        User holder = userRepository.getByField("username",user.getUsername());
        transfer.setWallet(holder.getWallet());
        Card card = cardMapper.fromDto(transferDto.getCard());
        transfer.setCard(card);
        transfer.setAmount(transferDto.getAmount());
        transfer.setDirection(0);
        transfer.setDate(LocalDateTime.now());

        return transfer;
    }

    public Transfer ingoingFromDto(User user, TransferDto transferDto) {
        Transfer transfer = new Transfer();
        User holder = userRepository.getByField("username",user.getUsername());
        transfer.setWallet(holder.getWallet());
        Card card = cardMapper.fromDto(transferDto.getCard());
        transfer.setCard(card);
        transfer.setAmount(transferDto.getAmount());
        transfer.setDirection(1);
        transfer.setDate(LocalDateTime.now());

        return transfer;
    }

}
