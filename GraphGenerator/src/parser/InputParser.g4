grammar InputParser;

WS: ' '+ -> skip;
NL: '\n';

fragment CHAR: [a-z]|[A-Z];
fragment NUM: [0-9];
fragment US: '_';
fragment DQ: '"';
fragment DIST: U|u|E|e|G|g|P|p;

INT: NUM+;
DOUBLE: NUM+ '.' NUM*;


parse: file EOF;

beginning
    : INT
    ;

