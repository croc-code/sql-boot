/*
  { "name": "sessions", "title": "Sessions" }
*/
select ID             /* { "label": "id", "description": "ID", "visible": true } */
     , USER_NAME      /* { "label": "user", "description": "User name", "visible": true } */
     , session_start  /* { "label": "session start", "description": "Session start", "visible": true } */
     , STATEMENT
     , STATEMENT_START
     , CONTAINS_UNCOMMITTED
  from information_schema.sessions
