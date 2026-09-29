package com.michaelgroves.ledger_service.domain;

import com.michaelgroves.ledger_service.exceptions.InvalidPostingException;

public record Posting(String account, Money amount, Direction direction) {

    public Posting {
        if (amount.amountInMinorUnits() <= 0) {
            throw new InvalidPostingException(String.format("Amount must be greater than zero - %s %s", amount.currency(), amount.amountInMinorUnits()));
        }
    }

}
