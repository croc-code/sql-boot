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
