CREATE SEQUENCE base_seq START 1;

CREATE TABLE CLIENTS (
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    patronymic VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL,
    phone_numbers TEXT[] NOT NULL,
    emails TEXT[] NOT NULL,
    login VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE INDEX idx_client_last_name ON CLIENTS (last_name);
CREATE INDEX idx_client_first_name ON CLIENTS (first_name);
CREATE INDEX idx_client_patronymic ON CLIENTS (patronymic);
CREATE INDEX idx_client_phone_numbers ON CLIENTS USING GIN (phone_numbers);
CREATE INDEX idx_client_emails ON CLIENTS USING GIN (emails);
CREATE INDEX idx_client_login ON CLIENTS (login);

CREATE TABLE BANK_ACCOUNTS (
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    amount BIGINT NOT NULL,
    client_id INT NOT NULL UNIQUE,
    bank_account_increase_percent BIGINT,
    decreasing_percent BIGINT,
    CONSTRAINT fk_client
        FOREIGN KEY(client_id)
        REFERENCES CLIENTS(id)
);

ALTER TABLE CLIENTS
ADD COLUMN bank_account_id INT UNIQUE;

ALTER TABLE CLIENTS
ADD CONSTRAINT fk_bank_account
FOREIGN KEY (bank_account_id)
REFERENCES BANK_ACCOUNTS(id);

CREATE TABLE BANK_OPERATIONS (
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    client_id INT NOT NULL,
    amount BIGINT NOT NULL,
    bank_operation_status VARCHAR(50) NOT NULL,
    client_old_balance BIGINT NOT NULL,
    client_new_balance BIGINT NOT NULL,
    recipient_id INT,
    recipient_old_balance BIGINT,
    recipient_new_balance BIGINT,
    CONSTRAINT fk_client_operation
        FOREIGN KEY(client_id)
        REFERENCES CLIENTS(id),
    CONSTRAINT fk_recipient
        FOREIGN KEY(recipient_id)
        REFERENCES CLIENTS(id)
);

CREATE TABLE CONTACT_DATA_HISTORY (
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    login VARCHAR(255) NOT NULL,
    contact_data_type VARCHAR(50) NOT NULL,
    contact_value VARCHAR(255) NOT NULL,
    contact_data_status VARCHAR(50) NOT NULL
);

CREATE INDEX idx_contact_data_history_login ON CONTACT_DATA_HISTORY (login);
CREATE INDEX idx_contact_data_history_contact_value ON CONTACT_DATA_HISTORY (contact_value);