````sql
select "@schema"            /* { "label": "owner", "description": "Owner of the table", "visible": true } */
     , "@table"             /* { "label": "table name", "description": "Name of the table", "visible": true } */
     , remarks              properties
     , last_modification    /* { "label": "last modification", "description": "Last modification" } */
     , table_type           /* { "label": "table type", "description": "Typical types", "visible": true, "values": ['TABLE', 'VIEW', 'SYSTEM TABLE', 'GLOBAL TEMPORARY', 'LOCAL TEMPORARY', 'ALIAS', 'SYNONYM'] } */
  from (select t.table_schema as "@schema"
             , t.table_name   as "@table"
             , t.*
          from information_schema.tables t
         where lower(t.table_schema) like lower('${uri.path(0)}')
           and lower(t.table_name) like lower('${uri.path(1)}'))
order by "@schema", "@table"
````
