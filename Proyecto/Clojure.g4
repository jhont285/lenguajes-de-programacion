grammar Clojure;

principal : cuerpo;

cuerpo: TK_PAR_IZQ declaracion TK_PAR_DER cuerpo |
        TK_PAR_IZQ declaracionTem TK_PAR_DER cuerpo |
        TK_PAR_IZQ condicional TK_PAR_DER cuerpo |
        TK_PAR_IZQ funcion TK_PAR_DER cuerpo |
        TK_PAR_IZQ ciclos TK_PAR_DER cuerpo |
        TK_PAR_IZQ envolver TK_PAR_DER cuerpo | // envuelve numeros parentesis
        TK_PAR_IZQ llamarFuncion  TK_PAR_DER cuerpo |
        ;

declaracion : 'def' ID expresion ;
declaracionTem: 'let' '[' ID expresion ']';

expresion: TK_PAR_IZQ operador expresion otraExpresion TK_PAR_DER | valor | estructura | funcionAnonima | '(' (llamarFuncion|) ')';
otraExpresion: expresion otraExpresion | ;

funcionAnonima: '(' 'fn' '[' otraExpresion ']' expresion ')';

condicional: 'if' expresion '(' cuerpoAux ')' ('(' cuerpoAux ')'|) ; // el else esta dentro de cuerpo

llamarFuncion: funcionLeng (expresion|) otraExpresion; // comas en la traduccion

funcion: 'defn' ID (|CADENA) '[' expresion otraExpresion']' cuerpo (expresion|) ; // otraExpresion sirve como argumentos en la funcion

ciclos: mientras;

mientras: 'while' expresion cuerpo ;

envolver: 'do' cuerpo;

estructura:vector |
            listaEnlazada |
            mapa |
            conjunto;

vector: '(' 'vector' (expresion|) otraExpresion ')' |
        '[' (expresion|) otraExpresion ']';

listaEnlazada:  '\'(' expresion otraExpresion ')' |
                '(' 'list' expresion otraExpresion ')';

conjunto : '#{' expresion otraExpresion '}' |
            '(' ('hash-set'|'conj') expresion otraExpresion ')';

mapa: '(' 'hash-map' llave expresion otraValor ')' |
        '{' llave expresion otraValor '}';

otraValor: llave expresion otraValor | ;

cuerpoAux: declaracion | condicional | funcion | ciclos | envolver | llamarFuncion;

funcionLeng: ('print'|'println'|ID);
valor :  (VBOOLEANO|NUMEROREAL|NUMEROENTERO|CADENA|ID|TK_NULO) ;
CADENA : '"' ([a-zA-Z0-9]|' '|'_'|'.'|'\\'|':'|'!'|'*'|'^'|','|'-'|'=')* '"';
NUMEROENTERO : (|'-')[0-9]+ ; // falta hacer retriccion de overflow
NUMEROREAL : (|'-') [0-9]+ TK_PUNTO [0-9]+;
ID : (|'-')[a-zA-Z][a-zA-Z0-9]* ;
llave: ':'ID|;
VBOOLEANO : ( 'true' | 'false' );
operador : ( 'and' | 'or' | 'str' | 'not=' | TK_MAS | TK_MENOS | TK_DIV | TK_MULT | TK_MOD | TK_MENOR | TK_MAYOR | TK_MENOR_IGUAL | TK_MAYOR_IGUAL | TK_IGUAL) ;
WS : ([ \t\r\n]+ |  ';' ~( '\r' | '\n' )*  |  '/*' .*? '*/' ) -> skip;

TK_MAS : '+';
TK_MENOS : '-';
TK_MULT : '*';
TK_DIV : '/';
TK_MOD : '%';
TK_MENOR : '<';
TK_MAYOR : '>';
TK_MENOR_IGUAL : '<=';
TK_MAYOR_IGUAL : '>=';
TK_IGUAL : '='; // clojure
TK_DOSP : ':';
TK_COMILLA_SEN : '\'';
TK_COMILLA_DOB : '\"';
TK_PYC : ';';
TK_COMA : ',';
TK_PUNTO : '.';
TK_PAR_IZQ : '(';
TK_PAR_DER : ')';
TK_NULO : 'nil';
