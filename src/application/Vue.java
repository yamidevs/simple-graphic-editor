package application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.*;

import core.FormType;
import eventManager.AutreEvent;
import eventManager.AutreEventListener;
import eventManager.AutreEventNotifieur;
import vues.*;

public class Vue extends JFrame {	
	private PropertiesMenu propMenu;
	private JPanel Container = new JPanel();
	private Model model;
	private RightMenu rightMenu;
	private Controller controller;
	private PaintZone paint;

	private ToolsMenu sideMenu;
	private AutreEventNotifieur eventManager = new AutreEventNotifieur();
	//private PaintZone paintZone = ;

	public Vue(Model model , Controller controller) {
		this.model = model;
		this.controller = controller;
		this.sideMenu = new ToolsMenu(model, controller);
		this.rightMenu = new RightMenu(model,controller);
		this.propMenu =  new PropertiesMenu(model,controller,eventManager);
		this.paint = new PaintZone(model,controller,eventManager);
		this.setTitle("Mini Paint");
		this.setSize(500, 300);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.getContentPane().add(paint, BorderLayout.CENTER);
	    this.getContentPane().add(sideMenu.getContainer(), BorderLayout.WEST);
	    this.getContentPane().add(rightMenu.getContainer(), BorderLayout.EAST);
	    this.getContentPane().add(propMenu.getContainer(), BorderLayout.SOUTH);
	    this.setJMenuBar(new BarMenu(model,controller));
	    this.subscribe(controller);
		eventManager.publish(new AutreEvent(this,FormType.rectangle));
		this.pack();
	    this.setLocationByPlatform(true);
	    this.setVisible(true);
	    this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);

	}
	

	public void subscribe(AutreEventListener listener)
	{
		eventManager.addAutreEventListener(listener);
	}
	
	public void unsubscribe(AutreEventListener listener)
	{
		eventManager.removeAutreEventListener(listener);	
	}
}
