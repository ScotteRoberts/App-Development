package cecs277.graphics_practice;

//Painter.java
//Paint by dragging open a rectangle.
//Time-stamp: <2016-10-10 12:22:42 Chuck Siska>
//------------------------------------------------------------
//MouseListener & MouseMotionListener

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.*; // For MouseEvent.

class Rect
{
// ---------------------------------------------------- TOC ----
//   - Rect( int rx1, int ry1, int rwid, int rhgt, Color rcolor ) CTOR
//  dbg( String rs ) void
//  draw_rect( Graphics rgfx ) void
//  set_color( Color rcolor ) void

// ---------------------------------------------------- Slots ----
public Color m_color;
public int m_x1, m_y1, m_wid, m_hgt; // For next rect.
// ---------------------------------------------------- CTOR ----
public Rect( int rx1, int ry1, int rwid, int rhgt, Color rcolor )
{
 m_x1 = rx1;
 m_y1 = ry1;
 m_wid = rwid;
 m_hgt = rhgt;
 m_color = rcolor;
     dbg( "(R "+ m_x1+ " "+ m_y1+ " "+ m_wid+ " "+ m_hgt+ ")" );
     dbg( m_color.toString( ));
}
// ---------------------------------------------------- dbg ----
public void dbg( String rs ) { // System.out.println( rs );
}
// ---------------------------------------------------- draw_rect ----
// Draw a rectagle with the given graphics "brush".
public void draw_rect( Graphics rgfx )
{
 if (null == rgfx) return; // No brush? ------ return ----
 rgfx.setColor( m_color ); // Set graphics "brush" to color.
     dbg( m_color.toString( ));
 rgfx.fillRect( m_x1, m_y1, m_wid, m_hgt ); // Draw the rect.
     dbg( "(D "+ m_x1+ " "+ m_y1+ " "+ m_wid+ " "+ m_hgt+ ")" );
}
// ---------------------------------------------------- set_color ----
// Change the rect's color.
public void set_color( Color rcolor )
{
 m_color = rcolor;
}
}

public class Painter extends JFrame
implements MouseListener, MouseMotionListener
{
// ---------------------------------------------------- TOC ----
//   - Painter( ) CTOR
//  add_rect( ) void
//  dbg( String rs ) void
//  draw_rects( Graphics rgfx ) void
//  get_rand_color( ) Color
//  mouseClicked( MouseEvent rev ) void
//  mouseDragged( MouseEvent rev ) void -- empty
//  mouseEntered( MouseEvent rev ) void -- empty
//  mouseExited( MouseEvent rev ) void -- empty
//  mouseMoved( MouseEvent rev ) void -- empty
//  mousePressed( MouseEvent rev ) void
//  mouseReleased( MouseEvent rev ) void
//  set_rect_corners( ) void
//  set_rect_wid_hgt( ) void
//  
//   main( String[] args ) void static public
//  

// Tools
// mousePressed( ), mouseReleased( )  // MouseListener interface
// mouseDragged( ) // MouseMotionListener interface

// ---------------------------------------------------- Consts ----
final int K_WID = 500;
final int K_HGT = K_WID; // Square size.
// ---------------------------------------------------- Slots ----
ArrayList< Rect > m_arects = new ArrayList< Rect >( );
int m_x1, m_y1, m_x2, m_y2, m_wid, m_hgt; // For next rect.
// ---------------------------------------------------- CTOR ----
public Painter( )
{
 setTitle( "Rectangle Painter" );
 setSize( K_WID, K_HGT );
 setLayout( new FlowLayout( ) );
 setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
 addMouseListener( this );
}
// ---------------------------------------------------- add_rect ----
public void add_rect( )
{
 Rect rx = new Rect( m_x1, m_y1, m_wid, m_hgt, get_rand_color( ));
 m_arects.add( rx );
}
// ---------------------------------------------------- dbg ----
public void dbg( String rs ) { // System.out.println( rs );
}
// ---------------------------------------------------- draw_rects ----
public void draw_rects( Graphics rgfx )
{
 for (Rect rx : m_arects)
   {
     rx.draw_rect( rgfx );
   }
}
// ---------------------------------------------------- get_rand_color ----
// Get a random color.
Color get_rand_color( )
{
 final int K_CLR_WID = 256; // For 8-bit color channels.
 int rr = (int)( Math.random( ) * K_CLR_WID ); // Get random RGB values.
 int gg = (int)( Math.random( ) * K_CLR_WID );
 int bb = (int)( Math.random( ) * K_CLR_WID );
 
 Color clrx = new Color( rr, gg, bb ); // Get color from RGB values.
     dbg( "(Color "+ rr+ " "+ gg+ " "+ bb+ ")" );
 return clrx;
}
// ---------------------------------------------------- mouseClicked ----
public void mouseClicked( MouseEvent rev )
{
 int ix = m_arects.size( ) - 1;
 Rect rx = m_arects.get( ix );
 rx.set_color( get_rand_color( ));
     dbg( "(clk "+ m_x2+ " "+ m_y2+ ")" );
 rx.draw_rect( this.getGraphics( ));
}
// ---------------------------------------------------- mouseDragged ----
public void mouseDragged( MouseEvent rev ) { }
// ---------------------------------------------------- mmouseEntered ----
public void mouseEntered( MouseEvent rev ) { }
// ---------------------------------------------------- mouseExited ----
public void mouseExited( MouseEvent rev ) { }
// ---------------------------------------------------- mouseMoved ----
public void mouseMoved( MouseEvent rev ) { }
// ---------------------------------------------------- mousePressed ----
public void mousePressed( MouseEvent rev )
{
 m_x1 = rev.getX( );
 m_y1 = rev.getY( );
     dbg( "(p1 "+ m_x1+ " "+ m_y1+ ")" );
}
// ---------------------------------------------------- mouseReleased ----
public void mouseReleased( MouseEvent rev )
{
 m_x2 = rev.getX( );
 m_y2 = rev.getY( );
     dbg( "(p2 "+ m_x2+ " "+ m_y2+ ")" );
 set_rect_corners( );
 set_rect_wid_hgt( );
 if ((0 < m_wid) && (0 < m_hgt))
   {
     add_rect( );
     draw_rects( getGraphics( ));
   }
}
// ---------------------------------------------- set_rand_rect_corners ----
// Set rectangle corners: up-left, low-right.
// Use slots.
void set_rect_corners( )
{
 // Ensure pt #1 is up-left of pt #2.
 int itmp;
 if (m_x2 < m_x1) { itmp = m_x1; m_x1 = m_x2; m_x2 = itmp; } // Swap.
 if (m_y2 < m_y1) { itmp = m_y1; m_y1 = m_y2; m_y2 = itmp; } // Swap.
     dbg( "(B "+ m_x1+ " "+ m_y1+ " "+ m_x2+ " "+ m_y2+ ")" );
}
// ---------------------------------------------- set_rect_wid_hgt ----
void set_rect_wid_hgt( )
{
 m_wid = Math.abs( m_x2 - m_x1 ); // Set width,height.
 m_hgt = Math.abs( m_y2 - m_y1 );
}
// ---------------------------------------------------- main ----
public static void main( String[] args )
{
 Painter px = new Painter( );
 px.setVisible( true );
}
}
