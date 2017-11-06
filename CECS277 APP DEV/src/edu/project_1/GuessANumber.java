/*
 * Scott Roberts
 * SID: 011651729
 * CECS 277
 * Email: s.e.roberts0@gmail.com
 * 
 * Guess a Number is a User-based game that will generate a random number
 * between 1 and 1000. The user must guess until the number is found.
 * The computer will calculate the distance between your current and immediately
 * previous guess the tell the User if they are getting warmer or colder
 * to/from the answer.
 */

package edu.project_1;

import java.util.Scanner;
import java.util.Random;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class GuessANumber extends ARepl
{
	
	static Scanner scan = new Scanner(System.in); // user input
	Scanner scoreReader; // input file reader
	PrintWriter outFile; // output file writer
	
	String name; // User's name for high score
	String address; //address of the input high score file.
	String address2; //address of the output high score file.
	boolean notFound; //loop controlling the file reader
	int turns; // total turns taken
	int pGuess; //previous guess
	int cGuess; //current guess
	int pDistance; //previous guess distance
	int cDistance; //current guess distance
	int answer; // correct answer to guess
	
	//Default Constructor
	public GuessANumber()
	{
		scoreReader = null;
		notFound = true;
		address = "highscores.txt";
		address2 = "tempGAN.txt";
		name = "";
		turns = 0;
		pGuess = -1; 
		cGuess = -1; 
	}
	//Calls a random number generator
	public void setup()
	{
		randomNumGen();
	}
	//Generates a random number between 1 and 1000
	public void randomNumGen() // Random number generator + seed
	{
		long seed = System.currentTimeMillis();
		Random rand = new Random(seed);
		answer = rand.nextInt(1000) + 1;
		//System.out.println(answer);
	}
	public void hello()
	{
		System.out.println("\n**********************");
		System.out.println("**  GUESS A NUMBER  **");
		System.out.println("**********************\n");
		System.out.println("The computer has picked a number between 1 and 1000.");
		System.out.println("Try and guess the number.");
		System.out.println("After your second guess, the game will give you a hint");
		System.out.println("if you are warmer or colder to the answer.");
		System.out.println("If you would like to quit, please press (0).");
	}
	public void listen()
	{
		System.out.println("\nEnter a number: ");
		cGuess = scan.nextInt();
	}
	public void respond()
	{
		turns++;
		//If the player has guessed twice, pDistance will calculate the
		//distance from pGuess to answer. cDistance will calculate the
		//distance from cGuess to answer.
		if (pGuess != -1)
		{
			pDistance = Math.abs(answer - pGuess);
			cDistance = Math.abs(answer - cGuess);
		}
		//User quits
		if (cGuess == 0)
		{
			done = true;
		}
		else
		{
			//Correct answer
			if (cGuess == answer)
			{
				System.out.println("Correct!");
				System.out.println("You guessed the answer in " + turns + " turns");
				done = true;
			}
			//Special case for the first turn.
			else if (turns == 1)
			{
				System.out.println("Incorrect!");
			}
			else
			{
				//User repeats the guess.
				if (cGuess == pGuess)
				{
					System.out.println("You repeated your guess.");
				}
				//Closer to the answer
				if (cDistance < pDistance)
				{
					System.out.println("Warmer.");
				}
				//Farther from the answer
				else
				{
					System.out.println("Colder.");
				}		
			}
			pGuess = cGuess; //previous guess is updated each turn
		}
	}
	public boolean endCheck()
	{
		return done; // We are done or not
	}
	public void cleanup()
	{
		System.out.println("Thank you for the playing the game!\n");
		//User quits, displayHighScore is called
		if (cGuess == 0)
		{
			displayHighScore();
		}
		//User finishes, makeHighScore is called
		else
		{
			makeHighScore();
		}
	}
	
	//Generic input file method using a hard-coded address variable
	public void inputFile()
	{
		while(notFound)
		{
			try
			{
				File infile = new File(address); //address is highscores.txt
				scoreReader = new Scanner(infile);
				notFound = false;
			}
			catch(FileNotFoundException e)
			{
				System.out.println("Invalid address. Please try again.");
				System.out.println("Please enter the address of highscores.txt");
				address = scan.nextLine();
			}
		}
	}
	//Read in and print high scores
	public void displayHighScore()
	{
		inputFile();
		System.out.println("***  High Scores ***");
		
		while(scoreReader.hasNextLine())
		{
			System.out.println(scoreReader.nextLine());
		}
	}
	//User has the chance to make the high score board
	public void makeHighScore()
	{
		System.out.println("Please enter your initials (no digits):");
		scan.nextLine();
		name = scan.nextLine();
		System.out.println();
		
		while(notFound)
		{
			try
			{
				File infile = new File(address);
				outFile = new PrintWriter("tempGAN.txt");
				scoreReader = new Scanner(infile);
				notFound = false;
			}
			catch(FileNotFoundException e)
			{
				System.out.println("Invalid address. Please try again.");
				System.out.println("Please enter the address of highscores.txt");
				address = scan.nextLine();
			}
		}
		
		System.out.println("***  High Scores ***");
		while(scoreReader.hasNextLine())
		{
			String line = scoreReader.nextLine();
			//Processes the high score name and score
			int i = 0;
			while (!Character.isDigit(line.charAt(i))) 
			{ 
				i++; 
			}
			String temp = line.substring(i, line.length()); 
			int score = Integer.parseInt(temp);
			
			if (turns < score)
			{
				outFile.printf("%-10s%10d", name, turns);
				outFile.println();
				System.out.printf("%-10s%10d", name, turns);
				System.out.println();
				turns = 100; // Once the user is put into place, make their turns crazy.
			}
			else
			{
				outFile.println(line);
				System.out.println(line);
			}
		}
		outFile.close();
		System.out.println();
		
		renameFile();
	}
	
	public void renameFile()
	{
		// File with old name
		File file = new File("tempGAN.txt");
		// File with new name
		File file2 = new File("highscores.txt");
		// Rename file
		file.renameTo(file2);
	}
	
	public static void main(String[] args)
	{
		GuessANumber gan = new GuessANumber();
		gan.repl();
	}

}
