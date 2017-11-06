/*
 * Scott Roberts
 * SID: 011651729
 * CECS 277
 * Email: s.e.roberts0@gmail.com
 * 
 * The AiBrain class is used as Player 2 in the Nim game. Whenever player 2
 * has a turn in Nim, AiBrain creates a NimMove object that holds a random
 * row choice and a random stone choice. The NimMove object is passed back
 * to Nim and is processed there.
 */

package cecs277.project_2;

import java.util.Random;

//Computer used in Nim's game
public class AiBrain {
	private int boardSize; // size of the board
	private NimMove move; // object of helper class NimMove
	private int maxNumStones; //maximum stones available for random choice
	
	//Random number generator variables
	private long seed;
	private Random rand;
	
	//Helper Class used to hold the computer's move.
	public class NimMove
	{
		private int p2Row; //Player 2's row choice
		private int p2Stone; //Player 2's number of stones choice
		public int getP2Row() {return p2Row;}
		public int getP2Stone() {return p2Stone;}
	}
	
	//Default Constructor
	public AiBrain()
	{
		boardSize = 8;
		maxNumStones = 10;
		randomNumSetup();
		move = new NimMove();
	}
	
	//Overloaded Constructor
	public AiBrain(Nim nimObj)
	{
		boardSize = nimObj.getBoardSize();
		maxNumStones = nimObj.getMaxNumStones();
		randomNumSetup();
		move = new NimMove();
	}
	
	//Random number Generator Setup
	private void randomNumSetup()
	{
		seed = System.currentTimeMillis();
		rand = new Random(seed);
	}
	
	//Computer's generator for picking a row during guess.
	private int randomBoardNum()
	{
		return rand.nextInt(boardSize) + 1;
	}
		
	//Computer's generator for pick number of stones during guess.
	private int randomStoneNum()
	{
		return rand.nextInt(maxNumStones) + 1;
	}
	
	//Returns the GetMove information after setMove() is called.
	public NimMove getMove()
	{
		setMove();
		return move;
	}
	
	//Assigns the random numbers for computer's row and stone choice
	private void setMove()
	{
		move.p2Row = randomBoardNum();
		move.p2Stone = randomStoneNum();
	}
}
