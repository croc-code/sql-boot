# malewicz

# A hackable GUI SQL-manager written on SQL itself

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/1b7fb7abccda4a16a4f698f40d3bd4dc)](https://app.codacy.com/app/mgramin/malewicz?utm_source=github.com&utm_medium=referral&utm_content=sql-boot/malewicz&utm_campaign=Badge_Grade_Dashboard)
[![Build Status](https://travis-ci.org/sql-boot/malewicz.svg?branch=master)](https://travis-ci.org/sql-boot/malewicz)

:warning: This project is in early stage of development

Backend powered by [sql-boot](https://github.com/sql-boot/sql-boot)

### Try online demo

- [oracle/table/scott](http://217.73.63.31:8080/#/oracle/table/scott) - show all tables in "scott" schema for "oracle" database (base on [this](https://github.com/sql-boot/sql-boot-oracle/blob/master/database/schema/table/README.md) SQL-query)
- [oracle/table/oracle/table/scott?page=1,2](http://217.73.63.31:8080/#/oracle/table/scott?page=1,2) - show all tables in "scott" schema with pagination (base on [this](https://github.com/sql-boot/sql-boot-oracle/blob/master/database/schema/table/README.md) SQL-query)
- [oracle/table/scott.emp](http://217.73.63.31:8080/#/oracle/table/scott.emp) - show "scott.emp" table (base on [this](https://github.com/sql-boot/sql-boot-oracle/blob/master/database/schema/table/README.md) SQL-query)
- [oracle/table/scott.dept](http://217.73.63.31:8080/#/oracle/table/scott.dept) - show "scott.dept" table (base on [this](https://github.com/sql-boot/sql-boot-oracle/blob/master/database/schema/table/README.md) SQL-query)
- [oracle/column/scott.emp](http://217.73.63.31:8080/#/oracle/column/scott.emp) - show all column for table "scott.emp" (base on [this](https://github.com/sql-boot/sql-boot-oracle/blob/master/database/schema/table/column/README.md) SQL-query)


- [oracle/active_session](http://217.73.63.31:8080/#/oracle/active_session/) - show active sessions (base on [this](https://github.com/sql-boot/sql-boot-oracle/blob/master/database/instance/active_session/README.md) SQL-query)
- [oracle/table_size_rating](http://217.73.63.31:8080/#/oracle/table_size_rating) - show tables size (base on [this](https://github.com/sql-boot/sql-boot-oracle/blob/master/database/storage/tablespace/table_size_rating/README.md) SQL-query)
- [oracle/locks](http://217.73.63.31:8080/#/oracle/locks) - show object locks (base on [this](https://github.com/sql-boot/sql-boot-oracle/blob/master/database/instance/locks/README.md) SQL-query)


### Try local demo

1. Run demo
```bash
git clone https://github.com/sql-boot/malewicz.git
cd malewicz
docker-compose up -d --build
http://localhost:8080
```

2. Go to the sql-boot/conf/sql-boot-oracle and play with scripts
