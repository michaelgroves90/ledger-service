package com.michaelgroves.ledger_service.domain;

import java.time.Instant;
import java.util.Objects;

public record PostedJournalEntry(long id, Instant createdAt, JournalEntry entry) {

    public PostedJournalEntry {

        Objects.requireNonNull(createdAt, "CreatedAt cannot be null");
        Objects.requireNonNull(entry, "Entry cannot be null");

    }
}
