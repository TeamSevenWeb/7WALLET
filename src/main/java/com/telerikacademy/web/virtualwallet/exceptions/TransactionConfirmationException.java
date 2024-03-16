package com.telerikacademy.web.virtualwallet.exceptions;

public class TransactionConfirmationException extends RuntimeException{
    public TransactionConfirmationException(String message) {
        super(message);
    }
}
