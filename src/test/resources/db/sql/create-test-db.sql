create schema main_schema;

CREATE TABLE main_schema.city (
  id        INTEGER PRIMARY KEY,
  country   VARCHAR(50),
  city      VARCHAR(50)
);

CREATE TABLE main_schema.users (
  id      INTEGER PRIMARY KEY,
  name    VARCHAR(30),
  email   VARCHAR(50),
  id_city integer
);

CREATE INDEX main_schema.users_name_idx on main_schema.users(name);

CREATE INDEX main_schema.users_email_idx on main_schema.users(email);

ALTER TABLE main_schema.users
ADD CONSTRAINT main_schema.users_city_fk
FOREIGN KEY (id_city) REFERENCES main_schema.city(id);

CREATE ALIAS getVersion FOR "org.h2.engine.Constants.getVersion";