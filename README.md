# Database as a Code_

[![Build Status](https://travis-ci.org/sql-boot/sql-boot.svg?branch=master)](https://travis-ci.org/sql-boot/sql-boot)
[![Build status](https://ci.appveyor.com/api/projects/status/vy096ig84cymr8ir?svg=true)](https://ci.appveyor.com/project/mgramin/sql-boot-hffyc)
[![Coverage Status](https://coveralls.io/repos/github/sql-boot/sql-boot/badge.svg?branch=master)](https://coveralls.io/github/sql-boot/sql-boot?branch=master)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/sql-boot/sql-boot/blob/master/LICENSE)
[![codebeat badge](https://codebeat.co/badges/5f90d946-b2a2-46fe-8951-99f354b3a8e9)](https://codebeat.co/projects/github-com-mgramin-sql-boot-master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/97169221af6f4b73a5974a6a5c82cd60)](https://www.codacy.com/app/mgramin/sql-boot?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=mgramin/sql-boot&amp;utm_campaign=Badge_Grade)
[![Docker Pulls](https://img.shields.io/docker/pulls/mgramin/sql-boot.svg)](https://hub.docker.com/r/mgramin/sql-boot/)
[![Main distribution](https://img.shields.io/badge/zip-download-brightgreen.svg)](https://github.com/sql-boot/sql-boot/releases/latest)
[![Join the chat at https://gitter.im/sqlboot/Lobby](https://badges.gitter.im/sqlboot/Lobby.svg)](https://gitter.im/sqlboot/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![EO badge](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org/)

SQL-driven* DB-management framework for Developers, DBA and DevOps (*actually, not only SQL)


- Evolutionary
- Transparent (DB independent - native old SQL/*QL/JDBC/REST)
- Polyglot


Destinations
------------
- Create and manage your DB source code (DDL, DML, etc)
- Get DB metrics
- Database reverse engineering
- Share expert knowledge
- Multiplatform (REST/Java based)

Concepts
--------
TODO

Self-documentation
------------------
sql-boot uses the [OpenAPI](https://github.com/OAI/OpenAPI-Specification) standard to generate up-to-date documentation for APIs based your SQL-queries metadata.
You can use a tool like [Swagger-UI](https://github.com/swagger-api/swagger-ui) or [Swagger-Editor](https://github.com/swagger-api/swagger-editor) to render interactive documentation (for demo requests) or [generate client API](https://github.com/swagger-api/swagger-codegen) against the live API server.

How to pronounce
----------------
It is pronounced "sequelboot" - https://translate.google.com/?source=osdd#en/en/sequelboot


Try online (on Heroku):
-----------------------
- [table/hr](https://sql-boot.herokuapp.com/api/h2/table/hr) - get all table from "hr" schema
- [table/hr.jobs](https://sql-boot.herokuapp.com/api/h2/table/hr.jobs) - get table "hr.jobs"
- [table/hr.users/](https://sql-boot.herokuapp.com/api/h2/table/hr.users/) - get table "hr.users" with child objects (pk, fk, indexes etc)
- [index/hr.users](https://sql-boot.herokuapp.com/api/h2/index/hr.users) - get all indexes for table "hr.users"
- [index/hr](https://sql-boot.herokuapp.com/api/h2/index/hr) - get all indexes from "hr" schema
- [index/hr.p*](https://sql-boot.herokuapp.com/api/h2/index/hr.p*) - get all indexes from "hr" schema and starting with "p"
- [pk/hr](https://sql-boot.herokuapp.com/api/h2/pk/hr) - get all pk from "hr" schema
- [fk/*](https://sql-boot.herokuapp.com/api/h2/fk/*) - get all fk from all db schemas



Try with Docker and embedded (H2) demo db:
------------------------------------------

```
docker run -t -p 8007:8007 mgramin/sql-boot
```

Build from source:
------------------
```
git clone https://github.com/mgramin/sql-boot
cd sql-boot
mvn package
```

Base configurations:
--------------------

[![Build Status](https://travis-ci.org/sql-boot/sql-boot-conf.svg?branch=master)](https://travis-ci.org/sql-boot/sql-boot-conf)
https://github.com/sql-boot/sql-boot-conf

[![Build Status](https://travis-ci.org/sql-boot/sql-boot-postgresql.svg?branch=master)](https://travis-ci.org/sql-boot/sql-boot-postgresql)
https://github.com/sql-boot/sql-boot-postgresql

[![Build Status](https://travis-ci.org/sql-boot/sql-boot-oracle.svg?branch=master)](https://travis-ci.org/sql-boot/sql-boot-oracle)
https://github.com/sql-boot/sql-boot-oracle

[![Build Status](https://travis-ci.org/sql-boot/sql-boot-cassandra.svg?branch=master)](https://travis-ci.org/sql-boot/sql-boot-cassandra)
https://github.com/sql-boot/sql-boot-cassandra

https://github.com/sql-boot/sql-boot-clickhouse
