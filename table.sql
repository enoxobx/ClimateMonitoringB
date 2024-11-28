-- Database: ClimateMonitoring

DROP DATABASE IF EXISTS "ClimateMonitoring";

CREATE DATABASE "ClimateMonitoring"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Italian_Italy.1252'
    LC_CTYPE = 'Italian_Italy.1252'
    LOCALE_PROVIDER = 'libc'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

COMMENT ON DATABASE "ClimateMonitoring"
    IS 'DB per LAB B';

GRANT TEMPORARY, CONNECT ON DATABASE "ClimateMonitoring" TO PUBLIC;

GRANT ALL ON DATABASE "ClimateMonitoring" TO agent1;

GRANT ALL ON DATABASE "ClimateMonitoring" TO postgres;

CREATE TABLE Citta (
                       Geoname_ID decimal(7) PRIMARY KEY,
                       Name VARCHAR(30) NOT NULL,
                       ASCII_Name VARCHAR(30) NOT NULL,
                       Country_Code VARCHAR(3) NOT NULL,
                       Country_Name VARCHAR(30) NOT NULL,
                       longitude FLOAT8 NOT NULL,
                       latitude FLOAT8 NOT NULL
);

CREATE TABLE operatori (
                           codice_fiscale VARCHAR(16) PRIMARY KEY,
                           nome VARCHAR(30) NOT NULL,
                           cognome VARCHAR(30) NOT NULL,
                           username VARCHAR(12) NOT NULL UNIQUE,
                           email VARCHAR(30) NOT NULL UNIQUE,
                           password VARCHAR(30) NOT NULL,
                           centro_monitoraggio VARCHAR(30) NOT NULL
);


CREATE TABLE CentriMonitoraggio(
                                   id VARCHAR(30) PRIMARY KEY,
                                   nomeCentro VARCHAR(30) NOT NULL,
                                   Indirizzo VARCHAR(50) NOT NULL
);

CREATE TABLE Parametro(
                          ID VARCHAR(30) PRIMARY KEY,
                          wind VARCHAR(10),
                          humidity VARCHAR(10),
                          pressure VARCHAR(10),
                          temperature VARCHAR(10),
                          precipitation  VARCHAR(10),
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

CREATE TABLE Rilevazione (
                             CF VARCHAR(16) REFERENCES Operatori(codice_fiscale)
                                 ON UPDATE CASCADE
                                 ON DELETE NO ACTION,
                             CentriMonitoraggio_ID VARCHAR(30) REFERENCES CentriMonitoraggio(id)
                                 ON UPDATE CASCADE
                                 ON DELETE NO ACTION,
                             Geoname_ID decimal(7) REFERENCES Citta(Geoname_ID)
                                 ON UPDATE CASCADE
                                 ON DELETE NO ACTION,
                             Par_ID VARCHAR(30) REFERENCES Parametro(ID)
                                 ON UPDATE CASCADE
                                 ON DELETE NO ACTION,
                             date_r date NOT NULL
);
CREATE TABLE Osservano (
                           CentriMonitoraggio_ID VARCHAR(30) REFERENCES CentriMonitoraggio(id)
                               ON UPDATE CASCADE
                               ON DELETE NO ACTION,
                           Geoname_ID decimal(7) REFERENCES Citta(Geoname_ID)
                               ON UPDATE CASCADE
                               ON DELETE NO ACTION,
                           PRIMARY KEY(CentriMonitoraggio_ID,Geoname_ID)
);

CREATE TABLE Lavora(
                       CF VARCHAR(16) REFERENCES Operatori(codice_fiscale)
                           ON UPDATE CASCADE
                           ON DELETE NO ACTION,
                       CentriMonitoraggio_ID VARCHAR(30) REFERENCES CentriMonitoraggio(id)
                           ON UPDATE CASCADE
                           ON DELETE NO ACTION
);
