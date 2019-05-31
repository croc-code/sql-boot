````sql
/*
  { "name": "schema", "title": "Schema" }
*/
select "@schema"                    /* { "label": "schema", "description": "Schema", "visible": true } */
     , CATALOG_NAME                 /* { "label": "catalog", "description": "Catalog" } */
     , SCHEMA_OWNER                 /* { "label": "owner", "description": "Schema owner" } */
     , DEFAULT_CHARACTER_SET_NAME   /* { "label": "character set", "description": "DEFAULT CHARACTER SET NAME" } */
     , DEFAULT_COLLATION_NAME       /* { "label": "collation", "description": "DEFAULT COLLATION NAME" } */
     , IS_DEFAULT                   /* { "label": "default", "description": "Is default" } */
     , REMARKS                      /* { "label": "remarks", "description": "Remarks" } */
     , ID                           /* { "label": "id", "description": "ID" } */
  from (select s.SCHEMA_NAME    as "@schema"
             , s.*
          from information_schema.SCHEMATA s)
````
