````sql
  select * from (
    select c.table_schema       as "@schema"
         , c.table_name         as "@table"
         , c.constraint_name    as "@pk"
      from information_schema.constraints c
     where constraint_type  = 'PRIMARY KEY')
````