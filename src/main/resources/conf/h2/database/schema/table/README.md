````sql
select * from (
  select t.table_schema       as "schema"
       , t.table_name         as "table"
       , t.remarks            as "@comment"
       , t.last_modification  as "@last_date"
       , t.table_type         as "@table_type"
    from information_schema.tables t
   where lower(t.table_schema) like '$schema'
     and lower(t.table_name) like '$table'
   order by t.table_name)
````