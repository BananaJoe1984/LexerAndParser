package parser;
        // Represents a token with its type, value, and position
public class Token {
    private final TokenType type; //what kind of token it is
    private final String value; //the actual text that will be displayed
    private final int line; //the line number in the source file
    private final int column; //column position

    public Token(TokenType type, String value, int line, int column) { //runs when a token is created
        this.type = type; //saves passed value into the object
        this.value = value;
        this.line = line;
        this.column = column;
    }

    public TokenType getType() {
        return type;
    }
//this returns the tokens type and is used instead of making the field public
    public String getValue() {
        return value;
    }
//returns the token text
    public int getLine() {
        return line;
    }
//returns the line number
    public int getColumn() {
        return column;
    }
//returns the column number
    @Override //this overrides javas default toString() method and controls how the
    // object prints
    public String toString() {
        return String.format("Token[%s, '%s', Line:%d, Col:%d]",
                type, value, line, column);
    }
}
//represents ont token from source text, stores type, value, line, and column, is immutable, has getters
// this would be used if you were to say a form validator, this would be the tokens used.