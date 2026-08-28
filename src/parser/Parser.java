package parser;

import java.util.List;


//Recursive Descent Parser for the language
//reads tokens, checks grammar rules, and throws errors if syntax is wrong
public class Parser {
    private final List<Token> tokens; //list from lexer
    private int position; //current index in token list
    private Token currentToken; //token that is being checked

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.position = 0;
        this.currentToken = tokens.get(0);
    }
//when parser starts, saves tokens and starts at first token

// Parse the entire program

    public void parse() throws ParserException {
        parseProgram();
        //starts parsing at <program> rule
        if (currentToken.getType() != TokenType.EOF) {
            throw new ParserException("Expected end of file but found: " + currentToken);
        }
        //if a token remains after program, syntax error
        System.out.println("Parsing completed successfully!");
    }
//no errors, then success

    //<program> → program <statements> end_program

    private void parseProgram() throws ParserException {
        expect(TokenType.PROGRAM);
        parseStatements();
        expect(TokenType.END_PROGRAM);
    }
//example:
    //program
    //x=5;
    //end_program

     // <statements> → <statement> | <statement> <statements>

    private void parseStatements() throws ParserException {
        while (isStatement()) {
            parseStatement();
        }
    }
//keep parsing statements until there are none left

     // Check if current token can start a statement

    private boolean isStatement() {
        TokenType type = currentToken.getType();
        return type == TokenType.IDENTIFIER ||
                type == TokenType.IF ||
                type == TokenType.LOOP;
    }
//statement can start with a variable name, if, or loop


    // <statement> → <assignment> | <condition> | <loop>

    private void parseStatement() throws ParserException {
        switch (currentToken.getType()) {
            case IDENTIFIER:
                parseAssignment();
                break;
            case IF:
                parseCondition();
                break;
            case LOOP:
                parseLoop();
                break;
            default:
                throw new ParserException("Expected statement but found: " + currentToken);
        }
    }//decides which statement type:
    //identifier: assignment
    //if: condition
    //loop is a loop



     // <assignment> → <identifier> = <expression> ;

    private void parseAssignment() throws ParserException {
        expect(TokenType.IDENTIFIER);
        expect(TokenType.ASSIGN);
        parseExpression();
        expect(TokenType.SEMICOLON);
    }
//x = 10 + 5

     //<expression> → <term> ((+ | -) <term>)*

    private void parseExpression() throws ParserException {
        parseTerm();
        while (currentToken.getType() == TokenType.PLUS ||
                currentToken.getType() == TokenType.MINUS) {
            advance();
            parseTerm();
        }
    }
//optional + or - terms, code loops when + or - is found (5 + 3 - 2)

     // <term> → <factor> ((* | / | %) <factor>)*

    private void parseTerm() throws ParserException {
        parseFactor();
        while (currentToken.getType() == TokenType.MULTIPLY ||
                currentToken.getType() == TokenType.DIVIDE ||
                currentToken.getType() == TokenType.MODULO) {
            advance();
            parseFactor();
        }
    }


     // <factor> → <identifier> | <number> | ( <expression> )

    private void parseFactor() throws ParserException {
        switch (currentToken.getType()) {
            case IDENTIFIER:
            case NUMBER:
                advance();
                break;
            case LPAREN:
                advance();
                parseExpression();
                expect(TokenType.RPAREN);
                break;
            default:
                throw new ParserException("Expected factor but found: " + currentToken);
        }
    }

//if identifier or number it is ok, experesion () is nested math, else is error

     // <condition> → if ( <logic_expression> ) <statements> end_if
    // if (x > 10), y = 5, end_if\
    //parser checks: if, (, logic expression, ), statements, end_if
    private void parseCondition() throws ParserException {
        expect(TokenType.IF);
        expect(TokenType.LPAREN);
        parseLogicExpression();
        expect(TokenType.RPAREN);
        parseStatements();
        expect(TokenType.END_IF);
    }


    // <logic_expression> → <var> <relational_op> <var>

    private void parseLogicExpression() throws ParserException {
        parseVar();
        parseRelationalOp();
        parseVar();
    }


    // <var> → <identifier> | <number>

    private void parseVar() throws ParserException {
        if (currentToken.getType() == TokenType.IDENTIFIER ||
                currentToken.getType() == TokenType.NUMBER) {
            advance();
        } else {
            throw new ParserException("Expected identifier or number but found: " + currentToken);
        }
    }


     // <relational_op> → == | != | > | < | >= | <=

    private void parseRelationalOp() throws ParserException {
        switch (currentToken.getType()) {
            case EQUAL:
            case NOT_EQUAL:
            case GREATER:
            case LESS:
            case GREATER_EQUAL:
            case LESS_EQUAL:
                advance();
                break;
            default:
                throw new ParserException("Expected relational operator but found: " + currentToken);
        }
    }


     //<loop> → loop ( <var> = <var> : <var> ) <statements> end_loop

    private void parseLoop() throws ParserException {
        expect(TokenType.LOOP);
        expect(TokenType.LPAREN);
        parseVar();
        expect(TokenType.ASSIGN);
        parseVar();
        expect(TokenType.COLON);
        parseVar();
        expect(TokenType.RPAREN);
        parseStatements();
        expect(TokenType.END_LOOP);
    }


     // Expect a specific token type and consume it
    //if token is not an expected type, throw a syntax error, else, move to the next token
    private void expect(TokenType expectedType) throws ParserException {
        if (currentToken.getType() != expectedType) {
            throw new ParserException(
                    String.format("Expected %s but found %s at line %d, column %d",
                            expectedType, currentToken.getType(),
                            currentToken.getLine(), currentToken.getColumn())
            );
        }
        advance();
    }


     // Move to the next token

    private void advance() {
        if (position < tokens.size() - 1) {
            position++;
            currentToken = tokens.get(position);
        }
    }
}
//moves parser forward one token