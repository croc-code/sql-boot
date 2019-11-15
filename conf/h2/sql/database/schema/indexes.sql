/*
{
  "name": "index",
  "title": "Indexes",
  "icon": "fa-indent",
  "tags": "ui,schema"
}
*/
select TABLE_SCHEMA     /* { "label": "owner", "description": "Owner of the table", "visible": true } */
     , TABLE_NAME       /* { "label": "table name", "description": "Name of the table", "visible": true } */
     , INDEX_NAME       /* { "label": "index name", "description": "Name of the index", "visible": true } */
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
  from information_schema.indexes i
