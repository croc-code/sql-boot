# Getting DB Done!

Evolutionary, Transparent and Polyglot DB-tool for Developers, DBA and DevOps Engineers

- Create and manage your DB source code (DDL, DML, etc)
- DB independent (use native old SQL)
- Multiplatform (REST/Java based)

Try online:
-----------

- [table/hr](https://sql-boot.herokuapp.com/api/table/hr) - get all table from "hr" schema
- [table/hr.jobs](https://sql-boot.herokuapp.com/api/table/hr.jobs) - get table "hr.jobs"
- [table/hr.users/](https://sql-boot.herokuapp.com/api/table/hr.users/) - get table "hr.users" with child objects (pk, fk, indexes etc)
- [index/hr.users](https://sql-boot.herokuapp.com/api/index/hr.users) - get all indexes for table "hr.users"
- [index/hr](https://sql-boot.herokuapp.com/api/index/hr) - get all indexes from "hr" schema
- [index/hr.p*](https://sql-boot.herokuapp.com/api/index/hr.p*) - get all indexes from "hr" schema and starting with "p"
- [pk/hr](https://sql-boot.herokuapp.com/api/pk/hr) - get all pk from "hr" schema
- [fk/*](https://sql-boot.herokuapp.com/api/fk/*) - get all fk from all db schemas

- [table/bookings/-](https://sql-boot.herokuapp.com/api/table/bookings/-) - drop all table from "bookings" schema 
- [index/hr.*.*index*/-](https://sql-boot.herokuapp.com/api/index/hr.*.*index*/-) - drop all table from "bookings" schema with specific name

- [table/hr?type=migration](https://sql-boot.herokuapp.com/api/table/hr?type=migration) - migration for create all tables 
- [table/hr/-?type=migration](https://sql-boot.herokuapp.com/api/table/hr/-?type=migration) - migration for drop all tables 
- [table/hr?type=html](https://sql-boot.herokuapp.com/api/table/hr?type=html) - get all tales from "hr" schema in html view 
- [table/*/?type=zip](https://sql-boot.herokuapp.com/api/table/*/?type=zip) - get all tales from "hr" in zip file 

Run with Docker:
----------------
docker run -t -p 8080:8080 mgramin/sql-boot

Build from source:
------------------
git clone https://github.com/mgramin/sql-boot  
cd sql-boot  
mvn package 

[![Build Status](https://travis-ci.org/mgramin/sql-boot.svg?branch=master)](https://travis-ci.org/mgramin/sql-boot)