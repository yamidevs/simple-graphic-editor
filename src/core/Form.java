package core;

import java.awt.Color;
import java.awt.*;
import java.util.Comparator;

public abstract class Form implements Cloneable {
	private int x, y, height, width, id;
	private int sizeSelection;
	private int priority;
	private boolean selected; // cet attribue utilisé pour savoir si la forme et sélectionné ou pas
	private Color color;

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Form(int id, int x, int y, int height, int width, Color color) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		this.selected = false;
		this.color = color;
		this.sizeSelection = 8;// nous allons utiliser cet attribue pour augmenter la hauteur et la largeur de la selection d'un objet
	}

	public boolean getSelected() {
		return this.selected;
	}

	public int getId() {
		return this.id;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public int getSizeSelection() {
		return this.sizeSelection;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void setSizeSelection(int sizeSelection) {
		this.sizeSelection = sizeSelection;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public abstract void draw(Graphics g);

	public void DrawSelectionRect(Graphics g) {// tracer la sélection d'un rectangle
		if (this.getSelected()) {
			g.setColor(Color.RED);
			g.fillRect(this.getX() - (this.getSizeSelection() / 2), this.getY() - (this.getSizeSelection() / 2),
					this.getWidth() + (this.getSizeSelection()), this.getHeight() + this.getSizeSelection());

		}
	}

	public void DrawSelectionOval(Graphics g) {// tracer la selection d'une elipse
		if (this.getSelected()) {
			g.setColor(Color.RED);
			g.fillOval(this.getX() - (this.getSizeSelection() / 2), this.getY() - (this.getSizeSelection() / 2),
					this.getWidth() + (this.getSizeSelection()), this.getHeight() + this.getSizeSelection());
		}
	}

	public void update(Form form) {// mettre à jour une form à partit d'une autre forme
		this.x = form.x;
		this.y = form.y;
		this.width = form.width;
		this.height = form.height;
	}

	public void update(int x, int y, int width, int height) {// mettre à jour une form à partir des attribue
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/*public int compareTo(Form arg) {
		if (this.priority > arg.priority)
			return 1;
		else if (this.priority < arg.priority)
			return -1;
		else
			return 0;
	}*/

	public boolean isCollision(Point p) {//on va utiliser cet methode pour savoir si ya une collision entre deux objet 
		java.awt.Rectangle rectangle = new java.awt.Rectangle(this.x, this.y, this.width, this.height);
		if (rectangle.intersects(new java.awt.Rectangle(p.getX(), p.getY(), 1, 1)))
			return true;
		return false;
	}

	public int isResizing(Point p) {
		java.awt.Rectangle top = new java.awt.Rectangle(this.getX() - (this.getSizeSelection() / 2),
				this.getY() - (this.getSizeSelection() / 2), this.getWidth() + (this.getSizeSelection()),
				this.getSizeSelection());
		if (top.intersects(new java.awt.Rectangle(p.getX(), p.getY(), 1, 1))) {
			return Cursor.N_RESIZE_CURSOR;
		} else {
			java.awt.Rectangle bot = new java.awt.Rectangle(this.getX() - (this.getSizeSelection() / 2),
					this.getY() + this.height, this.getWidth() + (this.getSizeSelection()), this.getSizeSelection());
			if (bot.intersects(new java.awt.Rectangle(p.getX(), p.getY(), 1, 1))) {
				return Cursor.S_RESIZE_CURSOR;
			} else {
				java.awt.Rectangle right = new java.awt.Rectangle(this.getX() + this.width,
						this.getY() - (this.getSizeSelection() / 2), (this.getSizeSelection()),
						this.getHeight() + this.getSizeSelection());
				if (right.intersects(new java.awt.Rectangle(p.getX(), p.getY(), 1, 1))) {
					return Cursor.E_RESIZE_CURSOR;
				} else {
					java.awt.Rectangle left = new java.awt.Rectangle(this.getX() - (this.getSizeSelection()),
							this.getY() - (this.getSizeSelection() / 2), (this.getSizeSelection()),
							this.getHeight() + this.getSizeSelection());
					if (left.intersects(new java.awt.Rectangle(p.getX(), p.getY(), 1, 1))) {
						return Cursor.W_RESIZE_CURSOR;
					}
				}
			}
		}
		return Cursor.DEFAULT_CURSOR;
	}

	@Override
	public boolean equals(Object arg) {
		return this.id == (int) arg;
	}

	@Override
	public String toString() {
		return "ID : " + this.id + " , type : " + this.getClass().getSimpleName();
	}

	public Form clone() throws CloneNotSupportedException {
		Form clonedObj = (Form) super.clone();
		return clonedObj;

	}
}
