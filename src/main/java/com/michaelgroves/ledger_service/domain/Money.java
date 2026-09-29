package com.michaelgroves.ledger_service.domain;

import com.michaelgroves.ledger_service.exceptions.MismatchedCurrencyException;

public record Money(Currency currency, long amountInMinorUnits) {

    public Money add(Money amountToAdd) {
        if(!currency.equals(amountToAdd.currency)) {
            throw new MismatchedCurrencyException(String.format("Currencies do not match - %s and %s", currency, amountToAdd.currency));
        }
        return new Money(currency, amountInMinorUnits + amountToAdd.amountInMinorUnits);

    }

}
