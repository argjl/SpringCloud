CREATE TABLE IF NOT EXISTS exchange_value (
    id BIGINT PRIMARY KEY,
    currency_from VARCHAR(3),
    currency_to VARCHAR(3),
    conversion_multiple DECIMAL(15, 6),
    port INT
);
