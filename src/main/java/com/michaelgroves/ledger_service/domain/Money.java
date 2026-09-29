package com.michaelgroves.ledger_service.domain;

public record Money(Currency currency, long amountInMinorUnits) {

    public Money add(Money amountToAdd) {
        return new Money(currency, amountInMinorUnits + amountToAdd.amountInMinorUnits);
    }

}
