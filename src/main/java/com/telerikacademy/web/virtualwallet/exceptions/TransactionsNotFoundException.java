package com.telerikacademy.web.virtualwallet.exceptions;

public class TransactionsNotFoundException extends RuntimeException{
    public TransactionsNotFoundException(String type) {
        super(String.format("No %s found.", type));
    }

    public TransactionsNotFoundException(String type, String attribute, String value) {
        super(String.format("%s with %s %s not found.", type, attribute, value));
    }
}
