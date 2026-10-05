package com.michaelgroves.ledger_service.domain;

import org.junit.jupiter.api.Test;

import static com.michaelgroves.ledger_service.domain.AccountType.ASSET;
import static com.michaelgroves.ledger_service.domain.Currency.USD;
import static com.michaelgroves.ledger_service.domain.Direction.DEBIT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountTest {

    @Test
    public void idCannotBeNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                new Account(null, ASSET));
        assertEquals("ID cannot be null", exception.getMessage());
    }

    @Test
    public void accountTypeCannotBeNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                new Account("id", null));
        assertEquals("Account Type cannot be null", exception.getMessage());
    }

}
