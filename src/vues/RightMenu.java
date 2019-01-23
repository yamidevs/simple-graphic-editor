package vues;

import java.awt.Cursor;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import application.Controller;
import application.Model;
import core.Form;
import core.History;
import core.PriorityList;
import eventManager.AutreEvent;
import eventManager.AutreEventListener;

public class RightMenu extends JPanel implements AutreEventListener{

	private JList listItems = null;
	private JList historiques = null;
	private JPanel container = new JPanel();
	private Controller controller;
	private Model model;
	private ArrayList<Form> elements;
	private History history;

	public RightMenu(Model model, Controller controller) {
		this.listItems = new JList();
		this.listItems.addListSelectionListener(controller.getJListForm());
		Box boxJlist = Box.createVerticalBox();
		boxJlist.add(new JLabel("Calques : "));
		boxJlist.add(new JScrollPane(this.listItems));
		Box boxButton = Box.createHorizontalBox();
		JButton up = new JButton("Monter");
		up.addActionListener(controller.getButtonEvent());
		up.setActionCommand("up");
		boxButton.add(up);
		JButton down = new JButton("Descendre");
		down.addActionListener(controller.getButtonEvent());
		down.setActionCommand("down");
		boxButton.add(down);
		JButton delete = new JButton("Supprimer");
		delete.addActionListener(controller.getButtonEvent());
		delete.setActionCommand("delete");
		boxButton.add(delete);
		boxJlist.add(boxButton);
		boxJlist.add(new JLabel("Historiques : "));
		this.historiques = new JList();
		this.historiques.addMouseListener(controller.getJListTrace());
		boxJlist.add(new JScrollPane(this.historiques));

		this.container.add(boxJlist);

		model.subscribe(this);
	}

	public JPanel getContainer() {
		return container;
	}

	public void setContainer(JPanel container) {
		this.container = container;
	}

	@Override
	public void Invoke(AutreEvent event) {
		// TODO Auto-generated method stub

		if(event.getDonnee() instanceof ArrayList)
		{
			DefaultListModel<String> model = new DefaultListModel<>();
			elements = (ArrayList<Form>)event.getDonnee();
			int indexSelected = -1;
			for(int i = 0;i<elements.size();i++)
			{
				if(elements.get(i).getSelected())
					indexSelected = i;
				model.addElement(elements.get(i).toString());
			}
			this.listItems.setModel(model);

			if(indexSelected != -1)
			{
				this.listItems.setSelectedIndex(indexSelected);
			}
		}else if(event.getDonnee() instanceof  History)
		{
			DefaultListModel<String> model = new DefaultListModel<>();
			history = (History)event.getDonnee();
			int index = -1;

			for(int i = 0;i<history.getTrace().size();i++)
			{
				if(i == history.getTracePosition()) {
					index = i;
				}
				model.addElement(history.getTrace().get(i).toString());
			}
			this.historiques.setModel(model);
			if(index != -1  && this.historiques.getSelectedIndex() != index) {
				this.historiques.setSelectedIndex(index);
			}

		}		
	}
}
