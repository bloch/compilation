/***************************/
/* FILE NAME: LEX_FILE.lex */
/***************************/

/*************/
/* USER CODE */
/*************/
import java_cup.runtime.*;

/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************/
/* OPTIONS AND DECLARATIONS SECTION */
/************************************/
   
/*****************************************************/ 
/* Lexer is the name of the class JFlex will create. */
/* The code will be written to the file Lexer.java.  */
/*****************************************************/ 
%class Lexer

/********************************************************************/
/* The current line number can be accessed with the variable yyline */
/* and the current column number with the variable yycolumn.        */
/********************************************************************/
%line
%column

/*******************************************************************************/
/* Note that this has to be the EXACT same name of the class the CUP generates */
/*******************************************************************************/
%cupsym TokenNames

/******************************************************************/
/* CUP compatibility mode interfaces with a CUP generated parser. */
/******************************************************************/
%cup

/****************/
/* DECLARATIONS */
/****************/
/*****************************************************************************/   
/* Code between %{ and %}, both of which must be at the beginning of a line, */
/* will be copied verbatim (letter to letter) into the Lexer class code.     */
/* Here you declare member variables and functions that are used inside the  */
/* scanner actions.                                                          */  
/*****************************************************************************/   
%{
	/*********************************************************************************/
	/* Create a new java_cup.runtime.Symbol with information about the current token */
	/*********************************************************************************/
	private Symbol symbol(int type)               {return new Symbol(type, yyline, yycolumn);}
	private Symbol symbol(int type, Object value) {return new Symbol(type, yyline, yycolumn, value);}

	/*******************************************/
	/* Enable line number extraction from main */
	/*******************************************/
	public int getLine() { return yyline + 1; } 

	/**********************************************/
	/* Enable token position extraction from main */
	/**********************************************/
	public int getTokenStartPosition() { return yycolumn + 1; } 
%}

/***********************/
/* MACRO DECALARATIONS */
/***********************/

%state IN_COMMENT

LETTER           = [a-z|A-Z]
DIGIT            = [0-9]
LineTerminator	 = \r|\n|\r\n
WhiteSpace		 = {LineTerminator} | [ \t\f]
INT			     = 0 | [1-9][0-9]*
LPAREN           = "("
RPAREN           = ")"
LBRACK           = "["
RBRACK           = "]"
LBRACE           = "{"
RBRACE           = "}"
NIL              = nil
PLUS             = "+"
MINUS            = "-"
TIMES            = "*"
DIVIDE           = "/"
COMMA            = ","
DOT              = "."
LEFT_COMMENT     = \/\*
RIGHT_COMMENT    = \*\/
SEMICOLON        = ";"
QUESTION_MARK    = "?"
EXCLAMATION_MARK = "!"
TYPE_INT         = int
ASSIGN           = :=
EQ               = =
LT               = <
GT               = >
ARRAY            = array
CLASS            = class
EXTENDS          = extends
RETURN           = return
WHILE            = while
IF               = if
NEW              = new
QUOTES           = "\""
STRING           = {QUOTES}{LETTER}*{QUOTES}
ID               = [a-z|A-Z][a-z|A-Z|0-9]*
TYPE_STRING      = string
ANY              = \n|.
INPUT_CHAR = [^\r\n]
ONE_LINE_COMMENT = "//" {INPUT_CHAR}* {LineTerminator}?


/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************************************/
/* LEXER matches regular expressions to actions (Java code) */
/************************************************************/

/**************************************************************/
/* YYINITIAL is the state at which the lexer begins scanning. */
/* So these regular expressions will only be matched if the   */
/* scanner is in the start state YYINITIAL.                   */
/**************************************************************/

<YYINITIAL> {
{LPAREN}            { return symbol(TokenNames.LPAREN);}
{RPAREN}            { return symbol(TokenNames.RPAREN);}
{LBRACK}            { return symbol(TokenNames.LBRACK);}
{RBRACK}            { return symbol(TokenNames.RBRACK);}
{LBRACE}            { return symbol(TokenNames.LBRACE);}
{RBRACE}            { return symbol(TokenNames.RBRACE);}
{NIL}               { return symbol(TokenNames.NIL);}
{PLUS}              { return symbol(TokenNames.PLUS);}
{MINUS}             { return symbol(TokenNames.MINUS);}
{TIMES}             { return symbol(TokenNames.TIMES);}
{DIVIDE}            { return symbol(TokenNames.DIVIDE);}
{COMMA}             { return symbol(TokenNames.COMMA);}
{DOT}               { return symbol(TokenNames.DOT);}
{SEMICOLON}         { return symbol(TokenNames.SEMICOLON);}
{TYPE_INT}          { return symbol(TokenNames.TYPE_INT);}
{ASSIGN}            { return symbol(TokenNames.ASSIGN);}
{EQ}                { return symbol(TokenNames.EQ);}
{LT}                { return symbol(TokenNames.LT);}
{GT}                { return symbol(TokenNames.GT);}
{ARRAY}             { return symbol(TokenNames.ARRAY);}
{CLASS}             { return symbol(TokenNames.CLASS);}
{EXTENDS}           { return symbol(TokenNames.EXTENDS);}
{RETURN}            { return symbol(TokenNames.RETURN);}
{WHILE}             { return symbol(TokenNames.WHILE);}
{IF}                { return symbol(TokenNames.IF);}
{NEW}               { return symbol(TokenNames.NEW);}
{INT}			    { Integer Num = new Integer(yytext());
                      int num = Num.intValue();
                      if ( num < Math.pow(2,15)){
                      return symbol(TokenNames.INT, Num);
                    } else {
                      return symbol(TokenNames.ERROR);
                    }
                    }
{STRING}			{ return symbol(TokenNames.STRING, new String(yytext()));}
{TYPE_STRING}       { return symbol(TokenNames.TYPE_STRING);}
{ID}				{ return symbol(TokenNames.ID,     new String( yytext()));}
{WhiteSpace}		{ /* just skip what was found, do nothing */ }
{LEFT_COMMENT}      { yybegin(IN_COMMENT);}
{ONE_LINE_COMMENT}  { }
<<EOF>>				{ return symbol(TokenNames.EOF);}
{ANY}               { return symbol(TokenNames.ERROR);}
}
<IN_COMMENT> {
{RIGHT_COMMENT}     { yybegin(YYINITIAL);}
{LETTER}            { }
{DIGIT}             { }
{WhiteSpace}        { }
{LineTerminator}    { }
{LPAREN}            { }
{RPAREN}            { }
{LBRACK}            { }
{RBRACK}            { }
{LBRACE}            { }
{RBRACE}            { }
{QUESTION_MARK}     { }
{EXCLAMATION_MARK}  { }
{PLUS}              { }
{MINUS}             { }
{TIMES}             { }
{DIVIDE}            { }
{DOT}               { }
{SEMICOLON}         { }
{ANY}               { return symbol(TokenNames.ERROR);}
}
