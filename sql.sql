CREATE TABLE operatori (
    id SERIAL PRIMARY KEY,              -- ID unico per ogni operatore (generato automaticamente)
    cf VARCHAR(16) UNIQUE NOT NULL,     -- Codice fiscale, che deve essere unico
    name VARCHAR(50),                   -- Nome dell'operatore
    surname VARCHAR(50),                -- Cognome dell'operatore
    user_id VARCHAR(30) UNIQUE NOT NULL,-- ID utente, che deve essere unico
    email VARCHAR(100) UNIQUE,          -- Email dell'operatore, deve essere unica
    password VARCHAR(100) NOT NULL      -- Password dell'operatore
);
