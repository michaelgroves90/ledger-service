package com.michaelgroves.ledger_service.domain;

import java.util.Objects;

public record Account(String id, AccountType accountType) {

    public Account {
        Objects.requireNonNull(id, "ID cannot be null");
        Objects.requireNonNull(accountType, "Account Type cannot be null");
    }

}
