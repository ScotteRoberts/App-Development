package cecs277.graphics_practice;

//Press Me Pgm (after Farrell 2014, Fig 14-26,27 pg 768)
//Time-stamp: <2016-10-08 16:55:25 Chuck Siska>
//
//Samples of callback use, layout mgrs, button row, inner class.
//Callback style: callback-wrapper is ~ the Java event-source-owner.

import javax.swing.*;
import java.awt.*;
import java.awt.event.*; // For ActionEvent.

public class Press_me extends JFrame implements ActionListener
{
// ---------------------------------------------------- TOC ----
//   Consts
//   Slots
//  - class ZButton inner
//  -- CTOR Press_me( ) public
//  actionPerformed( ActionEvent rev ) public void  // Callback.
//  add_w_panel( JComponent rpart ) void  // Add widget in its own panel.
//  do_click( ActionEvent rev ) void  // Handle a click.
//  do_press2( ) void  // Say hello loudly.
//  do_press_me( ) void  // Say hello nicely.
//  do_press_int( JButton bx ) void  // Announce the button index.
//  setup_btn_row( int rcnt ) void // Setup a row of btns titled w index.
//  setup_labal_fonts( Font rfont ) void  // Fontify the labels.
//  setup_widgets_flow( ) void  // Setup widgets via FlowLayout.
//  setup_widgets_border( ) void  // Setup widgets via BorderLayout.
//  setup_widgets_grid( ) void  // Setup widgets via GridLayout.
//  setup_widgets_box( ) void  // Setup widgets via BoxLayout + btn_row.
//  to_capital( String rstr ) String  // Capitalize the string start.
//  -- main static

// ---------------------------------------------------- Consts ----
final int K_FONT_SIZE = 16;
final int K_TBOX_SIZE = 20; // Textbos.
final int K_WIN_X = 100;  // Custom Window.
final int K_WIN_Y = 100;
final int K_WIDTH = 275;
final int K_HEIGHT = 250;
final String K_FONT_NAME = "Arial";
final String K_PRESS_ME = "Press for nice";
final String K_PRESS2 = "Press for loud";
// ---------------------------------------------------- Slots ----
JPanel m_pnl_top = new JPanel( ); // Top-level panel in frame.
JButton m_btn_press_me = new JButton( K_PRESS_ME );
JButton m_btn_press2 = new JButton( K_PRESS2 );
JLabel m_lbl_greeting = new JLabel( "" ); // Filled on btn press.
JLabel m_lbl_hi = new JLabel( "Hi!  Enter name & click button." );
JLabel m_lbl_question = new JLabel( "What is your name?" );
JTextField m_tbox_answer = new JTextField( K_TBOX_SIZE );
// ----------------------------------------------------- ZButton ----
class ZButton extends JButton // Inner class.
{
 int m_val;
 ZButton( int rix )
 {
   super( "" + rix );
   m_val = rix;
 };
}
// ---------------------------------------------------- CTOR ----
public Press_me( )
{
 super( "Press_me" );  // Call mom for frame title.
 setSize( K_WIDTH, K_HEIGHT );
 setLocation( K_WIN_X, K_WIN_Y );
 add( m_pnl_top );
 setup_labal_fonts( new Font( K_FONT_NAME, Font.BOLD, K_FONT_SIZE ) );
 { // Uncomment just one of these setups: each uses a diff layout mgr.
   //setup_widgets_flow( );
   //setup_widgets_border( );
   setup_widgets_grid( );
   //setup_widgets_box( ); // Includes a button-row.
 }
 setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
 m_btn_press_me.addActionListener( this ); // Listen to btn 1.
 m_btn_press2.addActionListener( this ); // Listen to btn 2.
}
// ---------------------------------------------------- actionPerformed ----
// Our callback md.
public void actionPerformed( ActionEvent rev )
{
 do_click( rev );
}
// ------------------------------------------------ add_w_panel ----
// Add widget in its own panel.
void add_w_panel( JComponent rpart )
{
 JPanel pnlx = new JPanel( );
 m_pnl_top.add( pnlx );
 pnlx.add( rpart );
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
 // Dispatch to source button's doit md.
 if (bx.getText( ).equals( K_PRESS_ME )) do_press_me( );
 else if (bx.getText( ).equals( K_PRESS2 )) do_press2( );
 else do_press_int( bx );
}
// ---------------------------------------------------- do_press2 ----
// Say hello loudly.
void do_press2( )
{
 String sbox = m_tbox_answer.getText( ); // Get name.
 if (sbox.equals( "" )) sbox = "unknown"; // Fix no name.
 String sgreet = "Hello, " + sbox.toUpperCase( ); // Be loud.
 m_lbl_greeting.setText( sgreet ); // Show user.
}
// ---------------------------------------------------- do_press_me ----
// Say hello nicely.
void do_press_me( )
{
 String sbox = m_tbox_answer.getText( ); // Get name.
 if (sbox.equals( "" )) sbox = "unknown"; // Fix no name.
 String sgreet = "Hello, " + to_capital( sbox ); // Be nice.
 m_lbl_greeting.setText( sgreet ); // Show user.
}
// ---------------------------------------------------- do_press_int ----
// Announce the button index.
void do_press_int( JButton bx )
{
 if (! (bx instanceof ZButton)) return; // Not our kind?  Punt!
 ZButton zbx = (ZButton) bx; // Downcast to button type.
 m_lbl_greeting.setText( "I'm Button #" + zbx.m_val ); // Show user.
}
// ------------------------------------------------ setup_btn_row ----
// Setup a row of buttons titled w their index.
void setup_btn_row( int rcnt )
{
 JPanel pnl_row = new JPanel( );
 m_pnl_top.add( pnl_row );
 for (int ix = 0; ix < rcnt; ++ ix )
   {
     ZButton bx = new ZButton( ix );
     pnl_row.add( bx );
     bx.addActionListener( this ); // Listen to new row btn.
   }
}
// ------------------------------------------------ setup_labal_fonts ----
// Fontify the labels.
void setup_labal_fonts( Font rfont )
{
 m_lbl_hi.setFont( rfont );
 m_lbl_question.setFont( rfont );
 m_lbl_greeting.setFont( rfont );
}
// ------------------------------------------------- setup_widgets_flow ----
// Setup widgets via FlowLayout.
void setup_widgets_flow( )
{
 LayoutManager layx = new FlowLayout( ); // Auto-wrap row of widgets.
 m_pnl_top.setLayout( layx );

 m_pnl_top.add( m_lbl_hi );
 m_pnl_top.add( m_lbl_question );
 m_pnl_top.add( m_tbox_answer );
 m_pnl_top.add( m_lbl_greeting );
 m_pnl_top.add( m_btn_press_me );
 m_pnl_top.add( m_btn_press2 );
}
// ------------------------------------------------ setup_widgets_border ----
// Setup widgets via BorderLayout.
void setup_widgets_border( )
{
 LayoutManager layx = new BorderLayout( ); // NEWS compass layout.
 m_pnl_top.setLayout( layx );

 // Warning, double "North" overwrites, etc.
 JPanel pnl_n = new JPanel( );
 JPanel pnl_c = new JPanel( );
 JPanel pnl_s = new JPanel( );
 m_pnl_top.add( pnl_n, "North" );
 m_pnl_top.add( pnl_c, "Center" );
 m_pnl_top.add( pnl_s, "South" );
 pnl_n.add( m_lbl_hi );
 pnl_n.add( m_lbl_question );
 pnl_c.add( m_tbox_answer );
 pnl_c.add( m_lbl_greeting );
 pnl_s.add( m_btn_press_me );
 pnl_s.add( m_btn_press2 );
}
// ------------------------------------------------ setup_widgets_grid ----
// Setup widgets via GridLayout.
void setup_widgets_grid( )
{
 LayoutManager layx = new GridLayout( 3, 2 ); // rows,columns layout.
 m_pnl_top.setLayout( layx );

 // Grid layx adds parts, each into cell, filling one row from the left.
 m_pnl_top.add( m_lbl_hi );
 m_pnl_top.add( m_lbl_question );
 m_pnl_top.add( m_tbox_answer );
 m_pnl_top.add( m_lbl_greeting );
 m_pnl_top.add( m_btn_press_me );
 m_pnl_top.add( m_btn_press2 );
}
// ------------------------------------------------ setup_widgets_box ----
// Setup widgets via BoxLayout + btn_row.
void setup_widgets_box( )
{
 LayoutManager layx = new BoxLayout( m_pnl_top, BoxLayout.Y_AXIS );
 m_pnl_top.setLayout( layx );

 add_w_panel( m_lbl_hi );
 add_w_panel( m_lbl_question );
 add_w_panel( m_tbox_answer );
 m_pnl_top.add( Box.createRigidArea( new Dimension( 0, 20 ))); // Blank box.
 add_w_panel( m_lbl_greeting );
 JPanel pnl_row_bot = new JPanel( );
 m_pnl_top.add( pnl_row_bot );
 pnl_row_bot.add( m_btn_press_me );
 pnl_row_bot.add( m_btn_press2 );
 setup_btn_row( 5 );
}
// ------------------------------------------------------- to_capital ----
// Capitalize the string start.
String to_capital( String rstr )
{
 if (null == rstr ) return rstr; // No string.
 if (rstr.equals( "" )) return rstr; // Empty string.
 Character c0 = Character.toUpperCase( rstr.charAt( 0 )); // Cap 1st letter.
 return c0 + rstr.substring( 1, rstr.length( ));
}
// ---------------------------------------------------- main ----
public static void main( String[] args )
{
 Press_me pmx = new Press_me( ); // Get new custom window.
 pmx.setVisible( true ); // Show window to user.
}
}

