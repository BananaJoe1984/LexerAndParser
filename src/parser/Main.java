package parser;

import java.util.List;
import java.util.Scanner;


// Main driver program to test the Lexer and Parser

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

System.out.println("Lexer and Recursive Descent Parser ");
System.out.println("Enter your program (type 'END' on a new line to finish):\n");

StringBuilder inputBuilder = new StringBuilder();
String line;
        //collects multiple lines into one string, and line stores each line typed

        while (scanner.hasNextLine()) {
       line = scanner.nextLine();
      //reads line one by one
       if (line.trim().equals("END")) {
       break;
       }
       //if user types end, stop reading
        inputBuilder.append(line).append("\n");
        }
        //adds line to program text

      String input = inputBuilder.toString();

     if (input.trim().isEmpty()) {
     System.out.println("No input provided.");
      return;
        }
     //if no input, exit

    System.out.println("\n Lexical Analysis ");
     try {
       Lexer lexer = new Lexer(input);
       //sends user program into lexer
       List<Token> tokens = lexer.tokenize();
       //lexer splits programs into tokens

       System.out.println("Tokens:");
        for (Token token : tokens) {
            System.out.println("  " + token);
        }

       System.out.println("\n Syntax Analysis ");
         Parser parser = new Parser(tokens);
         //send tokens to parser
        parser.parse();
        //if correct, success, if wrong, error.

      } catch (LexerException e) {
            System.err.println("Lexer Error: " + e.getMessage());
    } catch (ParserException e) {
      System.err.println("Parser Error: " + e.getMessage());
    }

        scanner.close();
     //releases system resources
    }
}