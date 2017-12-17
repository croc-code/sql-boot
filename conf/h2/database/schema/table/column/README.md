````sql
  select @schema                    /* table schema */
       , @table                     /* table name */
       , @column                    /* column name */
       , type_name                  /* data source dependent type name, for a UDT the type name is fully qualified */
       , character_maximum_length   /* column size */
       , nullable                   /* is NULL allowed */
       , column_default /* default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be null) */
   from (select c.table_schema               as "@schema"
              , c.table_name                 as "@table"
              , c.column_name                as "@column"
              , c.type_name
              , c.character_maximum_length
              , c.nullable
              , c.column_default
           from information_schema.columns c)
````