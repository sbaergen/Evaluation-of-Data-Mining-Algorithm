grammar InputParser;

//Lexar



fragment CHAR: [a-z]|[A-Z];
fragment NUM: [0-9];
fragment US: '_';
fragment DQ: '"';

NL: '\n' -> skip;

WS: [ \t\r\n]+ -> skip;
COMMENT: ('//'|'#').? NL -> skip;
MULTICOMMENT: '/*'.?'*/' -> skip;

DIST: 'U'|'u'|'E'|'e'|'G'|'g'|'P'|'p';

INT: NUM+;
DOUBLE: NUM* '.' NUM+;
ID: CHAR+;

//Parser

//parse: file EOF;

//file: (beginning NL)*;
/*
beginning:
    INT //EFGS
    INT //NODES
    INT //Length of union of all attributes
    (DOUBLE|INT) //Prob for all attributes
    DIST //Node placement
    distParams
    DIST //Edge weight
    distParams
    DIST //Node weight
    distParams
    DIST //Attribute weight
    distParams
    INT //Max attribute per pattern
    INT //Max forward edge
    INT //Max backward edge
    INT //gap value
    (DOUBLE|INT) //minSupport
    INT //max nodes per pattern
    ;

distParams
    : INT INT # Uniform
    | (DOUBLE|INT) # Exponential
    | (DOUBLE|INT) INT INT # Gaussian
    | INT # Poisson
    ;


*/
