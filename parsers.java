public class parsers {
Lexer lex;
int nextToken;
boolean success;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		parsers parser = new parsers();	
	}
	
 // create a parser object

	
	 public parsers() {
		 lex = new Lexer();
	        success = true;
	        if (lex.openFile()) {
	            lex.getChar();
	            nextToken = lex.lex();
	            System.out.println("First token is: " + nextToken);
	            nextToken =lex.lex();
	            program(); 

	        } else {
	            success = false;
	        }
	        if (success) {
	            System.out.println("Compiled correctly!");
	        }
    
	 }
	 void expression() {
	        System.out.println("NT: expression");

	        term();

	        while (nextToken == Lexer.PLUS || nextToken == Lexer.MINUS) {
	            System.out.println("have + or minus and expect now a term");
	            nextToken = lex.lex();

	            term();
	        }
	     
	    }

	    // ************************************************************************
	    // <term> -->  <factor> { (*|/) <factor> }
	    // ************************************************************************
	    void term() {
	        System.out.println("NT: term");

	        factor();

	        while (nextToken == Lexer.MULT || nextToken == Lexer.DIV) {
	            System.out.println("have * or / and expect now a factor");
	            nextToken = lex.lex();

	            factor();
	        }
	    }

	
	       
	        
	 void factor() {
	        System.out.println("NT: factor");

	        switch (nextToken) {
	            case Lexer.IDENT:
	                System.out.println("have identifer");
	                nextToken = lex.lex();
	                break;
	                
	            case Lexer.IntType:
	                System.out.println("have integer literal");
	                nextToken = lex.lex();
	                break;
	                
	            case Lexer.LETTER:
	                System.out.println("have real literal");
	                nextToken = lex.lex();
	                break;
	            case Lexer.LOOP:
	            {
	            	System.out.println("we have a loop");
	                //nextToken = lex.lex();
	                statements();
	                break;
	            }
	            case Lexer.LEFT_PAREN:
	                System.out.println("have left paren");
	                nextToken = lex.lex();
	                expression();
	                if (nextToken == lex.RIGHT_PAREN) {
	                    System.out.println("have right paren");
	                    nextToken = lex.lex();
	                } else {
	                    success = false;
	                    System.out.println("Error in factor : expecting right paren");
	                }   
	                break;
	          /*  case Lexer.False:
	                System.out.println("have yup");
	                nextToken = lex.lex();
	                break;
	            case Lexer.True:
	                System.out.println("have nope");
	                nextToken = lex.lex();
	                break;*/
	                
	            default:
	                success = false;
	                System.out.println("Error in factor : invalid option");
	                break;
	        }
	    }

	    void program() {
	        System.out.println("NT: program");
	        
	        block();
	        
	       
	        if (nextToken == lex.EOF) {
	           // success = false;
	            System.out.println("Expected end of program");
	        }
	    }
	   void block() {
	        System.out.println("NT: block");
	        if (nextToken == Lexer.Openingbrease) {
	            System.out.println("got a begin...");
	            nextToken = lex.lex();
if(nextToken != Lexer.Clossingbrease)
{
	if(nextToken != Lexer.Say)
	{
	nextToken = lex.lex();
	}
	statements();
}
	            
	         
	            if (nextToken == Lexer.Clossingbrease) {
	                System.out.println("got an end...");
	               
	               
	                	 nextToken = lex.lex();	
	                
	                
	            } else {
	                success = false;
	                System.out.println("Error in block : expecting an end");
	            }

	        } else {
	            success = false;
	            System.out.println("Error in block : expecting a begin");
	        }
	    }

	// ************************************************************************
	// <statements> -->  <statement> { ; <statement> }
	// ************************************************************************
	    void statements() {
	        System.out.println("NT: statements");

	        statement();
	       // nextToken = lex.lex();
	        while (nextToken == lex.SEMICOLON) {
	            System.out.println("got a semicolon");
	            nextToken = lex.lex();

	            statement();
	        }
	    }
	    void statement() {
	        System.out.println("NT: statement");
	        
	      if(nextToken == Lexer.UNTIL)
	      {
	    	  nextToken = lex.lex();
	            booleanExpression();
	      }
	        if (nextToken == Lexer.LOOP) {
	            nextToken = lex.lex();
	            repetitionStmt();

	        } else if (nextToken == Lexer.IF) {
	            nextToken = lex.lex();
	            conditionalStmt();
	        }
	            else if (nextToken == Lexer.Setto ) {
		           // nextToken = lex.lex();
		            assignStmt();
	        } else if (nextToken == Lexer.IDENT ) {
	            nextToken = lex.lex();
	            assignStmt();

	        } else if (nextToken == Lexer.Say) {
	        	 System.out.println("got say");
	            nextToken = lex.lex();
	            outputStmt();

	        } 
	         else if(nextToken != Lexer.Clossingbrease)  {
	            success = false;
	            System.out.println("Error in statement : Not a valid statement");
	        }
	    }
	    void conditionalStmt() {
	        System.out.println("NT: conditional statement");
	        
	      // nextToken = lex.lex();
	        if (nextToken == Lexer.UNTIL) {
	            System.out.println("got a then...");
	            nextToken = lex.lex();
	            booleanExpression();
	            nextToken = lex.lex();
	            block();
	           


	        } else {
	            success = false;
	            System.out.println("Error in conditional statement : expecting then");
	        }
	    }
	    void repetitionStmt() {
	        System.out.println("NT: repetition statement");

	      
	     //   nextToken = lex.lex();
	        if (nextToken == Lexer.UNTIL) {
	            System.out.println("got an until...");
	            nextToken = lex.lex();
	            booleanExpression();
	            nextToken = lex.lex();
	            block();

	        } else {
	            success = false;
	            System.out.println("Error in repetitionStmt: expecting until");
	        }
	    }
	    void booleanExpression() {
	        System.out.println("NT: boolean expression");
	    factor();
	        if(nextToken == Lexer.IDENT ||nextToken == Lexer.IntType )
	        {
	        	nextToken = lex.lex();
	        }
	        
	       // expression();
	        if (nextToken == Lexer.GT || nextToken == Lexer.LT || nextToken == Lexer.equ) {
	            System.out.println("have > or < or =");
	            nextToken = lex.lex();

	            expression();
	        }
	    }
	    void assignStmt() {
	        System.out.println("NT: assignStmt");
	    
	        if (nextToken == Lexer.Setto) {
	            System.out.println("have identifier");
	            nextToken = lex.lex();

	            if (nextToken == Lexer.IntType  || nextToken ==  Lexer.IDENT ||nextToken == Lexer.Chartype || nextToken == Lexer.LETTER || nextToken == Lexer.Stringtype ) {
	                System.out.println("have to");
	                type();
	                nextToken = lex.lex();
if(nextToken != Lexer.SEMICOLON)
{
	                expression();
}
	            } else {
	                success = false;
	                System.out.println("Error in assignment statement: expecting to");
	            }
	        } else {
	            success = false;
	            System.out.println("Error in assignment : expecting an identifer");
	        }
	    }
	    void outputStmt() {
	        System.out.println("NT: output statement");
	       // nextToken = lex.lex();
	        expression();
	        
	        
	    }
	   

	    void identifierList() {
	        System.out.println("NT: identifierList");

	        if (nextToken == Lexer.IDENT) {
	            System.out.println("have identifer..");
	            nextToken = lex.lex();

	     
	        } else {
	            success = false;
	            System.out.println("Error in identifer list : expecting identifer");
	        }
	    }
	    void type() {
	        System.out.println("NT: type");

	        switch (nextToken) {
            case Lexer.IDENT:
                System.out.println("have var");
                nextToken = lex.lex();
                break;
	            case Lexer.IntType:
	                System.out.println("have int");
	                nextToken = lex.lex();
	                break;
	            case Lexer.Stringtype:
	                System.out.println("have string");
	                nextToken = lex.lex();
	                break;
	            case Lexer.Chartype:
	                System.out.println("have string");
	                nextToken = lex.lex();
	                break;
	            default:
	                success = false;
	                System.out.println("Error in type : expected int or real");
	                break;
	        }
	    }

	}