grammar Mantra;

@header {package mantra;}

compilationUnit : packageDef? (function|clazz|interfaze|enumb)* EOF ;

packageDef
	:	'package' packageName
	;

clazz
    :   ('api'|'abstract')* 'class' ID typeArgumentNames? ('extends' type)? ('implements' type (',' type)*)?
        '{'
            clazzMember*
            function*
        '}'
    ;

interfaze
    :   'api'? 'interface' ID ('extends' type (',' type)*)?
        '{'
//            field*
            functionHead*
        '}'
    ;

enumb
    :   'enum' ID '{' ID (',' ID)* '}'
    ;

clazzMember
    :   clazz
    |	interfaze
    |	field
    |	memberFunction
    |   enumb
    ;

field:  ('static'|'api')? vardecl
// 	|	'api'? propdecl
 	|	'api'? valdecl
 	;

commands : stat+ ;

memberFunction
	:	('static'|'api'|'overload')* function
	;

function
    :   functionHead block
    ;

functionHead
    :   'def' ID functionSignature
    ;

functionSignature
    :   '(' argList? ')' (':' type)?
    ;

argList
    :   argDef (',' argDef)*
    ;

argDef
    :   decl ('=' expression)?  // const exprs
    ;

lambdaSignature // no explicit return types in lambdas
    :   '(' ')'				 // for use like () => 42.0
	|	'(' decl (',' decl)* ')'		 // (x:int) => x*2
    |   ID 					 // shorthand for inferred arg
    |   '(' ID (',' ID)* ')' // shorthand for inferred args
    ;

blockArgs // no explicit return types in blocks { x,y | return x*y }
    :   decl (',' decl)* 	 // { x:int | ... }
    |   ID (',' ID)* 		 // shorthand for inferred arg types
    ;

funcSignature
    :   '(' ')' ':' type
    |	'(' ')'
	|	'(' argList ')' ':' type
	|	'(' argList ')'
    ;

/*
propdecl
    :   'property' decl ('=' expression)?
    |   'property' ID ('=' expression)?
    ;
*/

/*
TODO: add
    public Scope scope;   // set by Def.g; ID lives in which scope?
    public Symbol symbol; // set by Ref.g; point at def in symbol table
*/
vardecl
    :   'var' decl ('=' expression)?
    |   'var' ID (',' ID)* ('=' expression)? // type inf can use multiple assign on left
    ;

valdecl
    :   'val' decl '=' expression
    |   'val' ID '=' expression
    ;

decl:   ID ':' type ;

type:	classOrInterfaceType ('[' ']')*
    |	builtInType typeArguments? ('[' ']')*
    |	tupleType ('[' ']')* // (int, float)[100]
	|   functionType ('[' ']')* // func<(x:int)>[100]
	;

tupleType // ordered list of types; tuples accessed with t[1], t[2], etc...
	:	'(' type (',' type)+ ')'
	;

functionType
    :   'func' '<' '(' argList? ')' ':' type '>'
    |	'func' '<' '(' argList ')' '>'
    |   'func' '<' '(' type ')' ':' type '>' // single argument doesn't need name
    |   'func' '<' '(' type ')' '>'
    |	'func' // ref to func with no args, no return value like {}
    ;

classOrInterfaceType
	:	qualifiedName typeArguments?
	;

typeArguments
    :   '<' type (',' type)* '>'
    ;

typeArgumentNames // only built-in types can use this like llist<string>
    :   '<' ID (',' ID)* '>'
    ;

builtInType
    :   'boolean'
    |   'char'
    |   'byte'
    |   'short'
    |   'int'
    |   'long'
    |   'float'
    |   'double'
	|	'string'

    |	(	'map'
		|	'tree'
		|	'list'
		|	'llist'
		|	'set'
		)
		typeArguments?
	;

block : '{' stat* '}' ;

stat:   lvalue (',' lvalue)* assignmentOperator expression
    |   expression // calls, pipelines, ...
    |   vardecl
    |   valdecl
    |   'for' expression block
    |   'while' expression block
    |   'if' expression block ('else' block)?
    |   'return' expression
    |   'do' block 'while' expression ';'
    |   'try' block (catches finallyBlock? | finallyBlock)
    |   'switch' expression '{' switchCase+ ('default' ':' (stat|block)?)? '}'
    |   'return' expression?
    |   'throw' expression
    |   'break'
    |   'continue'
    |   'print' expression
    |   'throw' expression
    |   clazz
    |   interfaze
    |   function
    |   enumb
    ;

