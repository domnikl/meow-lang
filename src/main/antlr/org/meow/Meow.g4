grammar Meow;

/*
 * Parser rules
 */

compileUnit : (expr | COMMENT | LINE_COMMENT | NEWLINE)* DELIMITER*;

expr : value=INTEGER_LITERAL # integerLiteralExpr ;

/*
 * Lexer rules
 */

fragment LOWERCASE : [a-z] ;
fragment UPPERCASE : [A-Z] ;
fragment LETTER    : [a-zA-Z] ;
fragment DIGIT     : [0-9] ;

WHITESPACE      : (' ' | '\t')+ -> skip ;
NEWLINE         : ('\r'? '\n' | '\r')+ -> skip;
DELIMITER       : (NEWLINE | ';')+ -> skip;

LINE_COMMENT    :  '//' ~[\r\n]* (EOF|'\r'? '\n') -> skip ;
COMMENT         : '/*' .*? '*/' -> skip ; // .*? matches anything until the first */

INTEGER_LITERAL : DIGIT+ ;

IDENTIFIER
    : (LETTER | '_') (LETTER | '_' | DIGIT)*
    | '`' ~('`')+ '`'
    ;
