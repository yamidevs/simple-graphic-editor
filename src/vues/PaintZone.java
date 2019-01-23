package vues;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.*;

import application.Controller;
import application.Model;
import core.Form;
import core.History;
import eventManager.AutreEvent;
import eventManager.AutreEventListener;
import eventManager.AutreEventNotifieur;

public class PaintZone extends JPanel implements AutreEventListener{
	private class KeyAction extends AbstractAction {
	      public KeyAction(String actionCommand) {
	         putValue(ACTION_COMMAND_KEY, actionCommand);
	      }

	      @Override
	      public void actionPerformed(ActionEvent e) {
	    	  eventManager.publish(new AutreEvent(this,e.getActionCommand()));
	      }
	   }
	
	private Model model;
	private Controller controller;
	private ArrayList<Form> elements;
	private AutreEventNotifieur eventManager;
	public PaintZone(Model model , Controller controller , AutreEventNotifieur eventManager)
	{
		this.eventManager = eventManager;
		this.model = model;
		this.model.subscribe(this);
		this.addMouseListener(controller.getMouseAdapter());
		this.addMouseMotionListener(controller.getMouseMotion());
		this.setBackground(Color.white);
		this.elements = new ArrayList<Form>();
	    setKeyBindings();
	}

	
	private void setKeyBindings() {
	      ActionMap actionMap = getActionMap();
	      int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
	      InputMap inputMap = getInputMap(condition);
	      inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK), "copy");
	      inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK), "past");
	      inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK), "undo");
	      inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK), "redo");
	      inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), "enter");

	      actionMap.put("copy", new KeyAction("copy"));
	      actionMap.put("past", new KeyAction("past"));
	      actionMap.put("undo", new KeyAction("undo"));
	      actionMap.put("redo", new KeyAction("redo"));

	   }

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		for(int i = 0;i < elements.size();i++)
		{
			elements.get(i).draw(g);
		}
	}
	
	@Override
	public void Invoke(AutreEvent event) {
		if(event.getDonnee() instanceof ArrayList)
		{
			elements = (ArrayList<Form>)event.getDonnee();
			this.repaint();
		}else if(event.getDonnee() instanceof  Cursor)
		{
			this.setCursor((Cursor)event.getDonnee());		
		}
	}
}