switchCase
    :   'case' expression (',' expression)* ':' (stat|block)?
    ;

catches
    :   catchClause+
    ;

catchClause
    :   'catch' '(' catchType ID ')' block
    ;

catchType
	:	qualifiedName ('|' qualifiedName)*
	;

finallyBlock
	:	'finally' block
	;

argExprList
    :   expression
    |   ID '=' expression (',' ID '=' expression)*
    ;

lvalue
	:	ID
	|	expression '[' expression ']'
	|	expression '.' ID
	;

expression
	:   primary
    |   expression '.' ID
    |   expression '[' expression ']'
    |   expression ('++' | '--')
    |	'len' '(' expression ')' // calls expression.size()
    |   expression '(' argExprList? ')' lambda?
    |   ('+'|'-'|'++'|'--') expression
    |   ('~'|'!') expression
    |   expression ('*'|'/'|'%') expression
    |   expression ('+'|'-') expression
    |   expression ('<' '<' | '>' '>' '>' | '>' '>') expression
    |   expression ('<=' | '>=' | '>' | '<') expression
	|   expression 'instanceof' type
	|   expression ('==' | '!=' | 'is') expression
	|   expression '&' expression
	|   expression '^' expression
	|   expression '|' expression
	|	expression ':' expression // range
	|   expression 'and' expression
	|   expression 'or' expression
	|   expression 'in' expression
	|   expression '?' expression ':' expression
	|	expression pipeOperator	expression
    ;

pipeOperator
	:	'=>' '[' expression ']'
	|	'=>*'
	|	'*=>' // pipe, merge
	|	'=>'
    ;

primary
	:	'(' expression ')'
    |	tuple
    |   'this'
    |   'super'
    |   literal
    |   type '.' 'class'
    |   list
    |   map
    |   set
    |   ctor
    |   lambda
    |   ID // string[] could match string here then [] as next statement; keep this last
    ;

tuple:	'(' expression (',' expression)+ ')' ; // can also be a tuple of pipelines, yielding pipeline graph

// ctor (ambig with call)
ctor:	classOrInterfaceType '(' argExprList? ')'  	// Button(title="foo")
	|	builtInType          '(' argExprList? ')'  	// int(), string()
	|	classOrInterfaceType ('[' expression? ']')+	// User[10][] list of 10 User lists of unspecified initial size
	|	builtInType          ('[' expression? ']')+ // int[] list of ints with unspecified initial size, int[10] 10 initial ints
	;

list:   '[' ']' // type inferred
    |   '[' expression (',' expression)* ']'
    ;

map:   '[' mapElem (',' mapElem)* ']'
    ;

mapElem
    :   expression '=' expression
    ;

// special case for convenience set(1,2), set<User>(User(), User())
set :   'set' typeArguments? '(' expression (',' expression)* ')' ;

lambda
    :   lambdaSignature '->' expression   // special case single expression
    |   '{' (blockArgs '|')? stat+ '}'
    |   '{' '}' // empty lambda
    ;

/** mantra::lang is a package name */
packageName
	:	ID ('::' ID)*
	;

/** mantra::lang::Object is a qualified class name in package mantra::lang */
qualifiedName
    :   packageName '::' ID ('.' ID)*
    |	ID ('.' ID)*
    ;

literal
    :   IntegerLiteral
    |   FloatingPointLiteral
    |   CharacterLiteral
    |   StringLiteral
    |   BooleanLiteral
    |   'nil'
    ;

assignmentOperator
    :   '='
    |   '+='
    |   '-='
    |   '*='
    |   '/='
    ;

