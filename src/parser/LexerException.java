package parser;


// Exception thrown by the Lexer when invalid input is encountered
public class LexerException extends Exception {
    public LexerException(String message) {
        super(message);
    }
}