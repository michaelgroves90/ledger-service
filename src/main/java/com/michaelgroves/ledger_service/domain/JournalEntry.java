package com.michaelgroves.ledger_service.domain;

import com.michaelgroves.ledger_service.exceptions.UnbalancedEntryException;

import java.util.List;

import static com.michaelgroves.ledger_service.domain.Direction.DEBIT;

public record JournalEntry(List<Posting> postings) {

    public JournalEntry {
        long debits = 0;
        long credits = 0;

        for (Posting posting : postings) {
            if(posting.direction() == DEBIT) {
                debits += posting.amount().amountInMinorUnits();
            }  else {
                credits += posting.amount().amountInMinorUnits();
            }
        }

        if(debits != credits) {
            throw new UnbalancedEntryException(String.format("Entry is unbalanced - Debits: %s do not equal Credits: %s", debits, credits));
        }
    }
}
