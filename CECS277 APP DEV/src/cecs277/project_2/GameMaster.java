/*
 * Scott Roberts
 * SID: 011651729
 * CECS 277
 * Email: s.e.roberts0@gmail.com
 * 
 * The GameMaster asks the user if they want to play a game. If so, the game
 * menu will display and the user has the chance to pick a game. If a game is
 * chosen, then GameMaster will create an instance of that game until the user
 * finishes and asks to stop playing games.
 */

package cecs277.project_2;

import java.util.Scanner;

//Controller for all games
public class GameMaster extends ARepl
{
	private char cGame; // Controls y/n choice to play.
	private int gChoice; // Controls the game choice and the loop in REPL.
	
	static Scanner scan; // User input Scanner variable
	
	public GameMaster()
	{
		scan = new Scanner(System.in);
		done = false;
		cGame = 'a';
		gChoice = -1;
	}
	
	public void setup(){}
	public void hello()
	{
		System.out.println("*********************************");
		System.out.println("**  Welcome to the GameMaster  **");
		System.out.println("*********************************\n");
		System.out.println("This is the second installment of CECS 277's");
		System.out.println("game projects. Enjoy!\n");
	}
	public void listen()
	{
		done = false;
		gChoice = -1;
		
		if (cGame == 'a') // default
		{
			System.out.println("\nDo you want to play a game? (y/n)");
		}
		else
		{
			System.out.println("\nWould you like to play another game? (y/n)");
		}
		
		cGame = scan.next().charAt(0); // Wait for user input to play the game.
		
		if (cGame == 'y' || cGame == 'Y')
		{
			System.out.println("\nWhich game would you like to play?");
			System.out.println("1. Guess A Number");
			System.out.println("2. NIM");
			System.out.println("3. Game coming soon");
			System.out.println("0. Quit");
			
			gChoice = scan.nextInt(); // Waiting for user input for game choice.
		}
		else
		{
			done = true;
		}
	}
	public void respond()
	{
		if (cGame == 'y' || cGame == 'Y')
		{
			//Picks case and creates an instance of the game based off of User's choice.
			switch(gChoice)
			{
				case 0:
					done = true;
					break;
				case 1:
					GuessANumber gan = new GuessANumber();
					gan.repl(); // Begin the Guess A Number Game
					break;
				case 2:
					Nim n = new Nim();
					n.repl();
					break;
				case 3:
					System.out.println("\nGame 3 is not available at this time. Please pick another game.");
					break;
				default:
					System.out.println("\nThat is not a game choice. Try again!");
			}
		}
	}
	// If user enters n when asked to play again, or if they press 0 to
	// quit the game choice, done will return true.
	public boolean endCheck()
	{
		return done;
	}
	public void cleanup()
	{
		System.out.println("\nHave a wonderful day!");
	};
	
	public static void main(String[] args)
	{
		GameMaster gm = new GameMaster();
		gm.repl();
	}
}
