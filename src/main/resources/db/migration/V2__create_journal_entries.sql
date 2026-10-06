CREATE TABLE journal_entries (
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT now(),
    description         TEXT NOT NULL,
    idempotency_key     TEXT NOT NULL UNIQUE
);