package com.telerikacademy.web.virtualwallet.exceptions;

public class TransactionExpiredException extends RuntimeException{
    public TransactionExpiredException(String message) {
        super(message);
    }
}
