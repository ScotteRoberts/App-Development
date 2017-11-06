/*
 * Scott Roberts
 * SID: 011651729
 * CECS 277
 * Email: s.e.roberts0@gmail.com
 * 
 * (Abstract Read-Evaluate-Print Loop) Each of the games, including the
 * GameMaster use the same repl() method.
 */

package cecs277.project_2;

public abstract class ARepl implements IRepl {
	public boolean done = false; // Control variable for finishing gameplay.
	public void repl()
	{
		setup();
		hello();
		while(!done) //setup the flag variable
		{
			listen();
			respond();
			done = endCheck();
		}
		cleanup();
	}

}
