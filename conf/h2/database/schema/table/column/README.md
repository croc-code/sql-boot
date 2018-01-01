````sql
  select _schema                    /* table schema */
       , _table                     /* table name */
       , _column                    /* column name */
       , type_name                  /* data source dependent type name, for a UDT the type name is fully qualified */
       , character_maximum_length   /* column size */
       , nullable                   /* is NULL allowed */
       , column_default /* default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be null) */
   from (select c.table_schema               as _schema
              , c.table_name                 as _table
              , c.column_name                as _column
              , c.type_name
              , c.character_maximum_length
              , c.nullable
              , c.column_default
           from information_schema.columns c);
````