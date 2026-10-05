package com.michaelgroves.ledger_service.domain;

import static com.michaelgroves.ledger_service.domain.Direction.CREDIT;
import static com.michaelgroves.ledger_service.domain.Direction.DEBIT;

public enum AccountType {

    ASSET(DEBIT),
    EXPENSE(DEBIT),
    LIABILITY(CREDIT),
    EQUITY(CREDIT),
    INCOME(CREDIT);

    private final Direction normalBalance;

    AccountType(Direction normalBalance) {
        this.normalBalance = normalBalance;
    }

    public Direction normalBalance() {
        return normalBalance;
    }
}
