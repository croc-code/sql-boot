# Getting DB Done!

Evolutionary, Transparent and Polyglot DB-tool for Developers, DBA and DevOps Engineers

Create and manage your DB source code (DDL, DML, etc).

- DB independent (use native old SQL);
- Multiplatform (REST/Java based);

Try online:
-----------
- <a href="https://sql-boot.herokuapp.com/ddl/table/hr" target="_blank">table/hr</a> - get all table from "hr" schema
- <a href="https://sql-boot.herokuapp.com/ddl/table/hr.jobs" target="_blank">hr.jobs</a> - get table "hr.jobs"
- <a href="https://sql-boot.herokuapp.com/ddl/table/hr.users/" target="_blank">table/hr.users/</a> - get table "hr.users" with child objects (pk, fk, indexes etc)
- <a href="https://sql-boot.herokuapp.com/ddl/index/hr.users" target="_blank">index/hr.users</a> - get all indexes for table "hr.users"
- <a href="https://sql-boot.herokuapp.com/ddl/index/hr" target="_blank">index/hr</a> - get all indexes from "hr" schema
- <a href="https://sql-boot.herokuapp.com/ddl/index/hr.p*" target="_blank">index/hr.p*</a> - get all indexes from "hr" schema and starting with "p"
- <a href="https://sql-boot.herokuapp.com/ddl/pk/hr" target="_blank">pk/hr</a> - get all pk from "hr" schema
- <a href="https://sql-boot.herokuapp.com/ddl/fk/*" target="_blank">fk/*</a> - get all fk from all db schemas

- <a href="https://sql-boot.herokuapp.com/ddl/table/bookings/-" target="_blank">table/bookings/-</a> - drop all table from "bookings" schema 
- <a href="https://sql-boot.herokuapp.com/ddl/index/hr.*.*index*/-" target="_blank">index/hr.*.*index*/-</a> - drop all table from "bookings" schema with specific name

- <a href="https://sql-boot.herokuapp.com/ddl/table/hr?type=migration" target="_blank">table/hr?type=migration</a> - migration for create all tables 
- <a href="https://sql-boot.herokuapp.com/ddl/table/hr/-?type=migration" target="_blank">table/hr/-?type=migration</a> - migration for drop all tables 
- <a href="https://sql-boot.herokuapp.com/ddl/table/hr?type=html" target="_blank">table/hr?type=html</a> - get all tales from "hr" schema in html view 
- <a href="https://sql-boot.herokuapp.com/ddl/table/*/?type=zip" target="_blank">table/*/?type=zip</a> - get all tales from "hr" in zip file 

Build from source:
------------------
git clone https://github.com/mgramin/sql-boot
cd sql-boot
mvn package

[![Build Status](https://travis-ci.org/mgramin/sql-boot.svg?branch=master)](https://travis-ci.org/mgramin/sql-boot)