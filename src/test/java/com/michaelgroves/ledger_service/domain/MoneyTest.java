package com.michaelgroves.ledger_service.domain;

import com.michaelgroves.ledger_service.exceptions.MismatchedCurrencyException;
import org.junit.jupiter.api.Test;

import static com.michaelgroves.ledger_service.domain.Currency.JPY;
import static com.michaelgroves.ledger_service.domain.Currency.USD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MoneyTest {

    @Test
    public void addUSDMoneyReturnsCorrectAmount() {
        Money valueOne = new Money(USD, 500);
        Money valueTwo = new Money(USD, 700);

        assertEquals(new Money(USD, 1200), valueOne.add(valueTwo));

    }

    @Test
    public void throwsExceptionWhenMismatchedCurrencies() {
        Money valueOne = new Money(USD, 500);
        Money valueTwo = new Money(JPY, 700);

        assertThrows(MismatchedCurrencyException.class, () ->
               valueOne.add(valueTwo)
        );
    }
}
