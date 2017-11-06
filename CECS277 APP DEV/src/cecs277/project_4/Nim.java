/*
 * Scott Roberts
 * SID: 011651729
 * CECS 277
 * Email: s.e.roberts0@gmail.com
 * 
 * Nim is a two player game (in this instance you are playing a computer).
 * The setup creates a game board with a random number generator assigning
 * 3-8 rows with 3-8 stones per row. Player 1 (the user) and Player 2 (the
 * computer) will alternate taking stones from each row until the stones are
 * all gone. The computer picks random rows and a random number of stones
 * each turn. There is some small exception handling to allow to computer to
 * properly pick a row and stone count without breaking the game.
 */

package cecs277.project_4;

import java.util.Random;
import java.util.Scanner;

//Nim game
public class Nim extends ARepl {
	private AiBrain aI; //Computer Player object
	private AiBrain.NimMove move; //Returnable object that holds Computer's row and stone choice
	
	public Scanner input; //User input
	
	private int[] board; //array for rows on the board
	private int boardSize; //size of the board
	private int maxNumStones; // maximum number of stones each row can hold
	private int maxBoardSize; // maximum number of rows on the board
	private int turn; //turn count
	
	private int p1Row; //Player 1's row choice
	private int p1Stone; //Player 1's number of stones choice
	private boolean gameOver; //check for quitting the game
	
	//Random number generator variables
	private long seed;
	private Random rand;
	
	//Default Constructor
	public Nim()
	{
		input = new Scanner(System.in);
		turn = 1;
		maxBoardSize = 8;
		maxNumStones = 10;
		gameOver = false;
		randomNumSetup();
	}
	public int getBoardStone(int boardRow){ return board[boardRow]; }
	
	// Returns board size
	public int getBoardSize(){ return boardSize;}
	
	//Returns maximum number of stones possible.
	public int getMaxNumStones(){ return maxNumStones;}
	
	//Random number Generator Setup
	public void randomNumSetup()
	{
		seed = System.currentTimeMillis();
		rand = new Random(seed);
	}
	
	// Generator for the board size
	public int randomBoardSize()
	{
		return rand.nextInt(4) + 3;
	}
	
	// Generator for the number of stones per row.
	public int randomStoneSize()
	{
		return rand.nextInt(8) + 3;
	}
	
	//Print the game board
	public void displayGameBoard()
	{
		System.out.println("\n**** Game Board ****");
		for (int i = 1; i <= boardSize; i++)
		{
			System.out.print(i + ": ");
			for (int j = 1; j <= board[i - 1]; j++)
			{
				System.out.print("O");
				if (j % 3 == 0)
				{
					System.out.print(" ");
				}
			}
			System.out.println();
		}
	}
	
	//Called whenever respond is on Player 1's turn
	public void playerUpdateBoard()
	{
		//Checks to make sure user input is bounded by the number of rows
		if(p1Row > 0 && p1Row <= boardSize)
		{
			//Checks if player's stone choice is under or equal to the current number of stones in the row.
			//Takes away stones from the row.
			if (p1Stone > 0 && p1Stone <= board[p1Row - 1])
			{
				System.out.println("\nPlayer removes " + p1Stone + " stone(s) from row " + p1Row);
				board[p1Row - 1] -= p1Stone;
			}
			//Takes away maximum stones in the row.
			else
			{
				System.out.println("\nPlayer removes " + board[p1Row - 1] + " stone(s) from row " + p1Row);
				board[p1Row - 1] = 0;
			}
			turn++;
		}
		//Invalid row choice
		else
		{
			System.out.println("\nYou tried to pick a row that is not available.");
			System.out.println("Please re-enter your move.");
		}
	}
	
	//Called whenever respond() is on Player 2's turn
	public void brainUpdateBoard()
	{
		displayGameBoard();
		System.out.println("\nBrain, your turn.");
		System.out.println("Brain removes " + move.getBrainStone() + " stone(s) from row " + move.getBrainRow());
		board[move.getBrainRow() - 1] -= move.getBrainStone();
		turn++;
		
	}
	
	//Create a random board(3-6 rows) with a random number of stones (3-10)
	//in each row.
	@Override
	public void setup()
	{
		boardSize = randomBoardSize(); //Generate number of rows
		board = new int[boardSize];
		for (int i = 0; i < boardSize; i++)
		{
			board[i] = randomStoneSize(); //Generate number of stones per row
		}
		aI = new AiBrain(this); //Create a new AI
	}
	
	//Explain the game to the user.
	@Override
	public void hello() 
	{
		System.out.println("\n***********");
		System.out.println("**  NIM  **");
		System.out.println("***********\n");
		System.out.println("The classic game of nim has been around for generations.");
		System.out.println("This game is a two player game.");
		System.out.println("To play, you must select a row and the number of stones");
		System.out.println("to take out of that row.");
		System.out.println("Each person must take at least one stone on their turn.");
		System.out.println("The goal is to be the last person to take a stone.");
		System.out.println("To quit: Enter 0 for either the row or number of stones.");
	}

	//Ask the user for their row and stone choice.
	@Override
	public void listen() 
	{
		//Player 1 Turn
		if(turn % 2 == 1)
		{
			//Printing the board
			displayGameBoard();
			//Player 1 choose input
			System.out.println("\nPlayer 1, your turn.");
			System.out.print("\nPlease pick a row: ");
			p1Row = input.nextInt();
			System.out.print("Please pick a number of stones: ");
			p1Stone = input.nextInt();
		}
		//Player 2 Turn
		else
		{
			//Player 2 Turn
			move = aI.getMoveNim();
		}
	}
	
	//Calls playerMove() if Player 1's turn. Calls brainMove() if Player 2's turn.
	@Override
	public void respond() 
	{
		//Game over if user enters a 0 for row or num of stones.
		if (p1Row == 0 || p1Stone == 0)
		{
			gameOver = true;
		}
		//Game is not over
		else
		{
			//Player 1
			if(turn % 2 == 1)
			{
				playerUpdateBoard();
			}
			//Player 2
			else
			{
				brainUpdateBoard();
			}
		}
	}
	
	//If the total sum of numbers in the board array is zero or
	//the user enters a zero, then the game is over. Else, the
	//game continues.
	@Override
	public boolean endCheck() 
	{
		if(gameOver)
		{
			System.out.println("\nPlayer 1 has quit the game.");
			return gameOver;
		}
		int sum = 0;
		for (int i = 0; i < boardSize; i++)
		{
			sum+= board[i];
		}
		if (sum != 0)
		{
			return false;
		}
		return true;
	}
	
	//Prints out and empty board and decides who the winner is based on turn.
	@Override
	public void cleanup() 
	{
		displayGameBoard();
		if ((turn + 1) % 2 == 1)
		{
			System.out.println("\nHooray! Player 1 Wins!");
		}
		else
		{
			System.out.println("\nBoooo! Brain Wins!");
		}
	}
	
	public static void main(String[] args)
	{
		Nim n = new Nim();
		n.repl();
	}
}
