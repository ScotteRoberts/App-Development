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

package cecs277.project_3;

import java.util.Random;

//Computer used in Nim's game
public class AiBrain {
	Nim nimObj;
	Nim_GUI nimGUIObj;
	private int boardSize; // size of the board
	private NimMove move; // object of helper class NimMove
	private int maxNumStones; //maximum stones available for random choice
	
	//Random number generator variables
	private long seed;
	private Random rand;
	
	//Helper Class used to hold the computer's move.
	public class NimMove
	{
		private int brainRow; //Player 2's row choice
		private int brainStone; //Player 2's number of stones choice
		public int getBrainRow() {return brainRow;}
		public int getBrainStone() {return brainStone;}
	}
	
	//Default Constructor
	public AiBrain()
	{
		boardSize = 8;
		maxNumStones = 10;
		randomNumSetup();
		move = new NimMove();
	}
	
	//Overloaded Constructor for text based NIM
	public AiBrain(Nim nimObj)
	{
		this.nimObj = nimObj;
		boardSize = nimObj.getBoardSize();
		maxNumStones = nimObj.getMaxNumStones();
		randomNumSetup();
		move = new NimMove();
	}
	
	//Overloaded Constructor for Nim_GUI
	public AiBrain(Nim_GUI nimGUIObj)
	{
		this.nimGUIObj = nimGUIObj;
		boardSize = nimGUIObj.getBoardSize();
		maxNumStones = nimGUIObj.getMaxNumStones();
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
	public NimMove getMoveNim()
	{
		setMoveNim();
		return move;
	}
	
	//Assigns the random numbers for computer's row and stone choice
	//Checking to make sure an appropriate random number
	//for both the row and the number of stones
	private void setMoveNim()
	{
		boolean valid = false;
		while(!valid)
		{
			move.brainRow = randomBoardNum();
			move.brainStone = randomStoneNum();
			if (move.brainStone <= nimObj.getBoardStone(move.brainRow - 1))
			{
				valid = true;
			}
			
		}
		
	}
	//Returns the GetMove information after setMove() is called.
	public NimMove getMoveGUI()
	{
		setMoveGUI();
		return move;
	}
	
	//Assigns the random numbers for computer's row and stone choice
	//Checking to make sure an appropriate random number
	//for both the row and the number of stones
	private void setMoveGUI()
	{
		boolean valid = false;
		while(!valid)
		{
			move.brainRow = randomBoardNum();
			move.brainStone = randomStoneNum();
			if (move.brainStone <= nimGUIObj.getBoardStone(move.brainRow - 1))
			{
				valid = true;
			}
			
		}
	}
}
