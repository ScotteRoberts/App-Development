package cecs277.graphics_practice;

//Rects.java
//Random overlapping rectangles every so often.
//Time-stamp: <2016-10-10 12:51:04 Chuck Siska>
//------------------------------------------------------------
//(Idle code, while looking at Farrell 2014 p 895)
//With a Timer.  And callback wrapper via source-object style.

import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.*; // For ActionEvent.

public class Rects extends JFrame implements ActionListener
{
// ---------------------------------------------------- TOC ----
//   - Rects( ) CTOR
//   actionPerformed( ActionEvent event ) void
//   dbg( String rs void // Print string, for debug output.
//   draw_rand_rect( Graphics rgfx ) void
//   flip_coin( ) boolean
//   paint( Graphics rgfx ) void
//   set_rand_color( Graphics rgfx ) void
//   set_rand_rect_corners( ) void // Use slots.
//   set_rect_wid_hgt( ) void
//   
//   main( String[] args ) void static public

// ---------------------------------------------------- Consts ----
final double K_BULKY_AREA_PCT = 0.16;
final int K_WID = 500;
final int K_HGT = K_WID; // Square size.
final int K_MAX_AREA = (int)(K_WID * K_HGT * K_BULKY_AREA_PCT);
final int K_TICK_DELAY = 200; // For Timer. Millisecs between ticks.
// ---------------------------------------------------- Slots ----
Container m_con = getContentPane( );
Timer m_make_rect_timer; // Make a rect on each tick. Set in CTOR.
int m_x1, m_y1, m_x2, m_y2, m_wid, m_hgt; // For next rect.
// ---------------------------------------------------- CTOR ----
public Rects( )
{
 setSize( K_WID, K_HGT );
 // m_con.setBackground( Color.BLUE );
 m_con.setLayout( new FlowLayout( ) );
 setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
 m_make_rect_timer = new Timer( K_TICK_DELAY, this );
 m_make_rect_timer.start(); // Make the timer tick.
}
// ---------------------------------------------------- actionPerformed ----
public void actionPerformed( ActionEvent event )
{
 // Action that is executed at each timer tick.
 draw_rand_rect( getGraphics( )); // Paint a rect with our graphics "brush".
}
// ---------------------------------------------------- dbg ----
// Print string, for debug output.
// NB, Comment out println to turn off debug output.
public void dbg( String rs ) { // System.out.println( rs );
}

// ---------------------------------------------------- draw_rand_rect ----
// Draw a random (but not too bulky) rect in a random color,
// with the given graphics "brush".
void draw_rand_rect( Graphics rgfx )
{
 if (null == rgfx) return; // No brush? ------ return ----
 set_rand_color( rgfx ); // Set graphics "brush" to random color.
 set_rand_rect_corners( ); // Use slots.
     dbg( "(C "+ m_x1+ " "+ m_y1+ " "+ m_x2+ " "+ m_y2+ ")" );
 set_rect_wid_hgt( ); // Use slots.
     dbg( "(D "+ m_x1+ " "+ m_y1+ " "+ m_wid+ " "+ m_hgt+ ")" );
 rgfx.fillRect( m_x1, m_y1, m_wid, m_hgt ); // Draw the rect.
}
// ---------------------------------------------------- flip_coin ----
// Return random coin flip: true or false.
boolean flip_coin( )
{
 return 0 == (int)( 2 * Math.random( )); // Ret 0==true or 1==false.
}
// ---------------------------------------------------- paint ----
// Let mom paint herself, & add a red square.
public void paint( Graphics rgfx )
{
 super.paint( rgfx ); // Let mom paint herself.
 // Start with a bulky red rect.
 rgfx.setColor( Color.RED ); // Set graphics "brush" to known RED.
 rgfx.fillRect( 40, 40, 120, 120 ); // Draw the rect.
}
// ---------------------------------------------------- set_rand_color ----
// Set the given graphics "brush" (foreground) color randomly.
void set_rand_color( Graphics rgfx )
{
 final int K_CLR_WID = 256;
 int rr = (int)( Math.random( ) * K_CLR_WID ); // Get random RGB values.
 int gg = (int)( Math.random( ) * K_CLR_WID );
 int bb = (int)( Math.random( ) * K_CLR_WID );
 
 Color clrx = new Color( rr, gg, bb ); // Get color from RGB values.
     dbg( "(Color "+ rr+ " "+ gg+ " "+ bb+ ")" );
 rgfx.setColor( clrx ); // Set graphics "brush" to color.
}
// ---------------------------------------------- set_rand_rect_corners ----
// Set random rectangle corners: up-left, low-right.
// Use slots.
void set_rand_rect_corners( )
{
 m_x1 = (int)( Math.random( ) * K_WID ); // Get random pts (for corners).
 m_x2 = (int)( Math.random( ) * K_WID );
 m_y1 = (int)( Math.random( ) * K_HGT );
 m_y2 = (int)( Math.random( ) * K_HGT );
     dbg( "(A "+ m_x1+ " "+ m_y1+ " "+ m_x2+ " "+ m_y2+ ")" );
 // Ensure pt #1 is up-left of pt #2.
 int itmp;
 if (m_x2 < m_x1) { itmp = m_x1; m_x1 = m_x2; m_x2 = itmp; } // Swap.
 if (m_y2 < m_y1) { itmp = m_y1; m_y1 = m_y2; m_y2 = itmp; } // Swap.
     dbg( "(B "+ m_x1+ " "+ m_y1+ " "+ m_x2+ " "+ m_y2+ ")" );
}
// ---------------------------------------------- set_rand_rect_corners ----
// Set rectangle width, height from corners.
// Use slots.
void set_rect_wid_hgt( )
{
 m_wid = Math.abs( m_x2 - m_x1 ); // Get width,height.
 m_hgt = Math.abs( m_y2 - m_y1 );
 // Reduce width or height till rect area isn't too bulky.
 while ( K_MAX_AREA < (m_wid * m_hgt) ) // Is rect too bulky?
   { // Halve wid or hgt by coin flip.
     if (flip_coin( )) m_wid = m_wid / 2;
     else m_hgt = m_hgt / 2;
   }
}
// ---------------------------------------------------- main ----
public static void main( String[] args )
{
 Rects frame = new Rects( );
 frame.setVisible( true );
}
}
