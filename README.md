# Treat your database as Code

[![Build Status](https://travis-ci.org/crocinc/sql-boot.svg?branch=master)](https://travis-ci.org/crocinc/sql-boot)
[![Build status](https://ci.appveyor.com/api/projects/status/jrpy23nvd03hcocb?svg=true)](https://ci.appveyor.com/project/mgramin/sql-boot)
[![codecov](https://codecov.io/gh/CrocInc/sql-boot/branch/master/graph/badge.svg)](https://codecov.io/gh/CrocInc/sql-boot)
[![License](https://img.shields.io/badge/License-BSD%203--Clause-blue.svg)](https://github.com/sql-boot/sql-boot/blob/master/LICENSE)
[![codebeat badge](https://codebeat.co/badges/da168874-4940-41f4-a6e2-ae1ddc0f3873)](https://codebeat.co/projects/github-com-mgramin-sql-boot-master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/dff14223d4e94de58f2b6a3ac1c24036)](https://www.codacy.com/manual/mgramin/sql-boot?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=CrocInc/sql-boot&amp;utm_campaign=Badge_Grade)
[![Docker Pulls](https://img.shields.io/docker/pulls/mgramin/sql-boot.svg)](https://hub.docker.com/r/mgramin/sql-boot/)
[![Main distribution](https://img.shields.io/badge/zip-download-brightgreen.svg)](https://github.com/sql-boot/sql-boot/releases/latest)
[![Join the chat at https://gitter.im/sqlboot/Lobby](https://badges.gitter.im/sqlboot/Lobby.svg)](https://gitter.im/sqlboot/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![EO badge](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org/)
[![DevOps By Rultor.com](http://www.rultor.com/b/CrocInc/sql-boot)](http://www.rultor.com/p/CrocInc/sql-boot)
[![Hits-of-Code](https://hitsofcode.com/github/CrocInc/sql-boot)](https://hitsofcode.com/view/github/CrocInc/sql-boot)
[![Scc Count Badge](https://sloc.xyz/github/CrocInc/sql-boot/)](https://github.com/CrocInc/sql-boot/)
[![Mentioned in Awesome database tools](https://awesome.re/mentioned-badge-flat.svg)](https://github.com/mgramin/awesome-db-tools)


Advanced REST-wrapper for your own SQL-queries (actually not only SQL).

1. Write query
2. Put to sql-boot folder structure
3. Get data from URI, e.g. prod_db/hr.persons?select=name,address&page=2&orderby=name+desc

Feel the Power of SQL
---------------------------------------------------------
Simple integration your sql-scripts to you infrastructure


Share expert knowledge
----------------------
[![Build Status](https://travis-ci.org/sql-boot/sql-boot-oracle.svg?branch=master)](https://travis-ci.org/sql-boot/sql-boot-oracle)
https://github.com/sql-boot/sql-boot-oracle

[![Build Status](https://travis-ci.org/sql-boot/sql-boot-postgresql.svg?branch=master)](https://travis-ci.org/sql-boot/sql-boot-postgresql)
https://github.com/sql-boot/sql-boot-postgresql

[![Build Status](https://travis-ci.org/sql-boot/sql-boot-cassandra.svg?branch=master)](https://travis-ci.org/sql-boot/sql-boot-cassandra)
https://github.com/sql-boot/sql-boot-cassandra


Self-documentation
------------------
sql-boot uses the [OpenAPI](https://github.com/OAI/OpenAPI-Specification) standard to generate up-to-date documentation for APIs based your SQL-queries metadata.
You can use a tool like [Swagger-UI](https://github.com/swagger-api/swagger-ui) or [Swagger-Editor](https://github.com/swagger-api/swagger-editor) to render interactive documentation (for demo requests) or [generate client API](https://github.com/swagger-api/swagger-codegen) against the live API server.

How to pronounce
----------------
It is pronounced "sequelboot" - https://translate.google.com/?source=osdd#en/en/sequelboot


Try online:
-----------------------
- [table/scott](http://217.73.63.31:8007/api/oracle/headers/table/scott) - get all tables from schema "scott"
- [table/scott.emp](http://217.73.63.31:8007/api/oracle/headers/table/scott.emp) - get table "hr.emp"
- [index/scott](http://217.73.63.31:8007/api/oracle/headers/index/scott) - get all indexes from "scott" schema


![Croc Cloud](src/main/resources/croc_cloud.png)


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
