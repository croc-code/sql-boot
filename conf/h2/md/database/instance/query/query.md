````sql
/*
  { "name": "query", "title": "Queries" }
*/
select SQL_STATEMENT
     , EXECUTION_COUNT
     , MIN_EXECUTION_TIME
     , MAX_EXECUTION_TIME
     , CUMULATIVE_EXECUTION_TIME
     , AVERAGE_EXECUTION_TIME
     , STD_DEV_EXECUTION_TIME
     , MIN_ROW_COUNT
     , MAX_ROW_COUNT
     , CUMULATIVE_ROW_COUNT
     , AVERAGE_ROW_COUNT
     , STD_DEV_ROW_COUNT
  from information_schema.QUERY_STATISTIC
````
