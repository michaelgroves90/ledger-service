CREATE TABLE postings (
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    journal_entry_id    BIGINT NOT NULL REFERENCES journal_entries (id),
    account_id          TEXT NOT NULL REFERENCES accounts (id),
    direction           TEXT NOT NULL CHECK (direction IN ('DEBIT', 'CREDIT')),
    currency            TEXT NOT NULL CHECK (currency IN ('USD', 'GBP', 'JPY')),
    amount_minor        BIGINT NOT NULL CHECK (amount_minor > 0)
);

CREATE INDEX ON postings (journal_entry_id);
CREATE INDEX ON postings (account_id);
