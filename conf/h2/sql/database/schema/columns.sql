/*
{
  "name": "column",
  "title": "Columns",
  "icon": "view_column",
  "tags": "ui,schema"
}
*/
select table_schema               /* { "label": "owner", "description": "Owner of the table", "visible": true } */
     , table_name                 /* { "label": "table name", "description": "Name of the table", "visible": true } */
     , column_name                /* { "label": "column name", "description": "Name of the column", "visible": true } */
     , type_name                  /* { "label": "type name", "description": "Data source dependent type name, for a UDT the type name is fully qualified", "visible": true } */
     , character_maximum_length   /* { "label": "size", "description": "Column size", "visible": true } */
     , nullable                   /* { "label": "nullable", "description": "Is NULL allowed", "visible": true } */
     , column_default             /* { "label": "default value", "description": "Default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be null)" } */
  from information_schema.columns
