package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


  //Lexer for tokenizing the input source code
//tracks where we are in the text
public class Lexer {
private final String input; //entire source code string
private int position; //current character index
private int line; //current line number
private int column; //column

    // Map keywords to their token types
    private static final Map<String, TokenType> KEYWORDS = new HashMap<>();
    static {
KEYWORDS.put("program", TokenType.PROGRAM);
KEYWORDS.put("end_program", TokenType.END_PROGRAM);
 KEYWORDS.put("if", TokenType.IF);
KEYWORDS.put("end_if", TokenType.END_IF);
KEYWORDS.put("end_loop", TokenType.END_LOOP);
    }
//^^this runs when the class loads and adds langugae keywords so the lexer knows "if" is not just
//an identifier but a keyword




// Regular expressions for token matching
private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]*");
//this matches identifiers like x, count, myVar1
private static final Pattern NUMBER_PATTERN = Pattern.compile("^[0-9]+");
//matches numbers like 5, 123, 1234123, 182734, etc
private static final Pattern WHITESPACE_PATTERN = Pattern.compile("^\\s+");
//matches spaces, tabs, and newlines

public Lexer(String input) {
        this.input = input;
        this.position = 0;
        this.line = 1;
        this.column = 1;
    }
//this starts at line 1, column 1


     // Tokenize the entire input and return a list of tokens

    public List<Token> tokenize() throws LexerException {
        List<Token> tokens = new ArrayList<>();
        Token token;

        while ((token = nextToken()).getType() != TokenType.EOF) {
            tokens.add(token);
        }
        tokens.add(token); // this reads tokens until the end of the file

        return tokens;
    }
//this returns a list of tokens and creates an empty list

    //Get the next token from the input one at a time

    public Token nextToken() throws LexerException {
        skipWhitespace();

        if (position >= input.length()) {
            return new Token(TokenType.EOF, "", line, column);
        }
        //if there are no more characters, return the end of file token
        char current = input.charAt(position);
        int tokenLine = line;
        int tokenColumn = column;
        //this remembers where the token started

        // Check for two-character operators first
        if (position + 1 < input.length()) {
            String twoChar = input.substring(position, position + 2);
            TokenType twoCharType = matchTwoCharOperator(twoChar);
            //examples: ==, !=, >=, <=
            if (twoCharType != null) {
                position += 2;
                column += 2;
                return new Token(twoCharType, twoChar, tokenLine, tokenColumn);
                //move forward and return a token
            }
        }

        // Single-character operators and delimiters
        TokenType singleCharType = matchSingleCharOperator(current);
        //examples : + - * / () ; , if any of these are found, move one character and return a token

        if (singleCharType != null) {
            position++;
            column++;
            return new Token(singleCharType, String.valueOf(current), tokenLine, tokenColumn);
        }

        // Numbers
        Matcher numberMatcher = NUMBER_PATTERN.matcher(input.substring(position));
        if (numberMatcher.find()) {
            String number = numberMatcher.group();
            position += number.length();
            column += number.length();
            return new Token(TokenType.NUMBER, number, tokenLine, tokenColumn);
        }

        // Identifiers and keywords
        Matcher identifierMatcher = IDENTIFIER_PATTERN.matcher(input.substring(position));
        if (identifierMatcher.find()) {
            String identifier = identifierMatcher.group();
            position += identifier.length();
            column += identifier.length();

            // Check if it's a keyword
            TokenType keywordType = KEYWORDS.get(identifier);
            //check if keyword, if yes return its token if not return its identifier token
            if (keywordType != null) {
                return new Token(keywordType, identifier, tokenLine, tokenColumn);
            }

            return new Token(TokenType.IDENTIFIER, identifier, tokenLine, tokenColumn);
        }

        // Invalid character
        throw new LexerException("Invalid character '" + current + "' at line " +
                line + ", column " + column);
    }


     // Skip whitespace and update line/column tracking

    private void skipWhitespace() {
        while (position < input.length()) {
            char c = input.charAt(position);
            if (c == ' ' || c == '\t' || c == '\r') {
                position++;
                column++;
            } else if (c == '\n') {
                position++;
                line++;
                column = 1;
            } else {
                break;
            }
        }
    }


      //Match two-character operators

    private TokenType matchTwoCharOperator(String twoChar) {
        switch (twoChar) {
case "==": return TokenType.EQUAL;
case "!=": return TokenType.NOT_EQUAL;
case ">=": return TokenType.GREATER_EQUAL;
case "<=": return TokenType.LESS_EQUAL;
default: return null;
        }
    }


     //Match single-character operators and delimiters

    private TokenType matchSingleCharOperator(char c) {
        switch (c) {
case '=': return TokenType.ASSIGN;
case '+': return TokenType.PLUS;
case '-': return TokenType.MINUS;
case '*': return TokenType.MULTIPLY;
case '/': return TokenType.DIVIDE;
case '%': return TokenType.MODULO;
case '>': return TokenType.GREATER;
case '<': return TokenType.LESS;
case '(': return TokenType.LPAREN;
case ')': return TokenType.RPAREN;
case ';': return TokenType.SEMICOLON;
case ':': return TokenType.COLON;
default: return null;
        }
    }
}
//reads the source code string, skips spaces, looks for: keywords, identifiers, numbers or operators,
//creates token objects, then tracks the lines and columns for errors