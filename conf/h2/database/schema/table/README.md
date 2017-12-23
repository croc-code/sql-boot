````sql
select @schema              /* table schema */
     , @table               /* table name */
     , remarks              /* explanatory comment on the table */
     , last_modification    /* last modification */
     , table_type           /* table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM"*/
  from (select t.table_schema as "@schema"
             , t.table_name as "@table"
             , t.remarks
             , t.last_modification
             , t.table_type
          from information_schema.tables t
         where lower(t.table_schema) like lower('${uri.path(0)}')
           and lower(t.table_name) like lower('${uri.path(1)}'));
````