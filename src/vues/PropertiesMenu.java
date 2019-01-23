package vues;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.*;

import application.Controller;
import application.Model;
import core.Form;
import core.FormDescription;
import eventManager.AutreEvent;
import eventManager.AutreEventListener;
import eventManager.AutreEventNotifieur;

public class PropertiesMenu extends JPanel implements AutreEventListener {
	private class KeyAction extends AbstractAction {
	      public KeyAction(String actionCommand) {
	         putValue(ACTION_COMMAND_KEY, actionCommand);
	      }

	      @Override
	      public void actionPerformed(ActionEvent e) {
	    	  eventManager.publish(new AutreEvent(this,e.getActionCommand()));
	      }
	   }
	private JLabel xTXT = new JLabel("X");
	private JTextField x = new JTextField();
	private JLabel yTXT = new JLabel("Y");
	private JTextField y = new JTextField();
	private JLabel hTXT = new JLabel("Hauteur");
	private JTextField h = new JTextField();
	private JLabel lTXT = new JLabel("Largeur");
	private JTextField l = new JTextField();
	private GridLayout listBtn = new GridLayout();
	private JPanel container = new JPanel();
	private JButton color;
	private Model model;
	private Controller controller;
	private ArrayList<Form> forms;
	private AutreEventNotifieur eventManager;
	private int selectedId;

	public PropertiesMenu(Model model, Controller controller, AutreEventNotifieur eventManager) {
		this.eventManager = eventManager;
		model.subscribe(this);
		this.controller = controller;
		this.model = model;
		Box box = Box.createHorizontalBox();
		x.setPreferredSize(new Dimension(60, 20));
		y.setPreferredSize(new Dimension(60, 20));
		h.setPreferredSize(new Dimension(60, 20));
		l.setPreferredSize(new Dimension(60, 20));
		box.add(xTXT);
		box.add(x);
		box.add(yTXT);
		box.add(y);
		box.add(hTXT);
		box.add(h);
		box.add(lTXT);
		box.add(l);
		this.container.add(box);
		color = new JButton();
		color.setPreferredSize(new Dimension(25, 20));
		color.setBackground(Color.BLACK);
		color.addActionListener((e) -> {
			Color nColor = JColorChooser.showDialog(new JColorChooser(), "Changement couleur", null);
			eventManager.publish(new AutreEvent(this, nColor));
		});
		this.container.add(color);
		JButton button = new JButton("Modifier");
		button.addActionListener((e) -> {
			
			eventManager.publish(new AutreEvent(this,new FormDescription(Integer.parseInt(x.getText()),Integer.parseInt(y.getText()),Integer.parseInt(l.getText()),Integer.parseInt(h.getText()))));
		});
		this.container.add(button);
		this.selectedId = -1;

	}

	public JPanel getContainer() {
		return this.container;
	}

	@Override
	public void Invoke(AutreEvent event) {
		if (event.getDonnee() instanceof ArrayList) {
			forms = (ArrayList<Form>) event.getDonnee();

			for (int i = 0; i < forms.size(); i++) {
				if (forms.get(i).getSelected() == true && forms.get(i).getId() != this.selectedId) {
					Form form = forms.get(i);
					x.setText("" + form.getX());
					y.setText("" + form.getY());
					h.setText("" + form.getHeight());
					l.setText("" + form.getWidth());
					color.setBackground(form.getColor());
					this.selectedId = forms.get(i).getId();
					break;
				}
			}
		}
	}
}
