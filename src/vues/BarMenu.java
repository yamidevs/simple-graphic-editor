package vues;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import application.Controller;
import application.Model;

public class BarMenu extends JMenuBar {
	
	public BarMenu(Model model , Controller controller) {
		JMenu menu = new JMenu("Fichier");
		JMenuItem menuItemLoad = new JMenuItem("Ouvrir");
		menuItemLoad.setActionCommand("open");
		JMenuItem menuItemSave = new JMenuItem("Sauvgarder");
		menuItemSave.setActionCommand("save");
		menuItemLoad.addActionListener(controller.getMenuBarListener());
		menuItemSave.addActionListener(controller.getMenuBarListener());
		menu.add(menuItemLoad);
		menu.add(menuItemSave);
		this.add(menu);

	}
}
