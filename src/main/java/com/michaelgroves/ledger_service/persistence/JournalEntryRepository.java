package com.michaelgroves.ledger_service.persistence;

import com.michaelgroves.ledger_service.domain.*;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JournalEntryRepository {

    private final JdbcClient jdbc;

    public JournalEntryRepository(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    private record EntryRow(long id, Instant createdAt, String description) { }

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

    public Optional<PostedJournalEntry> findById(long id) {
        Optional<EntryRow> row = jdbc.sql("""
                SELECT id, created_at, description
                FROM journal_entries
                WHERE id = :id
                """)
                .param("id", id)
                .query((rs, rowNum) -> new EntryRow(
                        rs.getLong("id"),
                        rs.getObject("created_at", OffsetDateTime.class).toInstant(),
                        rs.getString("description")))
                .optional();
        return row.map(r -> new PostedJournalEntry(
                r.id(),
                r.createdAt(),
                new JournalEntry(r.description(), findPostings(r.id()))
        ));

    }

    // helpers

    private List<Posting> findPostings(long entryId) {
        return jdbc.sql("""
            SELECT account_id, direction, currency, amount_minor
            FROM postings
            WHERE journal_entry_id = :entryId
            ORDER BY id
            """)
                .param("entryId", entryId)
                .query((rs, rowNum) -> new Posting(
                                rs.getString("account_id"),
                                new Money(Currency.valueOf(rs.getString("currency")), rs.getLong("amount_minor")),
                                Direction.valueOf(rs.getString("direction"))
                        )
                )
                .list();
    }

}
