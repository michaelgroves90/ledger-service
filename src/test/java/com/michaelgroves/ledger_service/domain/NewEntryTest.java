package com.michaelgroves.ledger_service.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.michaelgroves.ledger_service.exceptions.UnbalancedEntryException;

public class NewEntryTest {

    NewEntry newEntry;

    @Test()
    void shouldConstructBalancedEntry() {
        newEntry = new NewEntry();
        int debit = 450;
        int fees = 0;
        int credit = 450;
        assertEquals("Balanced", newEntry.balance(debit, fees, credit));
    }

    @Test()
    void shouldThrowUnbalancedEntryExceptionOnUnbalancedEntry() {
        newEntry = new NewEntry();
        int debit = 450;
        int fees = 0;
        int credit = 350;
        try {
            newEntry.balance(debit, fees, credit);
        } catch (UnbalancedEntryException e) {
            assertEquals("Not Balanced", e.getMessage());
        }
    }



}
