grammar SELECT;

ID
   : ( '@' | 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )+
   ;

WS
   : ( ' ' | '\t' | '\n' | '\r' )+ -> skip
   ;

LEFT_BRACKET
   : '('
   ;

RIGHT_BRACKET
   : ')'
   ;

DOT
   : '.'
   ;

COMMA
   : ','
   ;

SEMICOLON
   : ';'
   ;

QUOTE
   : '\''
   ;

DOUBLE_QUOTE
   : '"'
   ;


SINGLE_LINE_COMMENT
   : '--' ~[\r\n]* -> skip
   ;

MULTIPLE_LINE_COMMENT
   : '/*' .*? '*/'
   ;



select_statement
   : select select_row( COMMA select_row )*
       from (schema_name+ DOT)? table_name
       .*?
   ;



select
   : 'select' | 'SELECT'
   ;

from
   : 'from' | 'FROM'
   ;

as
  : 'as' | 'AS'
  ;

schema_name
   : ID
   ;

table_name
   : (DOUBLE_QUOTE)? ID (DOUBLE_QUOTE)?
   ;

select_row
   : column_name (column_alias)? (column_comment)?
   ;

column_name
   : (table_alias DOT)? (DOUBLE_QUOTE)? ID (DOUBLE_QUOTE)?
   ;

table_alias
  : (DOUBLE_QUOTE)? ID (DOUBLE_QUOTE)?
  ;

column_alias
  : (as)? (DOUBLE_QUOTE)? ID (DOUBLE_QUOTE)?
  ;

column_comment
  : MULTIPLE_LINE_COMMENT
  ;

column_coment_left_bracket
  : '/*'
  ;

column_coment_right_bracket
  : '*/'
  ;

column_coment_text
  : '.*?'
  ;
