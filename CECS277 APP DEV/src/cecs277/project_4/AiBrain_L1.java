/*
 * Scott Roberts
 * SID: 011651729
 * CECS 277
 * Email: s.e.roberts0@gmail.com
 * 
 * The AiBrain_L1 class is used as Player 2 in the Nim game. Whenever player 2
 * has a turn in Nim, AiBrain creates a NimMove object that holds a prioritized
 * row and stone choice based off of the current board's state. 
 * The NimMove object is passed backto a Nim, Nim_GUI, or Nim_GUI_L1 object and 
 * is processed there.
 */

package cecs277.project_4;

import java.util.Random;

//Computer used in Nim's game
public class AiBrain_L1 {
	Nim nimObj;
	Nim_GUI nimGUIObj;
	Nim_GUI_L1 nimGUIObj_L1;
	private int boardSize; // size of the board
	int board[];
	private NimMove move; // object of helper class NimMove
	private int maxNumStones; //maximum stones available for random choice
	
	private int getBoardStone(int row)
	{
		return nimGUIObj_L1.getBoardStone(row);
	}
	
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

	//Overloaded Constructor for text based NIM
	public AiBrain_L1(Nim nimObj)
	{
		this.nimObj = nimObj;
		boardSize = nimObj.getBoardSize();
		board = new int[boardSize];
		maxNumStones = nimObj.getMaxNumStones();
		randomNumSetup();
		move = new NimMove();
	}
	
	//Overloaded Constructor for Nim_GUI
	public AiBrain_L1(Nim_GUI nimGUIObj)
	{
		this.nimGUIObj = nimGUIObj;
		boardSize = nimGUIObj.getBoardSize();
		board = new int[boardSize];
		maxNumStones = nimGUIObj.getMaxNumStones();
		randomNumSetup();
		move = new NimMove();
	}
	
	public AiBrain_L1(Nim_GUI_L1 nimGUIObj_L1)
	{
		System.out.println("Begin");
		this.nimGUIObj_L1 = nimGUIObj_L1;
		boardSize = nimGUIObj_L1.getBoardSize();
		board = new int[boardSize];
		maxNumStones = nimGUIObj_L1.getMaxNumStones();
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
		return rand.nextInt(boardSize);
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
		boolean flag = false;
		if(!flag) flag = rule1GUI();
		if(!flag) flag = rule2GUI();
		if(!flag) flag = rule3GUI();
		if(!flag) flag = rule4GUI();
		if(!flag) randomMove();
		return move;
	}
	//If there is one row, clear the row.
	private boolean rule1GUI()
	{
		//System.out.println("Enter rule 1.");
		int rowCount = 0;
		boolean twoRowFlag = false;
		int removeIndex = -1;
		for (int i = 0; i < boardSize; i++)
		{
			if(getBoardStone(i) > 0)
			{
				rowCount++;
				if (rowCount == 1)
				{
					removeIndex = i;
				}	
				if(rowCount == 2)
					twoRowFlag = true;
			}
		}
		if (!twoRowFlag)
		{
			move.brainRow = removeIndex;
			move.brainStone = getBoardStone(removeIndex);
			return true;
		}
		return false;
	}
	//Returns the largest row index on the board.
	int largestRow()
	{
		int largest = 0;
		int largestIndex = -1;
		for (int i = 0; i < boardSize; i++)
		{
			if(getBoardStone(i) > 0)
			{
				if (largest < getBoardStone(i))
				{
					largest = getBoardStone(i);
					largestIndex = i;
				}
			}
		}
		return largestIndex;
	}
	//Using the largest row index, finds and returns the second
	//largest row index.
	int secondLargestRow(int largestIndex)
	{
		int second = 0;
		int secondIndex = 0;
		for (int i = 0; i < boardSize; i++)
		{
			if(getBoardStone(i) > 0)
			{
				if (second < getBoardStone(i) && i != largestIndex)
				{
					second = getBoardStone(i);
					secondIndex = i;
				}
			}
		}
		return secondIndex;
	}
	//returns true if any two non-zero rows have the same value.
	boolean hasTwins()
	{
		boolean twinsFlag = false;
		for (int i = 0; i < boardSize; i++)
		{
			if(getBoardStone(i) > 0)
			{
				for (int j = i + 1; j < boardSize; j++)
				{
					if (getBoardStone(i) == getBoardStone(j)) 
						twinsFlag = true;
				}
			}
		}
		return twinsFlag;
	}
	// returns true if there are an even number of non-zero rows.
	boolean hasEvenRows()
	{
		int rowCount = 0;
		for (int i = 0; i < boardSize; i++)
		{
			if(getBoardStone(i) > 0)
			{
				rowCount++;
			}
		}
		if (rowCount % 2 == 0)
			return true;
		return false;
	}
	
	//If there are an even number of rows and no twins, make twins
	//out of the largest row.
	private boolean rule2GUI()
	{
		//System.out.println("Enter rule 2.");
		int largestIndex = largestRow();
		int secondIndex = secondLargestRow(largestIndex);
		boolean evenRows = hasEvenRows();
		boolean twinsFlag = hasTwins();
		if (evenRows && !twinsFlag)
		{
			move.brainRow = largestIndex;
			move.brainStone = getBoardStone(largestIndex) - getBoardStone(secondIndex);
			return true;
		}
		return false;
	}
	//If there are twins and a fat row, delete the fat row.
	private boolean rule3GUI()
	{
		//System.out.println("Enter rule 3.");
		boolean twinsFlag = hasTwins();
		int rowCount = 0;
		int twin1Index = -1;
		int twin2Index = -1;
		int removeIndex = -1;
		for (int i = 0; i < boardSize; i++)
		{
			if(getBoardStone(i) > 0)
			{
				rowCount++;	
				for (int j = i + 1; j < boardSize; j++)
				{
					//Finds the twins and sets there indices
					if (getBoardStone(i) == getBoardStone(j))
					{
						twin1Index = i;
						twin2Index = j;
					}	
				}
			}
		}
		//Once twin indices are found, finds the left over non-zero row.
		for (int i = 0; i < boardSize; i++)
		{
			if (twin1Index != i && twin2Index != i && getBoardStone(i) > 0)
			{
				removeIndex = i;
			}
		}
		
		if (twinsFlag && rowCount == 3)
		{
			move.brainRow = removeIndex;
			move.brainStone = getBoardStone(removeIndex);
			return true;
		}
		return false;
	}
	//If there are more than three rows delete the smallest row.
	private boolean rule4GUI()
	{
		//System.out.println("Enter rule 4.");
		int rowCount = 0;
		int shortest = 10;
		int shortestIndex = -1;
		for (int i = 0; i < boardSize; i++)
		{
			if(getBoardStone(i) > 0)
			{
				rowCount++;
				if (getBoardStone(i) < shortest)
				{
					shortest = getBoardStone(i);
					shortestIndex = i;
				}
			}
		}
		if (rowCount > 3)
		{
			move.brainRow = shortestIndex;
			move.brainStone = shortest;
			return true;
		}
		return false;
	}
	//Assigns the random numbers for computer's row and stone choice
	private void randomMove()
	{
		//System.out.println("Enter rule 5.");
		boolean valid = false;
		while(!valid)
		{
			move.brainRow = randomBoardNum();
			if(getBoardStone(move.brainRow) > 0)
			{
				move.brainStone = 1; //not a random number of stones chosen
				valid = true;
			}
				
		}
	}
}







