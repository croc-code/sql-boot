drop schema hr if exists cascade;
create schema hr;

create table hr.users (
  id            integer,
  first_name    varchar(30),
  last_name     varchar(31),
  email         varchar(50),
  constraint users_pk primary key (id, first_name)
);
comment on table hr.users is 'all system users';

create index hr.users_email_idx on hr.users (email, last_name);

create view hr.users_view_materialized as select * from hr.users;


create table hr.jobs (
  id            integer primary key,
  name          varchar(30),
  description   varchar(100),
  constraint jobs_pk primary key (id)
);
comment on table hr.jobs is 'all jobs';

create table hr.persons (
  id            integer primary key,
  id_job        integer,
  id_user       integer,
  constraint persons_pk primary key (id)
);
comment on table hr.persons is 'all persons';

alter table hr.persons add constraint person_to_jobs_fk foreign key (id_job) references hr.jobs(id);
alter table hr.persons add constraint person_to_users_fk foreign key (id_user) references hr.users(id);

create view hr.persons_view_materialized as select * from hr.persons;



create table hr.countries (
  id            integer primary key,
  name          varchar(50),
  population    integer,
  constraint countries_pk primary key (id)
);

create table hr.cities (
  id            integer primary key,
  name          varchar(50),
  id_country    integer,
  constraint cities_pk primary key (id)
);

create table hr.cities_2 (
  id            integer primary key,
  name          varchar(50),
  id_country    integer,
  constraint cities_2_pk primary key (id)
);





drop schema bookings if exists cascade;
create schema bookings;

create table bookings.aircrafts (
    aircraft_code character(3) not null,
    model         text not null,
    range         integer not null,
    constraint aircrafts_range_check check ((range > 0))
);

create table bookings.airports (
    airport_code  character(3) not null,
    airport_name  text not null,
    city          text not null,
    longitude     double precision not null,
    latitude      double precision not null,
    timezone text not null
);

create table bookings.boarding_passes (
    ticket_no     character(13) not null,
    flight_id     integer not null,
    boarding_no   integer not null,
    seat_no       character varying(4) not null
);

create table bookings.bookings (
    book_ref      character(6) not null,
    book_date     timestamp not null,
    total_amount  numeric(10,2) not null
);

create table bookings.flights (
    flight_id           integer not null,
    flight_no           character(6) not null,
    scheduled_departure timestamp not null,
    scheduled_arrival   timestamp not null,
    departure_airport   character(3) not null,
    arrival_airport     character(3) not null,
    status              character varying(20) not null,
    aircraft_code       character(3) not null,
    actual_departure    timestamp ,
    actual_arrival      timestamp);