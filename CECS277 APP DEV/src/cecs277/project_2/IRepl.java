/*
 * Scott Roberts
 * SID: 011651729
 * CECS 277
 * Email: s.e.roberts0@gmail.com
 * 
 * (Interface Read-Evaluate-Print Loop) IRepl is the structure for all project games,
 * including the GameMaster.
 */

package cecs277.project_2;

public interface IRepl 
{
	public void repl(); //(Read-evaluate-print loop) Control structure for most games.
	public void setup(); //Load any details needed for the GameMaster/games to operate.
	public void hello(); //Introduce the program.
	public void listen(); //Wait for user input.
	public void respond(); //Perform calculations.
	public boolean endCheck(); //Check for completion.
	public void cleanup(); //Finish with ending remarks, creating high scores, etc.
}
