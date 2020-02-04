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

# Treat your database as Code

There are many awesome REST-wrappers for your Databases (e.g. [PostgREST](https://github.com/PostgREST/postgrest), [pREST](https://github.com/prest/prest), [sandman2](https://github.com/jeffknupp/sandman2) and many others), but how about REST-wrapper for your own SQL-queries?

The `sql-boot` tool is a REST-like wrapper for your own SQL-queries.
No ETLs, no generated SQL, no "automagic" - sql-boot simply transform your own SQL-query to Web resources.

`sql-boot` recursively finds every *.sql files in your folders and start REST-like service and runs scripts against live databases in response to http requests.


Example
-------
Save you SQL-query to `big_cities.sql`:
````sql
select a.airport_code as code
     , a.airport_name
     , a.city
     , a.coordinates
     , a.timezone
  from bookings.airports a
 where a.city in (select aa.city
                    from bookings.airports aa
                   group by aa.city
                  having count(*) > 1)
 order by
       a.city
     , a.airport_code
````

Now `sql-boot` is ready to receive http requests (without restarting and other actions).

[Execute](http://81.23.10.106:8008/api/master_db/big_cities.sql) query on "master_db" database:
````bash
master_db/big_cities.sql
````

[Execute](http://81.23.10.106:8008/api/.*/big_cities.sql) query against all registered databases:
````bash
.*/big_cities.sql
````

[Execute](http://81.23.10.106:8008/api/.*/big_cities.sql?select=code,endpoint) query against all registered databases with specified columns:
````bash
.*/big_cities.sql?select=code,endpoint
````

[Execute](http://81.23.10.106:8008/api/master_db/big_cities.sql?select=code&orderby=code-desc) query with ordering:
````bash
master_db/big_cities.sql?select=code&orderby=code-desc
````

[Execute](http://81.23.10.106:8008/api/master_db/big_cities.sql?select=code&orderby=code-desc&page=1,3) query with pagination:
````bash
master_db/big_cities.sql?select=code&orderby=code-desc&page=1,3
````


Self-documentation
------------------
sql-boot uses the [OpenAPI](https://github.com/OAI/OpenAPI-Specification) standard to generate up-to-date documentation for APIs based your SQL-queries metadata.
You can use a tool like [Swagger-UI](https://github.com/swagger-api/swagger-ui) or [Swagger-Editor](https://github.com/swagger-api/swagger-editor) to render interactive documentation (for demo requests) or [generate client API](https://github.com/swagger-api/swagger-codegen) against the live API server.


How to pronounce
----------------
It is pronounced "sequelboot" - https://translate.google.com/?source=osdd#en/en/sequelboot


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
