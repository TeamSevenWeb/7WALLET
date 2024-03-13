package com.telerikacademy.web.virtualwallet.filters.dtos;


public class TransactionFilterDto {
    private String startDate;
    private String endDate;
    private String receiver;
    private String sender;
    private String direction;
    private String sortBy;
    private String sortOrder;

    public TransactionFilterDto(){
    }

    public String getStartDate() {
        return startDate;
    }
    public String getEndDate() {
        return endDate;
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
}
