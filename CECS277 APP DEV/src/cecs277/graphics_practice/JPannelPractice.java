package cecs277.graphics_practice;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;

public class JPannelPractice {
	public static void main (String []args)
	{
		//setSize(500,500);
	    //JPanel dp = new DrawPanel(background);
		
		//Practicing with buttons
		JButton b = new JButton("Cold");
		b.setFont(new Font("Monospaced",Font.BOLD,12));
		b.setBackground(Color.yellow);
		b.setForeground(Color.blue);
		b.setVisible(true);
		
	}

}
