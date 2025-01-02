-- Elimina il database esistente, se presente
DROP DATABASE IF EXISTS "climate_monitoring";

COMMENT ON DATABASE "climate_monitoring"
    IS 'DB per LAB B';

GRANT TEMPORARY, CONNECT ON DATABASE "climate_monitoring" TO PUBLIC;

GRANT ALL ON DATABASE "climate_monitoring" TO agent1;

GRANT ALL ON DATABASE "climate_monitoring" TO postgres;

-- Tabella Citta
CREATE TABLE Citta (
                       Geoname_ID BIGINT PRIMARY KEY,
                       Name VARCHAR(100) NOT NULL,
                       ASCII_Name VARCHAR(100) NOT NULL,
                       Country_Code VARCHAR(10) NOT NULL,
                       Country_Name VARCHAR(100) NOT NULL,
                       longitude FLOAT8 NOT NULL,
                       latitude FLOAT8 NOT NULL
);

-- Tabella operatori
CREATE TABLE operatori (
                           codice_fiscale VARCHAR(16) PRIMARY KEY,
                           nome VARCHAR(100) NOT NULL,
                           cognome VARCHAR(100) NOT NULL,
                           username VARCHAR(12) NOT NULL UNIQUE,
                           email VARCHAR(100) NOT NULL UNIQUE,
                           password VARCHAR(100) NOT NULL,
                           centro_monitoraggio VARCHAR(50) NOT NULL
);

-- Tabella centrimonitoraggio
CREATE TABLE centrimonitoraggio (
                                    id VARCHAR(50) PRIMARY KEY,
                                    nomeCentro VARCHAR(100) NOT NULL,
                                    descrizione TEXT NOT NULL,
                                    username_operatore varchar not null
);

-- Tabella Parametro
CREATE TABLE Parametro (
                           ID VARCHAR(100) PRIMARY KEY,
                           wind VARCHAR(10),
                           humidity VARCHAR(10),
                           pressure VARCHAR(10),
                           temperature VARCHAR(10),
                           precipitation VARCHAR(10),
                           glacier_altitude VARCHAR(10),
                           glacier_mass VARCHAR(10),
                           wind_comment VARCHAR(255),
                           humidity_comment VARCHAR(255),
                           pressure_comment VARCHAR(255),
                           temperature_comment VARCHAR(255),
                           precipitation_comment VARCHAR(255),
                           glacier_altitude_comment VARCHAR(255),
                           glacier_mass_comment VARCHAR(255),
                           wind_score DECIMAL(5),
                           humidity_score DECIMAL(5),
                           pressure_score DECIMAL(5),
                           temperature_score DECIMAL(5),
                           precipitation_score DECIMAL(5),
                           glacier_altitude_score DECIMAL(5),
                           glacier_mass_score DECIMAL(5)
);

-- Tabella Rilevazione
CREATE TABLE Rilevazione (
                             CF VARCHAR(16) NOT NULL REFERENCES operatori(codice_fiscale)
                                 ON UPDATE CASCADE
                                 ON DELETE NO ACTION,
                             CentriMonitoraggio_ID VARCHAR(50) NOT NULL REFERENCES centrimonitoraggio(id)
                                 ON UPDATE CASCADE
                                 ON DELETE NO ACTION,
                             Geoname_ID BIGINT NOT NULL REFERENCES Citta(Geoname_ID)
                                 ON UPDATE CASCADE
                                 ON DELETE NO ACTION,
                             Par_ID VARCHAR(100) NOT NULL REFERENCES Parametro(ID)
                                 ON UPDATE CASCADE
                                 ON DELETE NO ACTION,
                             date_r DATE NOT NULL,
                             PRIMARY KEY (CF, CentriMonitoraggio_ID, Geoname_ID, Par_ID, date_r)
);

-- Tabella Osservano
CREATE TABLE Osservano (
                           CentriMonitoraggio_ID VARCHAR(50) REFERENCES centrimonitoraggio(id)
                               ON UPDATE CASCADE
                               ON DELETE NO ACTION,
                           Geoname_ID BIGINT REFERENCES Citta(Geoname_ID)
                               ON UPDATE CASCADE
                               ON DELETE NO ACTION,
                           PRIMARY KEY (CentriMonitoraggio_ID, Geoname_ID)
);




-- Assicurati che la colonna CF sia una chiave primaria (già definita nello script precedente)
ALTER TABLE lavora
    ADD CONSTRAINT lavora_lavora_fk
        FOREIGN KEY (centrimonitoraggio_id) REFERENCES lavora(cf);

DELETE FROM centrimonitoraggio
WHERE username_operatore = null;




-- Tabella Lavora
CREATE TABLE Lavora (
                        CF VARCHAR(16) NOT NULL REFERENCES operatori(codice_fiscale)
                            ON UPDATE CASCADE
                            ON DELETE NO ACTION,
                        CentriMonitoraggio_ID VARCHAR(50) NOT NULL REFERENCES centrimonitoraggio(id)
                            ON UPDATE CASCADE
                            ON DELETE NO ACTION,
                        PRIMARY KEY (CF, CentriMonitoraggio_ID)
);

CREATE TABLE dati_climatici (
                                key varchar(255) PRIMARY KEY,
                                centro_id VARCHAR(255) NOT NULL,
                                velocita_vento_score INT NOT NULL,
                                velocita_vento_note VARCHAR(256),
                                temperatura_score INT NOT NULL,
                                temperatura_note VARCHAR(256),
                                umidita_score INT NOT NULL,
                                umidita_note VARCHAR(256),
                                precipitazioni_score INT NOT NULL,
                                precipitazioni_note VARCHAR(256),
                                data_inserimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
