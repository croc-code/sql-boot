````sql
  select _schema    /* table schema */
       , _table     /* table name */
       , _pk        /* primary key name */
    from (select c.table_schema       as _schema
               , c.table_name         as _table
               , c.constraint_name    as _pk
            from information_schema.constraints c
           where constraint_type  = 'PRIMARY KEY');
````