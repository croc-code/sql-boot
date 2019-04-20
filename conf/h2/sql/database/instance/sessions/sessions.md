````sql
select "@id"          /* { "label": "id", "description": "ID", "visible": true } */
     , USER_NAME      /* { "label": "user", "description": "User name", "visible": true } */
     , session_start  /* { "label": "session start", "description": "Session start", "visible": true } */
     , STATEMENT
     , STATEMENT_START
     , CONTAINS_UNCOMMITTED
  from (select ID as "@id"
             , USER_NAME
             , session_start
             , STATEMENT
             , STATEMENT_START
             , CONTAINS_UNCOMMITTED
          from information_schema.sessions)
````
