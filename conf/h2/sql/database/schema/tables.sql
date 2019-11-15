/*
{ 
  "name": "table", 
  "title": "Tables",
  "icon": "table_chart",
  "tags": "ui,schema"
}
*/
select table_schema      /* { "label": "Owner", "description": "Owner of the table", "visible": false } */
     , table_name        /* { "label": "Name", "description": "Name of the table" } */
     , table_type        /* { "label": "Type", "description": "Typical types", "visible": true, "values": ['TABLE', 'VIEW', 'SYSTEM TABLE', 'GLOBAL TEMPORARY', 'LOCAL TEMPORARY', 'ALIAS', 'SYNONYM'] } */
     , remarks           /* { "label": "Comment", "description": "Comments", "visible": true } */
     , last_modification /* { "label": "Last modification", "description": "Last modification" } */
  from information_schema.tables
