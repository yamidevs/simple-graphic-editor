package core;

import java.awt.Color;
import java.awt.Graphics;

public class Ovale extends Form{

	public Ovale(int id ,int x, int y, int height, int width, Color color) {
		super(id , x, y, height, width, color);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
        super.DrawSelectionOval(g);
        g.setColor(this.getColor());
		g.fillOval(this.getX(),this.getY(), this.getWidth(),this.getHeight());	

	}
}
