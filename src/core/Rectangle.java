package core;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.border.LineBorder;

public class Rectangle extends Form{

	public Rectangle(int id ,int x, int y, int height, int width, Color color) {
		super(id , x, y, height, width, color);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
        super.DrawSelectionRect(g);
        g.setColor(this.getColor());
		g.fillRect(this.getX(),this.getY(), this.getWidth(),this.getHeight());	
	}
}