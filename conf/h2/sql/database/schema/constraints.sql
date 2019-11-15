/*
{
  "name": "constraint",
  "title": "Constraints",
  "icon": "fa-key",
  "tags": "ui,schema"
}
*/
  select table_schema     /* { "label": "owner", "description": "Owner of the table", "visible": true } */
       , table_name       /* { "label": "table name", "description": "Name of the table", "visible": true } */
       , constraint_name  /* { "label": "pk", "description": "Primary key name", "visible": true } */
       , COLUMN_LIST
       , REMARKS
       , CHECK_EXPRESSION
    from information_schema.constraints
