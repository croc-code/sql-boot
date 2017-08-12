````sql
  select * from (
    select c.table_schema               as "@schema"
         , c.table_name                 as "@table"
         , c.column_name                as "@column"
         , c.type_name
         , c.character_maximum_length
         , c.nullable
         , c.column_default
    from information_schema.columns c)
````