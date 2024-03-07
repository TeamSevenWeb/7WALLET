package com.telerikacademy.web.virtualwallet.filters;

import java.util.Optional;

public class TransactionFilterOptions {
    private Optional<String> startDate;
    private Optional<String> endDate;
    private Optional<String> receiver;
    private Optional<String> direction;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public TransactionFilterOptions(
            String startDate,
            String endDate,
            String receiver,
            String direction,
            String sortBy,
            String sortOrder) {
        this.startDate = Optional.ofNullable(startDate);
        this.endDate = Optional.ofNullable(endDate);
        this.receiver = Optional.ofNullable(receiver);
        this.direction = Optional.ofNullable(direction);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getStartDate() {
        return startDate;
    }
    public Optional<String> getEndDate() {
        return endDate;
    }

    public Optional<String> getReceiver() {
        return receiver;
    }

    public Optional<String> getDirection() {
        return direction;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}
