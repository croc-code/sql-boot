````sql
select * from (
  select t.table_schema       as "@schema"
       , t.table_name         as "@table"
       , t.remarks
       , t.last_modification
       , t.table_type
    from information_schema.tables t)
````