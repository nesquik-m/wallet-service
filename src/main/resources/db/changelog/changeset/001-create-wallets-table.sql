-- liquibase formatted sql

-- changeset Anastasia Martova:create-wallets-table
CREATE TABLE wallets
(
    id             UUID PRIMARY KEY,
    operation_type VARCHAR(255) NOT NULL,
    amount         DECIMAL      NOT NULL
);