package parser;

 //Enumeration of all token types in the language

public enum TokenType {
    // Keywords
    PROGRAM,
    END_PROGRAM,
    IF,
    END_IF,
    LOOP,
    END_LOOP,

    // Operators
    ASSIGN,         // =
    PLUS,           // +
    MINUS,          // -
    MULTIPLY,       // *
    DIVIDE,         // /
    MODULO,         // %

    // Relational operators
    EQUAL,          // ==
    NOT_EQUAL,      // !=
    GREATER,        // >
    LESS,           // <
    GREATER_EQUAL,  // >=
    LESS_EQUAL,     // <=

    // Delimiters
    LPAREN,         // (
    RPAREN,         // )
    SEMICOLON,      // ;
    COLON,          // :

    // Literals and identifiers
    IDENTIFIER,
    NUMBER,

    // Special
    EOF,
    INVALID
}