# Schema

### get_all_tables
````sql
select u.username     as "@schema"
     , u.user_id      as "user_id"
     , u.created      as "created"
  from all_users u
 order by u.username
````
```sql
select DBMS_DDL.get_metadata(...)
  from dual
```

### row_count

````sql
select count(1)
  from all_users u
 order by u.username
````
