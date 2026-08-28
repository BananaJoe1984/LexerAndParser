package parser;


 //Exception thrown by the Parser when syntax errors are encountered

public class ParserException extends Exception {
    public ParserException(String message) {
        super(message);
    }
}