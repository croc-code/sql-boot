````sql
/*
{ 
  "name": "table", 
  "title": "Tables",
  "icon": "table_chart"
}
*/
select "@schema"            /* { "label": "Owner", "description": "Owner of the table", "visible": false } */
     , "@table"             /* { "label": "Name", "description": "Name of the table" } */
     , table_type           /* { "label": "Type", "description": "Typical types", "visible": true, "values": ['TABLE', 'VIEW', 'SYSTEM TABLE', 'GLOBAL TEMPORARY', 'LOCAL TEMPORARY', 'ALIAS', 'SYNONYM'] } */
     , remarks              /* { "label": "Comment", "description": "Comments", "visible": true } */
     , last_modification    /* { "label": "Last modification", "description": "Last modification" } */
  from (select t.table_schema as "@schema"
             , t.table_name   as "@table"
             , t.*
          from information_schema.tables t
         where lower(t.table_schema) like lower('${uri.path(0)}')
           and lower(t.table_name) like lower('${uri.path(1)}'))
````

````sql
/*
{ 
  "name": "column", 
  "title": "Columns",
  "icon": "view_column"
}
*/
select "@schema"                  /* { "label": "owner", "description": "Owner of the table", "visible": true } */
     , "@table"                   /* { "label": "table name", "description": "Name of the table", "visible": true } */
     , "@column"                  /* { "label": "column name", "description": "Name of the column", "visible": true } */
     , type_name                  /* { "label": "type name", "description": "Data source dependent type name, for a UDT the type name is fully qualified", "visible": true } */
     , character_maximum_length   /* { "label": "size", "description": "Column size", "visible": true } */
     , nullable                   /* { "label": "nullable", "description": "Is NULL allowed", "visible": true } */
     , column_default             /* { "label": "default value", "description": "Default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be null)" } */
  from (select c.table_schema               as "@schema"
             , c.table_name                 as "@table"
             , c.column_name                as "@column"
             , c.*
          from information_schema.columns c)
````

````sql
/*
{ 
  "name": "pk", 
  "title": "Primary keys",
  "icon": "vpn_key"
}
*/
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

````sql
/*
  { "name": "index", "title": "Indexes" }
*/
select "@schema"    /* { "label": "owner", "description": "Owner of the table", "visible": true } */
     , "@table"     /* { "label": "table name", "description": "Name of the table", "visible": true } */
     , "@index"     /* { "label": "index name", "description": "Name of the index", "visible": true } */
     , ORDINAL_POSITION
     , COLUMN_NAME
     , CARDINALITY
     , PRIMARY_KEY
     , INDEX_TYPE_NAME
     , IS_GENERATED
     , INDEX_TYPE
     , ASC_OR_DESC
     , PAGES
     , FILTER_CONDITION
     , REMARKS
     , SQL
     , ID
     , SORT_TYPE
     , CONSTRAINT_NAME
     , INDEX_CLASS
     , AFFINITY
  from (select TABLE_SCHEMA as "@schema"
             , TABLE_NAME   as "@table"
             , INDEX_NAME   as "@index"
             , i.*
          from information_schema.indexes i)
````
