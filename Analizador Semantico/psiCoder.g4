grammar psiCoder;

principal : sentencia 'funcion_principal' cuerpo 'fin_principal' sentencia ;
sentencia : estructura sentencia |
            declaracion sentencia |
            funcion sentencia |  ;

cuerpo : ciclos cuerpo |
         condicional cuerpo |
         seleccionMultiple cuerpo |
         declaracion cuerpo |
         asignacionVarDecla cuerpo |
         captura cuerpo |
         impresion cuerpo |
         llamarFuncion cuerpo |
         ;

declaracion : tipoDeDato ID asignacion otraDeclarcion TK_PYC;
asignacion : TK_ASIG expresion | ;
otraDeclarcion : TK_COMA ID asignacion otraDeclarcion | ;

asignacionVarDecla : (ID|IDESTRUCTURA) TK_ASIG expresion TK_PYC ; // no se permite vacio

expresion : (|TK_NEG) ( (valor|llamarFuncion) otraExpresion | TK_PAR_IZQ expresionConPar );
expresionConPar : expresion TK_PAR_DER otraExpresion ;
otraExpresion: (operador|) expresion | ; // exception

// romper
romper : 'romper' TK_PYC | ;

// condicional
condicional : 'si' TK_PAR_IZQ expresion TK_PAR_DER 'entonces' cuerpo romper enOtroCaso 'fin_si';
enOtroCaso : 'si_no' cuerpo romper | ;

// ciclos
ciclos : cicloMientras | cicloPara |  cicloHacer;
cicloMientras : 'mientras'  TK_PAR_IZQ expresion TK_PAR_DER 'hacer' cuerpo romper 'fin_mientras'  ;
cicloPara :  'para'  TK_PAR_IZQ (tipoDeDato|) ID TK_ASIG expresion TK_PYC expresion TK_PYC (NUMEROENTERO|ID) TK_PAR_DER 'hacer' cuerpo romper 'fin_para';
cicloHacer : 'hacer' cuerpo romper 'mientras' TK_PAR_IZQ expresion TK_PAR_DER TK_PYC;

// seleccion multiple
seleccionMultiple : 'seleccionar' TK_PAR_IZQ (ID|IDESTRUCTURA) TK_PAR_DER 'entre' casos casoDefecto 'fin_seleccionar' ;
casos : 'caso' (ID|NUMEROENTERO) TK_DOSP cuerpo romper casos | ;
casoDefecto : 'defecto' TK_DOSP cuerpo romper | ;

// captura e impresion
captura : 'leer' TK_PAR_IZQ (ID|IDESTRUCTURA) TK_PAR_DER TK_PYC ;

impresion : 'imprimir' TK_PAR_IZQ expresion otrosParametros TK_PAR_DER TK_PYC;
otrosParametros : TK_COMA expresion otrosParametros | ;

// llamar funciones
llamarFuncion : (ID|IDESTRUCTURA) TK_PAR_IZQ  argLLamada  TK_PAR_DER (TK_PYC|)  ;
argLLamada : expresion otrosargLLamada | ;
otrosargLLamada : TK_COMA expresion otrosargLLamada | ;

// funciones
funcion : 'funcion' tipoDeDato ID TK_PAR_IZQ argumentos TK_PAR_DER 'hacer' cuerpo 'retornar' expresion TK_PYC 'fin_funcion';
argumentos : tipoDeDato ID otrosArgumentos | ;
otrosArgumentos : TK_COMA tipoDeDato ID otrosArgumentos | ;

// estructura
estructura : 'estructura' ID decEnEstructura funEstructura 'fin_estructura';
decEnEstructura : tipoDeDato ID otradecEnEstructura TK_PYC decEnEstructura | ;
otradecEnEstructura : TK_COMA ID otradecEnEstructura | ;
funEstructura : funcion funEstructura | ;

// Reglas lexicas
tipoDeDato : ( 'entero'|'real'|'caracter'|'cadena'|'booleano'|ID);
valor :  (VBOOLEANO|NUMEROREAL|NUMEROENTERO|CARACTER|CADENA|IDESTRUCTURA|ID) ;
CARACTER : TK_COMILLA_SEN ([a-zA-Z0-9]|' '|'_') TK_COMILLA_SEN;
CADENA : '"' ([a-zA-Z0-9]|' '|'_'|'.'|'\\'|':'|'!'|'*'|'^'|','|'-')* '"';
NUMEROENTERO : (|'-')[0-9]+ ; // falta hacer retriccion de overflow
NUMEROREAL : (|'-') [0-9]+ TK_PUNTO [0-9]+;
ID : (|'-')[a-zA-Z][a-zA-Z0-9]* ;
IDESTRUCTURA : (|'-')[a-zA-Z][a-zA-Z0-9]* (TK_PUNTO [a-zA-Z][a-zA-Z0-9]*)+;
VBOOLEANO : ( 'verdadero' | 'falso' );
operador : ( TK_MAS | TK_MENOS | TK_DIV | TK_MULT | TK_MOD | TK_MENOR | TK_MAYOR | TK_MENOR_IGUAL | TK_MAYOR_IGUAL | TK_Y | TK_O | TK_DIF | TK_IGUAL | TK_NEG) ;
WS : ([ \t\r\n]+ |  '//' ~( '\r' | '\n' )*  |  '/*' .*? '*/' ) -> skip;

// Tokens
TK_MAS : '+';
TK_MENOS : '-';
TK_MULT : '*';
TK_DIV : '/';
TK_MOD : '%';
TK_ASIG : '=';
TK_MENOR : '<';
TK_MAYOR : '>';
TK_MENOR_IGUAL : '<=';
TK_MAYOR_IGUAL : '>=';
TK_IGUAL : '==';
TK_Y : '&&';
TK_O : '||';
TK_DIF : '!=';
TK_NEG : '!';
TK_DOSP : ':';
TK_COMILLA_SEN : '\'';
TK_COMILLA_DOB : '\"';
TK_PYC : ';';
TK_COMA : ',';
TK_PUNTO : '.';
TK_PAR_IZQ : '(';
TK_PAR_DER : ')';
