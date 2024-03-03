package com.telerikacademy.web.virtualwallet.exceptions;

public class TransferFailedException extends RuntimeException{
    public TransferFailedException(){
        super("Transfer failed");
    }

}
