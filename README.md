# Database as a Code!

[![Build Status](https://travis-ci.org/mgramin/sql-boot.svg?branch=master)](https://travis-ci.org/mgramin/sql-boot)
[![Build status](https://ci.appveyor.com/api/projects/status/h72if6ir8ehp1vwv?svg=true)](https://ci.appveyor.com/project/mgramin/sql-boot)
[![Coverage Status](https://coveralls.io/repos/github/mgramin/sql-boot/badge.svg?branch=master)](https://coveralls.io/github/mgramin/sql-boot?branch=master)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/mgramin/sql-boot/blob/master/LICENSE)
[![codebeat badge](https://codebeat.co/badges/5f90d946-b2a2-46fe-8951-99f354b3a8e9)](https://codebeat.co/projects/github-com-mgramin-sql-boot-master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/97169221af6f4b73a5974a6a5c82cd60)](https://www.codacy.com/app/mgramin/sql-boot?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=mgramin/sql-boot&amp;utm_campaign=Badge_Grade)
[![Docker Pulls](https://img.shields.io/docker/pulls/mgramin/sql-boot.svg)](https://hub.docker.com/r/mgramin/sql-boot/)
[![Main distribution](https://img.shields.io/badge/zip-download-brightgreen.svg)](https://github.com/mgramin/sql-boot/releases/latest)
[![Join the chat at https://gitter.im/sqlboot/Lobby](https://badges.gitter.im/sqlboot/Lobby.svg)](https://gitter.im/sqlboot/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

Evolutionary, Transparent and Polyglot DB-framework for Developers, DBA and DevOps Engineers

- Create and manage your DB source code (DDL, DML, etc);
- Database reverse engineering;
- DB independent - native old SQL/*QL/JDBC/REST;
- Share expert knowledge;
- Multiplatform (REST/Java based).

Try online (on Heroku):
-----------------------
- [table/hr](https://sql-boot.herokuapp.com/api/table/hr) - get all table from "hr" schema
- [table/hr.jobs](https://sql-boot.herokuapp.com/api/table/hr.jobs) - get table "hr.jobs"
- [table/hr.users/](https://sql-boot.herokuapp.com/api/table/hr.users/) - get table "hr.users" with child objects (pk, fk, indexes etc)
- [index/hr.users](https://sql-boot.herokuapp.com/api/index/hr.users) - get all indexes for table "hr.users"
- [index/hr](https://sql-boot.herokuapp.com/api/index/hr) - get all indexes from "hr" schema
- [index/hr.p*](https://sql-boot.herokuapp.com/api/index/hr.p*) - get all indexes from "hr" schema and starting with "p"
- [pk/hr](https://sql-boot.herokuapp.com/api/pk/hr) - get all pk from "hr" schema
- [fk/*](https://sql-boot.herokuapp.com/api/fk/*) - get all fk from all db schemas


Try online (on SwaggerHub):
---------------------------
![swagger validator](http://online.swagger.io/validator?url=https://raw.githubusercontent.com/mgramin/sql-boot/master/src/main/resources/swagger.json)
https://app.swaggerhub.com/apis/mgramin/commons-sql_boot_configuration/v1


Try with Docker and embedded (H2) demo db:
------------------------------------------

```
docker run -t -p 8080:8080 mgramin/sql-boot
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
