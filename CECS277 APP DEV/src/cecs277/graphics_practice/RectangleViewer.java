package cecs277.graphics_practice;

import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;

public class RectangleViewer {

	public static void main(String[] args) {
		JFrame j = new JFrame();
		j.setSize(400, 500);
		j.setTitle("Hello World");
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		RectangleComponent component = new RectangleComponent();
		j.add(component);
		
		j.setVisible(true);
		
		

	}

}