ABSTRACT : 'abstract';
API : 'api';
ASSERT : 'assert';
BOOLEAN : 'boolean';
BREAK : 'break';
BYTE : 'byte';
CASE : 'case';
CATCH : 'catch';
CHAR : 'char';
CLASS : 'class';
CONST : 'const';
CONTINUE : 'continue';
DEFAULT : 'default';
DO : 'do';
DOUBLE : 'double';
ELSE : 'else';
ENUM : 'enum';
EXTENDS : 'extends';
FINAL : 'final';
FINALLY : 'finally';
FLOAT : 'float';
FOR : 'for';
IF : 'if';
GOTO : 'goto';// reserved, not used
IMPLEMENTS : 'implements';
IMPORT : 'import';
INSTANCEOF : 'instanceof';
INT : 'int';
INTERFACE : 'interface';
LEN : 'len';
LONG : 'long';
NATIVE : 'native';
OVERLOAD : 'overload';
PACKAGE : 'package';
PROPERTY : 'property'; // reserved, not used
RETURN : 'return';
SET : 'set';
SHORT : 'short';
STATIC : 'static';
SUPER : 'super';
SWITCH : 'switch';
THIS : 'this';
THROW : 'throw';
TRANSIENT : 'transient';
TRY : 'try';
WHILE : 'while';
VAR : 'var' ;
VOID : 'void' ;

// §3.10.1 Integer Literals

IntegerLiteral
	:	DecimalIntegerLiteral
	|	HexIntegerLiteral
	|	OctalIntegerLiteral
	|	BinaryIntegerLiteral
	;

fragment
DecimalIntegerLiteral
	:	DecimalNumeral IntegerTypeSuffix?
	;

fragment
HexIntegerLiteral
	:	HexNumeral IntegerTypeSuffix?
	;

fragment
OctalIntegerLiteral
	:	OctalNumeral IntegerTypeSuffix?
	;

fragment
BinaryIntegerLiteral
	:	BinaryNumeral IntegerTypeSuffix?
	;

fragment
IntegerTypeSuffix
	:	[lL]
	;

fragment
DecimalNumeral
	:	'0'
	|	NonZeroDigit (Digits? | Underscores Digits)
	;

fragment
Digits
	:	Digit (DigitsAndUnderscores? Digit)?
	;

fragment
Digit
	:	'0'
	|	NonZeroDigit
	;

fragment
NonZeroDigit
	:	[1-9]
	;

fragment
DigitsAndUnderscores
	:	DigitOrUnderscore+
	;

fragment
DigitOrUnderscore
	:	Digit
	|	'_'
	;

fragment
Underscores
	:	'_'+
	;

fragment
HexNumeral
	:	'0' [xX] HexDigits
	;

fragment
HexDigits
	:	HexDigit (HexDigitsAndUnderscores? HexDigit)?
	;

fragment
HexDigit
	:	[0-9a-fA-F]
	;

fragment
HexDigitsAndUnderscores
	:	HexDigitOrUnderscore+
	;

fragment
HexDigitOrUnderscore
	:	HexDigit
	|	'_'
	;

fragment
OctalNumeral
	:	'0' Underscores? OctalDigits
	;

fragment
OctalDigits
	:	OctalDigit (OctalDigitsAndUnderscores? OctalDigit)?
	;

fragment
OctalDigit
	:	[0-7]
	;

fragment
OctalDigitsAndUnderscores
	:	OctalDigitOrUnderscore+
	;

fragment
OctalDigitOrUnderscore
	:	OctalDigit
	|	'_'
	;

fragment
BinaryNumeral
	:	'0' [bB] BinaryDigits
	;

fragment
BinaryDigits
	:	BinaryDigit (BinaryDigitsAndUnderscores? BinaryDigit)?
	;

fragment
BinaryDigit
	:	[01]
	;

fragment
BinaryDigitsAndUnderscores
	:	BinaryDigitOrUnderscore+
	;

fragment
BinaryDigitOrUnderscore
	:	BinaryDigit
	|	'_'
	;

FloatingPointLiteral
	:	DecimalFloatingPointLiteral
	|	HexadecimalFloatingPointLiteral
	;

fragment
DecimalFloatingPointLiteral
	:	Digits '.' Digits? ExponentPart? FloatTypeSuffix?
	|	'.' Digits ExponentPart? FloatTypeSuffix?
	|	Digits ExponentPart FloatTypeSuffix?
	|	Digits FloatTypeSuffix
	;

fragment
ExponentPart
	:	ExponentIndicator SignedInteger
	;

fragment
ExponentIndicator
	:	[eE]
	;

