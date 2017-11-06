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
 * 
 * Nim_GUI_L1 is a graphical user interface of the classic NIM game. Nim_GUI uses
 * a JFrame with JPanels, JLabels, JButtons, and a call back method inherited
 * from ActionListener.
 * 
 * A new class was created to match with AiBrain_L1 with minimal break points.
 */

package cecs277.project_3;

import java.util.Random;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Nim_GUI_L1 extends JFrame implements ActionListener
{
	boolean done = false; // Control variable for finishing gameplay.
	// ---------------------------------------------------- Consts ----
	final int K_FONT_SIZE = 16;
	final int K_WIN_X = 100;    // Custom Window.
	final int K_WIN_Y = 100;
	final int K_WIDTH = 275;
	final int K_HEIGHT = 350;
	final String K_FONT_NAME = "Times";
	// ---------------------------------------------------- Slots ----
	JPanel m_pnl_top = new JPanel( ); // Top-level panel in frame.
	ArrayList<JPanel> m_pnl_row_array = new ArrayList<JPanel>(); //Holds all row panels created
	JPanel m_pnl_row = new JPanel(); // Panel used to hold a row of buttons.
	JLabel m_lbl_hi = new JLabel( "Welcome To NIM: Challenge Mode!" ); //Introduction
	JLabel m_lbl_instruction = new JLabel( "Please pick a stone." ); //Instructions
	JLabel m_lbl_choice = new JLabel("No Stones Chosen"); //Filled for turn's choice.
	JLabel m_lbl_turn = new JLabel("Player 1's Turn"); //Filled for turn instruction.
	JButton m_btn_press_brain = new JButton( "Cue Brain" ); //Activate Brain Button
	JLabel m_lbl_row = new JLabel(""); //Filled in when rows are generated
	// ---------------------------------------------------- Slots from NIM ----
	final int maxNumStones = 10; // maximum number of stones each row can hold
	final int maxBoardSize = 6; // maximum number of rows on the board
	AiBrain_L1 aI; //Computer Player object
	AiBrain_L1.NimMove move; //Returnable object that holds Computer's row and stone choice
	int[] board; //array for rows on the board
	int boardSize; //size of the board
	int turn; //turn count of the game
	//Random number generator variables
	long seed; 
	Random rand;
	// ---------------------------------------------------- methods from NIM ----
	// Returns board size
	public int getBoardSize(){ return boardSize;}
	// Returns the value of a given row on the board.
	public int getBoardStone(int boardRow) {return board[boardRow]; }
	//Returns maximum number of stones possible.
	public int getMaxNumStones(){ return maxNumStones;}
	//Random number Generator Setup
	public void randomNumSetup()
	{
		seed = System.currentTimeMillis();
		rand = new Random(seed);
	}	
	// Generator for the board size
	public int randomBoardSize(){return rand.nextInt(4) + 3;}	
	// Generator for the number of stones per row.
	public int randomStoneSize(){return rand.nextInt(8) + 3;}
	// ----------------------------------------------------- ZButton ----
	class ZButton extends JButton // Inner class.
	{
		int m_row; //Stone's row
		int m_stone; //Stone's number
		ZButton( int rix )
		{
			super( "" + rix );
			m_stone = rix;
		};
	}
	// ---------------------------------------------------- CTOR ----
	public Nim_GUI_L1( )
	{
		super( "NIM" );  // Call mom for frame title.
		setSize( K_WIDTH, K_HEIGHT );
		setLocation( K_WIN_X, K_WIN_Y );
		add( m_pnl_top );
		setup_label_fonts( new Font( K_FONT_NAME, Font.BOLD, K_FONT_SIZE ) );
		board = new int[maxBoardSize]; //setup game board
		randomNumSetup(); //Setup all random numbers
		turn = 1;
		setup(); //pastes the introduction and instructions to the jframe.
		aI = new AiBrain_L1(this); //Create a new AI
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		
	}
	// ---------------------------------------------------- actionPerformed ----
	// Our callback md.
	@Override
	public void actionPerformed( ActionEvent rev )
	{
		do_click( rev );
	}
	// ------------------------------------------------ do_click ----
	// Handle a click.
	void do_click( ActionEvent rev )
	{
		// Get a button-type object from the event.
		JButton bx;
		Object ox = rev.getSource( ); // Get event source.
		if (! (ox instanceof JButton)) return; // Not our kind?  Punt!
		bx = (JButton) ox; // Downcast to button type.
		// Dispatch to source button's do_press.
		if (bx.getText( ).equals( "Cue Brain" )) do_press_brain( bx );
		else do_press_stone( bx );
	}
	// ---------------------------------------------------- do_press_brain ----
	//User clicks on the "Cue Brain" Button
	void do_press_brain(JButton bx )
	{
		//Getting a move from AiBrainGUI to change the board.
		move = aI.getMoveGUI();
		board[move.getBrainRow()] -= move.getBrainStone();
		remove_rows();
		setup_board_rows();
		m_lbl_choice.setText("Choice: Row " + (move.getBrainRow() + 1) + ", Stone " + move.getBrainStone());
		m_lbl_turn.setText( "Player 1's Turn" ); // Show user that it is now their turn.
		endcheck();
		turn++;
	}
	// ---------------------------------------------------- do_press_stone ----
	// Announce the button index.
	void do_press_stone( JButton bx )
	{
		if (! (bx instanceof ZButton)) return; // Not our kind?  Punt!
		ZButton zbx = (ZButton) bx; // Downcast to button type.
			dbg("Row: " + zbx.m_row);
		board[zbx.m_row] = zbx.m_stone - 1;
		remove_rows();
		setup_board_rows();
		m_lbl_choice.setText("Choice: Row " + (zbx.m_row + 1) + ", Stone " + zbx.m_stone);
		m_lbl_turn.setText( "Brain's Turn" ); // Show user it is now brain's turn.
		endcheck();
		turn++;
	}
	// ---------------------------------------------------- remove_rows ----
	// Deletes all row panels.
	void remove_rows()
	{
		for(int i = 0; i< m_pnl_row_array.size(); i++)
			m_pnl_top.remove(m_pnl_row_array.get(i));
		m_pnl_top.revalidate();
	}
	// ------------------------------------------------ setup_label_fonts ----
	// Fontify the labels.
	void setup_label_fonts( Font rfont )
	{
		m_lbl_hi.setFont( rfont );
		m_lbl_instruction.setFont( rfont );
		m_lbl_choice.setFont(rfont );
		m_lbl_turn.setFont( rfont );
	}
	// ------------------------------------------------------- setup ----
	//Explain the game to the user.
	public void setup() 
	{
		LayoutManager layx = new GridLayout( 11, 1 ); // rows,columns layout.
		m_pnl_top.setLayout( layx ); // Grid layx adds parts, each into cell, filling one row from the left.
		m_pnl_top.add( m_lbl_hi );
		m_pnl_top.add( m_lbl_instruction );
		m_pnl_top.add(m_lbl_choice);
		m_pnl_top.add( m_lbl_turn );
		m_pnl_top.add(m_btn_press_brain);
		m_btn_press_brain.addActionListener( this ); // Listen to brain button.
		setup_random_board();
		setup_board_rows();
	}
	// ------------------------------------------------------- setup_random_board ----
	//Create a random board(3-6 rows) with a 
	//random number of stones (3-10) in each row.
	void setup_random_board()
	{
		boardSize = randomBoardSize();
		for (int i = 0; i < boardSize; i++)
			board[i] = randomStoneSize();
	}
	// ------------------------------------------------ setup_board_rows ----
	// Setup board of buttons leading with the row number 
	// and titled with their index.
	void setup_board_rows()
	{
		for (int ix = 0; ix < boardSize; ++ ix )
		{
			JPanel pnl_row = new JPanel(new FlowLayout(FlowLayout.LEFT));
			m_lbl_row = new JLabel("");
			m_lbl_row.setText(""+ (ix + 1) + ". ");
			pnl_row.add(m_lbl_row);
			m_pnl_top.add( pnl_row );
			m_pnl_row_array.add(pnl_row); //adds panels to the array list
				
			for (int jx = 0; jx < board[ix]; jx++)
			{
				//System.out.print(jx + 1);
				ZButton bx = new ZButton(jx + 1);
				bx.m_row = ix;
			    bx.setPreferredSize(new Dimension(20, 20));
			    pnl_row.add( bx );
			    bx.addActionListener( this ); // Listen to new row btn.
			}
			dbg("");
		}
		dbg("Finished iteration");
	}
	
	// ---------------------------------------------------- endcheck ----
	//Sums all the stones on total board to check for zero stones
	//If ended on Player's turn, then player wins. Else, brain wins
	void endcheck()
	{
		int sum = 0;
		for (int i = 0; i< boardSize; i++)
			sum += board[i];
		if (sum == 0)
		{
			m_btn_press_brain.setEnabled(false); //Turn off brain button.
			if(turn % 2 == 1)
				m_lbl_turn.setText("Player 1 Wins!");
			else
				m_lbl_turn.setText("Brain Wins...");
		}
	}
	// ---------------------------------------------------- debugger ----
	// System out debugger
	void dbg(String s)
	{
		//System.out.println(s);
	}
	// ---------------------------------------------------- main ----
	public static void main( String[] args )
	{
		Nim_GUI_L1 ngx = new Nim_GUI_L1();
		ngx.setVisible(true);
	}
	
}
