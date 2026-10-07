package com.michaelgroves.ledger_service.domain;

import com.michaelgroves.ledger_service.exceptions.InvalidEntryException;
import com.michaelgroves.ledger_service.exceptions.UnbalancedEntryException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.michaelgroves.ledger_service.domain.Direction.DEBIT;

public record JournalEntry(String description, List<Posting> postings) {

    public JournalEntry {

        Objects.requireNonNull(description, "Description cannot be null");
        Objects.requireNonNull(postings, "Postings cannot be null");

        postings = List.copyOf(postings);

        if (description.isBlank()) {
            throw new InvalidEntryException("Description cannot be blank");
        }

        if (postings.isEmpty()) {
            throw new InvalidEntryException("Entry has no postings");
        }

        Map<Currency, Long> debits = new HashMap<>();
        Map<Currency, Long> credits = new HashMap<>();

        for (Posting posting : postings) {
            if(posting.direction() == DEBIT) {
                debits.merge(posting.amount().currency(), posting.amount().amountInMinorUnits(), Long::sum);
            } else {
                credits.merge(posting.amount().currency(), posting.amount().amountInMinorUnits(), Long::sum);
            }
        }

        for (Currency currency : Currency.values()) {
            long currencyDebits = debits.getOrDefault(currency, 0L);
            long currencyCredits = credits.getOrDefault(currency, 0L);
            if (currencyDebits != currencyCredits) {
                throw new UnbalancedEntryException(String.format("Entry is unbalanced in %s - Debits: %s do not equal Credits: %s", currency, currencyDebits, currencyCredits));
            }

        }

    }
}
