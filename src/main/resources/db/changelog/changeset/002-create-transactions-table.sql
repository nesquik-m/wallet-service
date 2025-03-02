-- liquibase formatted sql

-- -- changeset Anastasia Martova:create-operation-type-enum
-- CREATE TYPE operation_type AS ENUM ('DEPOSIT', 'WITHDRAW');
--
-- -- changeset Anastasia Martova:create-transaction-status-enum
-- CREATE TYPE transaction_status AS ENUM ('PENDING', 'APPROVED', 'FAILED');

-- changeset Anastasia Martova:create-transactions-table
CREATE TABLE transactions
(
    id             SERIAL PRIMARY KEY,
    wallet_id      UUID        NOT NULL,
    amount         DECIMAL     NOT NULL,
    operation_type VARCHAR(20) NOT NULL,
    status         VARCHAR(20) NOT NULL,
    timestamp      TIMESTAMP   NOT NULL,

    CONSTRAINT fk_transactions_wallet
        FOREIGN KEY (wallet_id)
            REFERENCES wallets (id),

    CONSTRAINT chk_operation_type CHECK (operation_type IN ('DEPOSIT', 'WITHDRAW')),

    CONSTRAINT chk_transaction_status CHECK (status IN ('PENDING', 'APPROVED', 'FAILED'))

);

-- changeset Anastasia Martova:add-index-to-transactions-table
CREATE INDEX idx_transactions_wallet_id ON transactions (wallet_id);