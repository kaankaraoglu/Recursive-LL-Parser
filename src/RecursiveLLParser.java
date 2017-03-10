import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class RecursiveLLParser {

	/* Global declarations */
	/* Variables */
	public static String input;
	public static int index = 0;
	public static int charClass;
	public static String lexeme = "";
	public static ArrayList < Character > symbols = new ArrayList < Character > (
	Arrays.asList('[', ']', '{', '}', '(', ')', '<', '>', '\'', '"', '=', '|', '.', ',', ';'));
	public static char nextChar;
	public static int nextToken;
	public static Scanner scan;

	/* Character classes */
	public final static int LETTER = 0;
	public final static int DIGIT = 1;
	public final static int SYMBOL = 2;
	public final static int UNKNOWN = 19;

	/* Token codes */
	public final static int EOF = -1;
	public final static int LEFT_BRACK = 3;
	public final static int RIGHT_BRACK = 4;
	public final static int LEFT_CURLY = 5;
	public final static int RIGHT_CURLY = 6;
	public final static int LEFT_PAREN = 7;
	public final static int RIGHT_PAREN = 8;
	public final static int SMALLER = 9;
	public final static int GREATER = 10;
	public final static int APOST = 11;
	public final static int QUOTA = 12;
	public final static int EQUAL = 13;
	public final static int OR_SYMBOL = 14;
	public final static int DOT = 15;
	public final static int COMMA = 16;
	public final static int SEMICOLON = 17;
	public final static int UNDERLINE = 18;
	public final static int IDENT = 20;
	public final static int CHARACTER = 21;
	public final static int RIGHT_ARROW = 23;

    public void getEBNFStatement(){
        scan = new Scanner( System.in );
        System.out.print("Enter an EBNF Statement : ");
        input = scan.nextLine();
    }

	public static int lookup(char ch) {
		switch (ch) {
		case '[':
			addChar();
			nextToken = LEFT_BRACK;
			break;

		case '%':
			addChar();
			nextToken = RIGHT_ARROW;
			break;

		case ']':
			addChar();
			nextToken = RIGHT_BRACK;
			break;

		case '{':
			addChar();
			nextToken = LEFT_CURLY;
			break;

		case '}':
			addChar();
			nextToken = RIGHT_CURLY;
			break;

		case '(':
			addChar();
			nextToken = LEFT_PAREN;
			break;

		case ')':
			addChar();
			nextToken = RIGHT_PAREN;
			break;

		case '<':
			addChar();
			nextToken = SMALLER;
			break;

		case '>':
			addChar();
			nextToken = GREATER;
			break;

		case '\'':
			addChar();
			nextToken = APOST;
			break;

		case '"':
			addChar();
			nextToken = QUOTA;
			break;

		case '=':
			addChar();
			nextToken = EQUAL;
			break;

		case '|':
			addChar();
			nextToken = OR_SYMBOL;
			break;

		case '.':
			addChar();
			nextToken = DOT;
			break;

		case ',':
			addChar();
			nextToken = COMMA;
			break;

		case ';':
			addChar();
			nextToken = SEMICOLON;
			break;

		case '_':
			addChar();
			nextToken = UNDERLINE;
			break;

		default:
			addChar();
			nextToken = EOF;
			break;
		}
		return nextToken;
	}
    // Check lexeme length.
	public static void addChar() {
		if (lexeme.length() <= 98) {
			lexeme += nextChar;
		} else System.out.println("Error - lexeme is too long \n");
	}
    // Determines the type of the input.
	public static void getChar() {
		if (input.length() > index) {
			nextChar = input.charAt(index++);
			if (Character.isAlphabetic(nextChar)) charClass = LETTER;
			else if (Character.isDigit(nextChar)) charClass = DIGIT;
			else if (symbols.contains(nextChar)) charClass = SYMBOL;
			else if (nextChar == '_') charClass = UNDERLINE;
			else charClass = UNKNOWN;
		} else charClass = EOF;
	}
    // If next character is Whitespace get next char.
	public static void getNonBlank() {
		while (Character.isWhitespace(nextChar))
		getChar();
	}
    // Process a lexeme.
	public static int lex() {
		lexeme = "";
		getNonBlank();
		switch (charClass) {
			/* Parse identifiers */
		case LETTER:
			addChar();
			getChar();
			if (Character.isWhitespace(nextChar) || charClass == EOF) {
				nextToken = CHARACTER;
				break;
			}
			while (charClass == LETTER || charClass == DIGIT || charClass == UNDERLINE) {
				addChar();
				getChar();
			}
			nextToken = IDENT;
			break;

		case DIGIT:
			addChar();
			getChar();
			if (Character.isWhitespace(nextChar)) nextToken = CHARACTER;
			else System.out.println("SYNTAX ERROR: Input is neither Character nor Identifier");
			break;

			/* Symbols */
		case SYMBOL:
			lookup(nextChar);
			getChar();
			break;

		case UNKNOWN:
			lookup(nextChar);
			getChar();
			break;

		case EOF:
			nextToken = EOF;
			lexeme = "EOF";

		}
		/* End of switch */
		System.out.println("\tNext token is: " + nextToken + " Next lexeme is " + lexeme.toString());
		return nextToken;

	}

    // Process grammar for the lexeme.
	public static void grammar() {
		System.out.println("\nEntering grammar...");

		do {
			lex();
			rule();
		} while ( nextToken == SEMICOLON );

		System.out.println("\nExiting grammar...");
		scan.close();
	}

	// Apply rules.
	public static void rule() {
		System.out.println("\nEntering rule...");
		lhs();
		if (nextToken == RIGHT_ARROW) {
			lex();
			if (nextToken != EOF) rhs();
			else System.out.println("RULE ERROR: RHS Statement is missing.");
		} else System.out.println("Rule : Missing right arrow.");
		System.out.println("Exiting rule...");
	}

	public static void rhs() {
		System.out.println("Entering RHS...");
		if (nextToken == IDENT) lex();
		else if (nextToken == LEFT_BRACK) {
			lex();
			rhs();
			if (nextToken == RIGHT_BRACK) lex();
			else System.out.println("RHS ERROR: Missing Right bracket.");
		} else if (nextToken == APOST) terminal();
		else if (nextToken == QUOTA) terminal();
		else if (nextToken == LEFT_CURLY) {
			lex();
			rhs();
			if (nextToken == RIGHT_CURLY) lex();
			else System.out.println("RHS ERROR: Missing Right Curly Braces.");
		} else if (nextToken == LEFT_PAREN) {
			lex();
			rhs();
			if (nextToken == RIGHT_PAREN) lex();
			else System.out.println("RHS ERROR: Missing Right Parenthesis.");
		}
		if (nextToken == OR_SYMBOL) {
			lex();
			rhs();
		}
		System.out.println("Exiting RHS...");
	}

	
	public static void terminal() {
		System.out.println("Entering Terminal...");
		if (nextToken == APOST) {
			lex();
			if (nextToken == CHARACTER) {
				lex();
				while (nextToken == CHARACTER)
				lex();
				if (nextToken == APOST) lex();
				else System.out.println("TERMINAL Error: Missing Apostrophe.");
			} else System.out.println("TERMINAL Error: Expecting Character...");
		} else if (nextToken == QUOTA) {
			lex();
			if (nextToken == CHARACTER) {
				lex();
				while (nextToken == CHARACTER)
				lex();
				if (nextToken == QUOTA) lex();
				else System.out.println("TERMINAL Error: Missing Quoatation symbol.");
			} else System.out.println("TERMINAL Error: Expecting character...");
		}
		System.out.println("Exiting Terminal...");
	}

	public static void lhs() {
		System.out.println("Entering LHS...");
		if (nextToken == IDENT) lex();
		else System.out.println("LHS Error: Expecting Identifier");
		System.out.println("Exiting LHS...");
	}
}
