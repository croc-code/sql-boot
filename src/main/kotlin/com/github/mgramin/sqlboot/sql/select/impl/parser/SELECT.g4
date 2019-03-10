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

CASE_EXPRESSION
  : 'case' .*? 'end'
  ;

FUNCTION_EXPRESSION
  : ID '(' .*? ')'
  ;

WITH_EXPRESSION
  : 'with' LEFT_BRACKET .*? RIGHT_BRACKET
  ;





select_statement
   : (with)*
     select select_row( COMMA select_row )*
     from (schema_name+ DOT)? table_name
       .*?
   ;

with
    : with_expression
    ;

with_expression
  : 'with' ID 'as' LEFT_BRACKET .*? RIGHT_BRACKET (COMMA ID 'as' LEFT_BRACKET .*? RIGHT_BRACKET)*
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
   : (column_name | column_case_expression | column_function_expression) (column_alias)? (column_comment)?
   ;

column_name
   : (table_alias DOT)? (DOUBLE_QUOTE)? ID (DOUBLE_QUOTE)?
   ;

column_case_expression
  : CASE_EXPRESSION
  ;

column_function_expression
  : FUNCTION_EXPRESSION
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
