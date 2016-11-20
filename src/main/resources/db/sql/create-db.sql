create schema hr;

create table hr.users (
  id            integer primary key,
  first_name    varchar(30),
  last_name     varchar(30),
  email         varchar(50)
);

create table hr.jobs (
  id            integer primary key,
  name          varchar(30),
  description   varchar(100)
);

create table hr.persons (
  id        integer primary key,
  id_job    integer,
  id_user   integer
);



create table hr.countries (
  id            integer primary key,
  name          varchar(50),
  population    integer
);

create table hr.cities (
  id            integer primary key,
  name          varchar(50),
  id_country    integer
);



CREATE ALIAS getVersion FOR "org.h2.engine.Constants.getVersion";