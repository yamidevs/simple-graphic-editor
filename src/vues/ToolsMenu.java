package vues;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import application.*;
//import png.*;
import eventManager.AutreEvent;

public class ToolsMenu extends JPanel implements ActionListener{
	
	private JButton cursor = new JButton(new ImageIcon("src/png/cursor.png"));
	private JButton circle = new JButton(new ImageIcon("src/png/circle.png"));
	private JButton rectangle = new JButton(new ImageIcon("src/png/rectangle.png"));
	private JButton color;
	private JPanel container = new JPanel();
	
	public ToolsMenu ( Model model, Controller controler) {
		color = new JButton("     ");
		color.setBackground(Color.black);
		color.addActionListener((e) -> {
            Color nColor = JColorChooser.showDialog(new JColorChooser(), "Changement couleur",null);
    		color.setBackground(nColor);
            controler.setDefaultColor(nColor);
		});
		circle.addActionListener(controler.getButtonEvent());
		circle.setActionCommand("ovale");
		rectangle.addActionListener(controler.getButtonEvent());
		rectangle.setActionCommand("rectangle");
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
	    container.add(cursor);
		container.add(circle);
		container.add(rectangle);
		container.add(color);

	}

	public void setCursor(JButton cursor) {
		this.cursor = cursor;
	}

	public JButton getCircle() {
		return circle;
	}

	public void setCircle(JButton circle) {
		this.circle = circle;
	}

	public JButton getRectangle() {
		return rectangle;
	}

	public void setRectangle(JButton rectangle) {
		this.rectangle = rectangle;
	}
	
	public JPanel getContainer() {
		return container;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getActionCommand().equals("rectangle"))
			rectangle.setSelected(true);
	}
}
