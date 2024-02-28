package com.telerikacademy.web.virtualwallet.exceptions;

public class FundsSupplyException extends RuntimeException{
    public FundsSupplyException(){
        super("Not enough resources available");
    }
}
