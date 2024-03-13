package com.telerikacademy.web.virtualwallet.filters.dtos;


public class TransactionFilterDto {
    private String date;
    private String receiver;
    private String sender;
    private String direction;
    private String sortBy;
    private String sortOrder;

    public TransactionFilterDto(){
    }

    public String getDate() {
        return date;
    }

    public String getSender() {
        return sender;
    }
    public String getReceiver() {
        return receiver;
    }

    public String getDirection() {
        return direction;
    }

    public String getSortBy() {
        return sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
