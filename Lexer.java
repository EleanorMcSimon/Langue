/*
 * To change this license otherwiseer, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.HashMap;
import java.io.*;
import java.util.Scanner;

/**
 *
 * @author Eleanor Simon
 */
public class Lexer {
    static final int IDENT = 0;
    static final int LETTER = 1;
    static final int DIGIT = 2;
    static final int Doit = 3;
    static final int Setto = 4;
    static final int Stringtype = 5;
    static final int Chartype = 6;
    static final int IntType = 7;
    static final int DoubleType = 8;
    static final int SEMICOLON = 9;
    static final int PLUS = 10;
    static final int MINUS = 11;
    static final int MULT = 12;
    static final int DIV = 13;
    static final int LT = 15;
    static final int GT = 16; 
    static final int LEFT_PAREN = 17;
    static final int RIGHT_PAREN = 18;
    static final int LOOP = 19;
    static final int IF = 20;
    static final int UNTIL = 21;
    static final int Say = 22;
    static final int fuc = 23;
    static final int back = 24;
    static final int PER = 24;
    static final int equ = 25;
    static final int Openingbrease = 25;
    static final int Clossingbrease = 26;
    static final int True = 26;
    static final int False = 26;
    static final int otheroption = 27;
    static final int UNKNOWN = 99;
    static final int EOF = -99;
    int charClass;
    String lexeme;
    char nextChar;
    int lexLen;
    int token;
    int nextToken;
    FileReader file;
    HashMap<String, Integer> specialWord = new HashMap<>();
    public boolean openFile() {
        System.out.println("Enter input file name:");
        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();
        try {
            file = new FileReader(fileName);
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("File not found. ");
            return false;
        }

    }

// **
    public void fillSpecialWords() {
        System.out.println("About to fill special words map");
        specialWord.put("while", LOOP);
        specialWord.put("for", LOOP);
        specialWord.put("fuc", fuc);
        specialWord.put("con", UNTIL);
        specialWord.put("Say", Say);
        specialWord.put("if", IF);
        specialWord.put("true", True);
        specialWord.put("false", False);
    }
    public Lexer() {
        fillSpecialWords();
    }
    public int lookup(char ch) {
        //System.out.println("in lookup with: " + ch);
        switch (ch) { //calls file.unget() when looking aotherwise for more than one character but there isn't one, so everything stays consistent
        case '}':
       	 addChar();
            nextToken = Clossingbrease;
            break;
        case '{':
        	 addChar();
             nextToken = Openingbrease;
             break;
            case '+':
                addChar();
                nextToken = PLUS;
                break;
            case '-':
                addChar();
                nextToken = MINUS;
                break;
            case '/':
                addChar();
                nextToken = DIV;
                break;
            case '*':
                addChar();
                nextToken = MULT;
                break;
            case '<':
                addChar();
                nextToken = LT;
                break;
            case '>':
                addChar();
                nextToken = GT;
                break;
            case '=':
                addChar();
                nextToken = equ;
                break;
            case ';':
                addChar();
                nextToken = SEMICOLON;
                break;
            case '(':
                addChar();
                nextToken = LEFT_PAREN;
                addChar();
                nextToken = UNTIL;
                break;
            case ')':
                addChar();
                nextToken = RIGHT_PAREN;
                break;
            case ':':
            	 addChar();
                 nextToken = Setto;
                 break;
            default:
                addChar();
                nextToken = EOF;
                break;
        }
        return nextToken;
    }

    public void getChar() {
        //System.out.println("in getChar");
        try {
            int c = file.read();
            nextChar = (char) c;
            //System.out.println("Next character:" + nextChar);

            if (Character.isLetter(nextChar)) {
                charClass = LETTER;
            } else if (Character.isDigit(nextChar)) {
                charClass = IntType;
            } else if (nextChar == '"') {
                charClass = Stringtype;
            } else if (nextChar == '.') {
                charClass = DoubleType;
            }else if (nextChar == '-')
            	{
                charClass = Chartype;		
          }else {
                charClass = UNKNOWN; }
            //System.out.println("character class:" + charClass);
        } catch (IOException e) {
            System.out.println("End of file reached.");
            charClass = EOF;
        }
    }
    public void getNonBlank() {
        //System.out.println("In getNonBlank");
        while (Character.isWhitespace(nextChar) && (charClass != EOF)) {
            //System.out.println("got white space"+nextChar);
            getChar();
        }
    } 
    int lex() {
        System.out.println("LEX\n");
        lexeme = "";

        lexLen = 0;
        getNonBlank(); // Eat up white space

        switch (charClass) {

            // Parse identifiers
            case LETTER:
                addChar();
                getChar();
                while (charClass == LETTER) {
                    addChar();
                    getChar();
                }
                
                System.out.println("lexeme before looking up in hashmap:" + lexeme);
                if (specialWord.containsKey(lexeme))
                    nextToken = specialWord.get(lexeme);
                else
                    nextToken = IDENT;
                
                System.out.println("nextToken read from hashmap: " + nextToken);
                
                break;

            // Integers 
            case IntType:
                addChar();
                getChar();
                while (charClass == IntType) {
                    addChar();
                    getChar();
                }
                // Integer
                if (charClass != DoubleType)
                    nextToken = IntType;
                else {
                    addChar();
                    getChar();
                    while (charClass == DoubleType) {
                        addChar();
                        getChar();
                    }
                 //   nextToken = DoubleType;
                }
                break;
            case Stringtype:
            {
            	while(charClass == Stringtype)
            	{
            		addChar();
                    getChar();
            	}
            break;
            }
            case Chartype:
            {
            	if(charClass == Chartype)
            	{
            		addChar();
                    getChar();
            	}
            break;
            }
            // Single characters
            case UNKNOWN:
                lookup(nextChar);
                getChar();
                break;

            case EOF:
                nextToken = EOF;
                lexeme = "EOF";
                break;
        } //end of switch

        System.out.println("lexeme: " + lexeme);
        return nextToken;
    }

 // *******************************************************************************************
 // ADDCHAR
//     adds nextChar to the lexeme
 // *******************************************************************************************
     public void addChar() {
         //System.out.println("in addChar");
         if (lexLen <= 98) {
             //cout << "Character " << nextChar << " placed at index " << lexLen << "\n";
             lexeme+= nextChar;
             //System.out.println("Lexeme so far:" + lexeme);
         } else {
             System.out.println("Error - lexeme is too long");
         }
     }
     
    
}