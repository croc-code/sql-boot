select "@schema"
     , "@table"
     , "@session"
 from (select TABLE_SCHEMA as "@schema"
            , TABLE_NAME   as "@table"
            , SESSION_ID   as "@session"
            , LOCK_TYPE
         from information_schema.LOCKS)
