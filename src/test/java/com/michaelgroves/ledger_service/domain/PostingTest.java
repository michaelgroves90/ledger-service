package com.michaelgroves.ledger_service.domain;

import com.michaelgroves.ledger_service.exceptions.InvalidPostingException;
import org.junit.jupiter.api.Test;

import static com.michaelgroves.ledger_service.domain.Currency.USD;
import static com.michaelgroves.ledger_service.domain.Direction.DEBIT;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PostingTest {

    @Test
    public void amountMustBeGreaterThanZero() {
        assertThrows(
                InvalidPostingException.class, () ->
                new Posting("id", new Money(USD, 0), DEBIT),
                "Amount must be greater than zero - USD 0"
        );

    }
}
