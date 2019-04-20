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
