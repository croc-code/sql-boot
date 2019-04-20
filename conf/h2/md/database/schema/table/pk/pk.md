````sql
  select "@schema"    /* { "label": "owner", "description": "Owner of the table", "visible": true } */
       , "@table"     /* { "label": "table name", "description": "Name of the table", "visible": true } */
       , "@pk"        /* { "label": "pk", "description": "Primary key name", "visible": true } */
       , COLUMN_LIST
       , REMARKS
       , CHECK_EXPRESSION
    from (select c.table_schema       as "@schema"
               , c.table_name         as "@table"
               , c.constraint_name    as "@pk"
               , c.*
            from information_schema.constraints c)
````
