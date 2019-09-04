````sql
/*
{
  "name": "locks",
  "title": "Locks",
  "icon": "lock"
}
*/
select TABLE_SCHEMA
     , TABLE_NAME
     , SESSION_ID
     , LOCK_TYPE
  from information_schema.LOCKS
````
