package com.michaelgroves.ledger_service.domain;

import com.michaelgroves.ledger_service.exceptions.InvalidPostingException;
import org.junit.jupiter.api.Test;

import static com.michaelgroves.ledger_service.domain.Currency.USD;
import static com.michaelgroves.ledger_service.domain.Direction.DEBIT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PostingTest {

    @Test
    public void throwExceptionWhenAmountIsZero() {
        InvalidPostingException exception = assertThrows(
                InvalidPostingException.class, () ->
                        new Posting("id", new Money(USD, 0), DEBIT)
        );
        assertEquals("Amount must be greater than zero: USD 0", exception.getMessage());

    }

    @Test
    public void throwExceptionWhenAmountIsNegative() {
        InvalidPostingException exception = assertThrows(
                InvalidPostingException.class, () ->
                        new Posting("id", new Money(USD, -500), DEBIT)
        );
        assertEquals("Amount cannot be a negative value: USD -500", exception.getMessage());

    }

    @Test
    public void accountCannotBeNullValue() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                new Posting(null, new Money(USD, 500), DEBIT)
        );
        assertEquals("Account cannot be null", exception.getMessage());
    }

    @Test
    public void amountCannotBeNullValue() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                new Posting("BOB", null, DEBIT)
        );
        assertEquals("Amount cannot be null", exception.getMessage());
    }

    @Test
    public void directionCannotBeNullValue() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                new Posting("BOB", new Money(USD, 500), null)
        );
        assertEquals("Direction cannot be null", exception.getMessage());
    }
}