fragment
SignedInteger
	:	Sign? Digits
	;

fragment
Sign
	:	[+-]
	;

fragment
FloatTypeSuffix
	:	[fFdD]
	;

fragment
HexadecimalFloatingPointLiteral
	:	HexSignificand BinaryExponent FloatTypeSuffix?
	;

fragment
HexSignificand
	:	HexNumeral '.'?
	|	'0' [xX] HexDigits? '.' HexDigits
	;

fragment
BinaryExponent
	:	BinaryExponentIndicator SignedInteger
	;

fragment
BinaryExponentIndicator
	:	[pP]
	;

// §3.10.3 Boolean Literals

BooleanLiteral
	:	'true'
	|	'false'
	;

// §3.10.4 Character Literals

CharacterLiteral
	:	'\'' SingleCharacter '\''
	|	'\'' EscapeSequence '\''
	;

fragment
SingleCharacter
	:	~['\\\r\n]
	;

StringLiteral
	:	'"' StringCharacter* '"'
	;

UNTERMINATED_STRING_LITERAL
	:  '\'' StringCharacter*
	;


fragment
StringCharacter
	:	~["\\\r\n]
	|	EscapeSequence
	;

fragment
EscapeSequence
	:	'\\' [btnfr"'\\]
	|	OctalEscape
	;

fragment
OctalEscape
	:	'\\' OctalDigit
	|	'\\' OctalDigit OctalDigit
	|	'\\' ZeroToThree OctalDigit OctalDigit
	;

fragment
ZeroToThree
	:	[0-3]
	;

Nil :	'nil'
	;

LPAREN : '(';
RPAREN : ')';
LBRACE : '{';
RBRACE : '}';
LBRACK : '[';
RBRACK : ']';
SEMI : ';';
COMMA : ',';
DOT : '.';

//ASSIGN : '=';
GT : '>';
LT : '<';
BANG : '!';
TILDE : '~';
QUESTION : '?';
COLON : ':';
EQUAL : '==';
IS : 'is' ;
IN : 'in' ;
LE : '<=';
GE : '>=';
NOTEQUAL : '!=';
AND : 'and';
OR : 'or';
INC : '++';
DEC : '--';
ADD : '+';
SUB : '-';
MUL : '*';
DIV : '/';
BITAND : '&';
BITOR : '|';
CARET : '^';
MOD : '%';
YIELDS : '->' ; // lambda
PIPE : '=>' ;
PIPE_MANY : '=>*' ;
MERGE_MANY : '*=>' ;

ADD_ASSIGN : '+=';
SUB_ASSIGN : '-=';
MUL_ASSIGN : '*=';
DIV_ASSIGN : '/=';

ID  :	JavaLetter JavaLetterOrDigit*  // follow Java conventions
	;

fragment
JavaLetter
	:	[a-zA-Z$_] // these are the "java letters" below 0xFF
	|	// covers all characters above 0xFF which are not a surrogate
		~[\u0000-\u00FF\uD800-\uDBFF]
		{Character.isJavaIdentifierStart(_input.LA(-1))}?
	|	// covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
		[\uD800-\uDBFF] [\uDC00-\uDFFF]
		{Character.isJavaIdentifierStart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?
	;

fragment
JavaLetterOrDigit
	:	[a-zA-Z0-9$_] // these are the "java letters or digits" below 0xFF
	|	// covers all characters above 0xFF which are not a surrogate
		~[\u0000-\u00FF\uD800-\uDBFF]
		{Character.isJavaIdentifierPart(_input.LA(-1))}?
	|	// covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
		[\uD800-\uDBFF] [\uDC00-\uDFFF]
		{Character.isJavaIdentifierPart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?
	;

WS  :  [ \t\u000C]+ -> channel(HIDDEN)
    ;

NL  :   '\r'? '\n' -> channel(HIDDEN) ;    // command separator (ignore for now)

DOC_COMMENT
	:	'/**' .*? ('*/' | EOF) -> channel(HIDDEN)
	;

BLOCK_COMMENT
	:	'/*' .*? ('*/' | EOF)  -> channel(HIDDEN)
	;

LINE_COMMENT
	:	'//' ~[\r\n]*  -> channel(HIDDEN)
	;