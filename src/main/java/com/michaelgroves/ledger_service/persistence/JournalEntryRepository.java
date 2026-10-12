package com.michaelgroves.ledger_service.persistence;

import com.michaelgroves.ledger_service.domain.JournalEntry;
import com.michaelgroves.ledger_service.domain.Posting;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JournalEntryRepository {

    private final JdbcClient jdbc;

    public JournalEntryRepository(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    @Transactional
    public long save(JournalEntry journalEntry, String idempotencyKey) {

        long entryId = jdbc.sql("""
                INSERT INTO journal_entries (description, idempotency_key)
                VALUES (:description, :idempotencyKey)
                RETURNING id
                """)
                .param("description", journalEntry.description())
                .param("idempotencyKey", idempotencyKey)
                .query(Long.class)
                .single();
        for (Posting posting : journalEntry.postings()) {
            jdbc.sql("""
                    INSERT INTO postings (journal_entry_id, account_id,
                    direction, currency, amount_minor)
                    VALUES (:entryId, :accountId, :direction, :currency, :amountMinor)
                    """)
                    .param("entryId", entryId)
                    .param("accountId", posting.account())
                    .param("direction", posting.direction().name())
                    .param("currency", posting.amount().currency().name())
                    .param("amountMinor", posting.amount().amountInMinorUnits())
                    .update();
        }
        return entryId;
    }

}
