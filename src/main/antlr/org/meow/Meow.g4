grammar Meow;

/*
 * Parser rules
 */

compileUnit : (expr | defineValue | COMMENT | LINE_COMMENT | NEWLINE)* DELIMITER* ;

expr : identifier=IDENTIFIER L_PARENS exprList R_PARENS # funcCallExpr
     | value=INTEGER_LITERAL                            # integerLiteralExpr
;

exprList   : (expr (',' expr)* )? ;

defineValue : KEYWORD_LET IDENTIFIER ASSIGNMENT_OPERATOR expr ;

/*
 * Lexer rules
 */

fragment LOWERCASE : [a-z] ;
fragment UPPERCASE : [A-Z] ;
fragment LETTER    : [a-zA-Z] ;
fragment DIGIT     : [0-9] ;

KEYWORD_LET         : 'let' ;

ASSIGNMENT_OPERATOR : '=' ;
L_PARENS            : '(' ;
R_PARENS            : ')' ;

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
