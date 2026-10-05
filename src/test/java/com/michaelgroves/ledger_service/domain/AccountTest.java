package com.michaelgroves.ledger_service.domain;

import com.michaelgroves.ledger_service.exceptions.InvalidAccountException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.michaelgroves.ledger_service.domain.AccountType.ASSET;
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

    @ParameterizedTest(name = "{0} is an accepted ID")
    @ValueSource(strings = {"cash", "sales-japan", "cash-usd-2", "2024-sales"} )
    void validIDsAreAccepted(String id) {
        Account account = new Account(id, ASSET);
        assertEquals(id, account.id());
    }

    @ParameterizedTest(name = "{0} is an invalid ID and is rejected")
    @ValueSource(strings = {"Cash", "cash at bank", "cash/usd", "-sales", "sales-", "sales--usd", ""} )
    void invalidIDsAreRejected(String id) {
        InvalidAccountException exception = assertThrows(InvalidAccountException.class, () -> new Account(id, ASSET));
        assertEquals(String.format("Invalid ID: '%s' - Use lowercase letters and digits, words separated by single hyphens", id), exception.getMessage());
    }

}
