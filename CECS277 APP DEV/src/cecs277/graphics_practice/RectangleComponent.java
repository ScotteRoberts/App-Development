package cecs277.graphics_practice;

import java.awt.*;
import javax.swing.JComponent;

public class RectangleComponent extends JComponent{
	public void paintComponent(Graphics g)
	{
		//Recover Graphics2D
		Graphics2D g2 = (Graphics2D) g;
		
		//Construct a rectangle
		Rectangle box = new Rectangle(5,10,20,30);
		
		//Put the box onto the JFrame
		g2.draw(box);
		
		//Move the x and y coordinates
		box.translate(15, 25);
		
		//Draw another box
		g2.draw(box);
		
		Rectangle box2 = new Rectangle(1,1,396,490);
		g2.draw(box2);
	}
}
