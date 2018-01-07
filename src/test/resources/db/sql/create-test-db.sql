create schema main_schema;

create table main_schema.city (
  id        integer primary key,
  country   varchar(50),
  city      varchar(50)
);

create table main_schema.users (
  id        integer primary key,
  name      varchar(30),
  email     varchar(50),
  id_city   integer
);

create index main_schema.users_name_idx on main_schema.users(name);

create index main_schema.users_email_idx on main_schema.users(email);

alter table main_schema.users
add constraint main_schema.users_city_fk
foreign key (id_city) references main_schema.city(id);


create view main_schema.city_vw as select * from main_schema.city;
create view main_schema.users_vw as select * from main_schema.users;


CREATE ALIAS getVersion FOR "org.h2.engine.Constants.getVersion";