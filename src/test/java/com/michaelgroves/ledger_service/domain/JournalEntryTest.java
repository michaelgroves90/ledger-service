package com.michaelgroves.ledger_service.domain;

import com.michaelgroves.ledger_service.exceptions.InvalidEntryException;
import com.michaelgroves.ledger_service.exceptions.UnbalancedEntryException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.michaelgroves.ledger_service.domain.Currency.*;
import static com.michaelgroves.ledger_service.domain.Direction.CREDIT;
import static com.michaelgroves.ledger_service.domain.Direction.DEBIT;
import static org.junit.jupiter.api.Assertions.*;

public class JournalEntryTest {

    @Test
    public void postingsCannotBeNullValue() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                new JournalEntry(null)
        );
        assertEquals("Postings cannot be null", exception.getMessage());
    }

    @Test
    public void rejectsEntryWhenDebitsAndCreditsDiffer() {

        Money debitAmount = new Money(USD, 1000);
        Money creditAmount = new Money(USD, 900);
        Posting postingOne = new Posting("cash", debitAmount, DEBIT);
        Posting postingTwo = new Posting("sales", creditAmount, CREDIT);

        UnbalancedEntryException result = assertThrows(UnbalancedEntryException.class, () -> new JournalEntry(List.of(postingOne, postingTwo)));
        assertEquals("Entry is unbalanced in USD - Debits: 1000 do not equal Credits: 900", result.getMessage());

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

    @Test
    public void acceptsEntryWithMultipleCurrenciesThatBalance() {

        Money debitAmountGBP = new Money(GBP, 1000);
        Money creditAmountGBP = new Money(GBP, 1000);
        Money debitAmountJPY = new Money(JPY, 15000);
        Money creditAmountJPY = new Money(JPY, 15000);

        Posting postingDebitGBP = new Posting("cash", debitAmountGBP, DEBIT);
        Posting postingCreditGBP = new Posting("sales", creditAmountGBP, CREDIT);
        Posting postingDebitJPY = new Posting("cash", debitAmountJPY, DEBIT);
        Posting postingCreditJPY = new Posting("sales", creditAmountJPY, CREDIT);

        JournalEntry journalEntry = new JournalEntry(List.of(postingDebitGBP, postingCreditGBP,  postingDebitJPY,  postingCreditJPY));
        assertEquals(4, journalEntry.postings().size());

    }

    @Test
    public void rejectsEntryWhenACurrencyDoesNotBalance() {

        Money debitAmount = new Money(USD, 1000);
        Money creditAmount = new Money(JPY, 1000);
        Posting postingDebit = new Posting("cash", debitAmount, DEBIT);
        Posting postingCredit = new Posting("sales", creditAmount, CREDIT);

        UnbalancedEntryException result = assertThrows(UnbalancedEntryException.class, () -> new JournalEntry(List.of(postingDebit, postingCredit)));
        assertEquals("Entry is unbalanced in USD - Debits: 1000 do not equal Credits: 0", result.getMessage());

    }

    @Test
    public void rejectsEntryWhenACurrencyDoesNotBalanceGBP() {

        Money debitAmount = new Money(GBP, 1000);
        Money creditAmount = new Money(JPY, 1000);
        Posting postingDebit = new Posting("cash", debitAmount, DEBIT);
        Posting postingCredit = new Posting("sales", creditAmount, CREDIT);

        UnbalancedEntryException result = assertThrows(UnbalancedEntryException.class, () -> new JournalEntry(List.of(postingDebit, postingCredit)));
        assertEquals("Entry is unbalanced in GBP - Debits: 1000 do not equal Credits: 0", result.getMessage());

    }

    @Test
    public void anEntryMustHavePostings() {

        InvalidEntryException result = assertThrows(InvalidEntryException.class, () -> new JournalEntry(List.of()));
        assertEquals("Entry has no postings", result.getMessage());

    }

    @Test
    public void changingTheOriginalListDoesNotChangeTheEntry() {

        Money debitAmount = new Money(GBP, 1000);
        Money creditAmount = new Money(GBP, 1000);
        Posting postingDebit = new Posting("cash", debitAmount, DEBIT);
        Posting postingCredit = new Posting("sales", creditAmount, CREDIT);

        ArrayList<Posting> postings = new ArrayList<>();
        postings.add(postingDebit);
        postings.add(postingCredit);
        JournalEntry journalEntry = new JournalEntry(postings);
        postings.add(postingDebit);

        assertEquals(2, journalEntry.postings().size());

    }

    @Test
    public void postingsFromTheEntryCannotBeModified() {
        Money debitAmount = new Money(GBP, 1000);
        Money creditAmount = new Money(GBP, 1000);
        Posting postingDebit = new Posting("cash", debitAmount, DEBIT);
        Posting postingCredit = new Posting("sales", creditAmount, CREDIT);

        ArrayList<Posting> postings = new ArrayList<>();
        postings.add(postingDebit);
        postings.add(postingCredit);
        JournalEntry journalEntry = new JournalEntry(postings);

        assertThrows(UnsupportedOperationException.class, () -> journalEntry.postings().add(postingDebit));

    }



}
