package com.michaelgroves.ledger_service.domain;

import com.michaelgroves.ledger_service.exceptions.UnbalancedEntryException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.michaelgroves.ledger_service.domain.Currency.USD;
import static com.michaelgroves.ledger_service.domain.Direction.CREDIT;
import static com.michaelgroves.ledger_service.domain.Direction.DEBIT;
import static org.junit.jupiter.api.Assertions.*;

public class JournalEntryTest {

    @Test
    public void rejectsEntryWhenDebitsAndCreditsDiffer() {

        Money debitAmount = new Money(USD, 1000);
        Money creditAmount = new Money(USD, 900);
        Posting postingOne = new Posting("cash", debitAmount, DEBIT);
        Posting postingTwo = new Posting("sales", creditAmount, CREDIT);

        UnbalancedEntryException result = assertThrows(UnbalancedEntryException.class, () -> new JournalEntry(List.of(postingOne, postingTwo)));
        assertEquals("Entry is unbalanced - Debits: 1000 do not equal Credits: 900", result.getMessage());

    }

    @Test
    public void acceptsEntryWhenDebitsAndCreditsMatch() {

        Money debitAmount = new Money(USD, 1000);
        Money feesAmount = new Money(USD, 50);
        Money creditAmount = new Money(USD, 1050);
        Posting postingDebit = new Posting("cash", debitAmount, DEBIT);
        Posting postingFees = new Posting("fees", feesAmount, DEBIT);
        Posting postingCredit = new Posting("sales", creditAmount, CREDIT);

        JournalEntry journalEntry = new JournalEntry(List.of(postingDebit, postingFees, postingCredit));
        assertEquals(3, journalEntry.postings().size());

    }




}
