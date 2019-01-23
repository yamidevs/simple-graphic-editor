package application;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.text.Normalizer.Form;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import core.FormDescription;
import core.FormType;
import core.Point;
import eventManager.AutreEvent;
import eventManager.AutreEventListener;

public class Controller implements AutreEventListener {

	private Model model;
	private Point start, end;
	private int currentId;
	private int selectedId;
	private MouseAdapterController mouseAdapter;
	private MouseMotionController mouseMotion;
	private JListForm jListForm;
	private boolean isDraging;
	private int isResize;
	private ButtonEvent buttonEvent;
	private int copyId;
	private Color defaultColor;
	private JListTrace jlistTrace;
	private MenuBarListener menuBarListener;

	public Controller(Model model) {
		this.model = model;
		this.mouseAdapter = new MouseAdapterController();
		this.mouseMotion = new MouseMotionController();
		this.buttonEvent = new ButtonEvent();
		this.jListForm = new JListForm();
		this.menuBarListener = new MenuBarListener();
		this.currentId = -1;
		this.selectedId = -1;
		this.isDraging = false;
		this.isResize = 0;
		this.copyId = -1;
		this.defaultColor = Color.BLACK;
		this.jlistTrace = new JListTrace();
	}

	public MenuBarListener getMenuBarListener() {
		return menuBarListener;
	}

	public JListTrace getJListTrace() {
		return jlistTrace;
	}

	public JListForm getJListForm() {
		return jListForm;
	}

	public ButtonEvent getButtonEvent() {
		return buttonEvent;
	}

	public void setButtonEvent(ButtonEvent buttonEvent) {
		this.buttonEvent = buttonEvent;
	}

	public MouseAdapterController getMouseAdapter() {
		return this.mouseAdapter;
	}

	public void setDefaultColor(Color color) {
		this.defaultColor = color;
	}

	public MouseMotionController getMouseMotion() {
		return this.mouseMotion;
	}

	@Override
	public void Invoke(AutreEvent event) {
		if (event.getDonnee() instanceof FormType) {
			this.model.setCurrentForm((FormType) event.getDonnee());
		} else if (event.getDonnee() instanceof String) {
			String actionCommand = (String) event.getDonnee();
			if (actionCommand == "copy") {
				if (selectedId != -1) {
					copyId = selectedId;
				}
			} else if (actionCommand == "past") {
				core.Form element = model.get(copyId);
				if (copyId != -1) {
					if (element != null) {
						try {
							model.setMaxId(model.getMaxId() + 1);
							model.add(model.getMaxId(), (core.Form) element.clone());
						} catch (CloneNotSupportedException e) {
							e.printStackTrace();
						}
					}
				}
			} else if (actionCommand == "undo") {
				model.undo();
				start = null;
				end = null;
				currentId = -1;
				selectedId = -1;
				isDraging = false;
			} else if (actionCommand == "redo") {
				model.redo();
				start = null;
				end = null;
				currentId = -1;
				selectedId = -1;
				isDraging = false;
			}
		} else if (event.getDonnee() instanceof Color) {
			if (selectedId != -1) {
				core.Form form = model.get(selectedId);
				if (form != null) {
					model.changeColor(form, (Color) event.getDonnee());
					model.updateTrace("Changement de couleur l'objet " + selectedId);

				}
			}
		} else if(event.getDonnee() instanceof FormDescription) {
			if(selectedId != -1) {
				model.update(selectedId, (FormDescription)event.getDonnee());
				model.updateTrace("Mise à jour de l'objet " + selectedId);

			}
		}
	}

	class MouseMotionController extends MouseMotionAdapter {

		public void mouseDragged(MouseEvent e) {
			// on vérifie si il a rien selectionner alors on crée form
			if (start != null && end != null && selectedId == -1) {
				end = new Point(e.getX(), e.getY());
				model.update(currentId, start, end);
			} else {
				// si selectionne dans le vide on deplace pas l'object courant
				// deplacement
				int currentSelectId = model.selectedForm(new Point(e.getX(), e.getY()), false, false);
				if (currentSelectId != -1 && (currentSelectId == selectedId || isDraging) && isResize == 0) {

					model.update(selectedId, new Point(e.getX() - start.getX(), e.getY() - start.getY()));
					start = new Point(e.getX(), e.getY());
				} else {
					// resize
					if (isResize != 0 && selectedId != -1) {
						if (isResize == Cursor.E_RESIZE_CURSOR) {
							model.update(selectedId, e.getX() - start.getX(), 0);
							if (model.get(selectedId).getWidth() <= 0) {
								isResize = Cursor.W_RESIZE_CURSOR;
							}
						} else if (isResize == Cursor.W_RESIZE_CURSOR) {
							model.update(selectedId, new Point(e.getX(), model.get(selectedId).getY()),
									start.getX() - e.getX(), 0);
							if (model.get(selectedId).getWidth() <= 0) {
								isResize = Cursor.E_RESIZE_CURSOR;
							}
						} else if (isResize == Cursor.S_RESIZE_CURSOR) {
							model.update(selectedId, 0, e.getY() - start.getY());
							if (model.get(selectedId).getHeight() <= 0) {
								isResize = Cursor.N_RESIZE_CURSOR;
							}
						} else {
							model.update(selectedId, new Point(model.get(selectedId).getX(), e.getY()), 0,
									start.getY() - e.getY());
							if (model.get(selectedId).getHeight() <= 0) {
								isResize = Cursor.S_RESIZE_CURSOR;
							}
						}

						start.setX(e.getX());
						start.setY(e.getY());

					} else {

						selectedId = model.selectedForm(new Point(e.getX(), e.getY()));
						isResize = 0;
					}
				}

			}
		}

		public void mouseMoved(MouseEvent e) {
			if (model.selectedForm(new Point(e.getX(), e.getY()), false, false) != -1) {
				model.changeCursor(Cursor.HAND_CURSOR);
				isResize = 0;
				// check if resize object
			} else if (selectedId != -1) {
				// return 0 for default cursor , 8 north ...
				isResize = model.get(selectedId).isResizing(new Point(e.getX(), e.getY()));
				model.changeCursor(isResize);

			} else {
				model.changeCursor(Cursor.DEFAULT_CURSOR);
				isResize = 0;
			}

		}
	}

	class MouseAdapterController extends MouseAdapter {

		public void mousePressed(MouseEvent e) {
			// si y'as rien de selectionner et la souris est dans le vide alors on crée une
			// forme
			if (selectedId == -1 && (selectedId = model.selectedForm(new Point(e.getX(), e.getY()), true)) == -1) {

				start = new Point(e.getX(), e.getY());
				end = start;
				model.setMaxId(model.getMaxId() + 1);

				if (model.add(model.getMaxId(), start, end, defaultColor)) {
					selectedId = model.getMaxId();
					currentId = model.getMaxId();
				}
			} else {
				// sinon on donne la premier position du click pour que on puisse déplacer la
				// forme
				if (isResize == 0) {
					selectedId = model.selectedForm(new Point(e.getX(), e.getY()));
				}

				start = new Point(e.getX(), e.getY());
				isDraging = true;
			}
		}

		public void mouseReleased(MouseEvent e) {

			if (start != null && end != null) {
				end = new Point(e.getX(), e.getY());
				model.update(currentId, start, end, true);
				start = null;
				end = null;
				currentId = -1;
				selectedId = -1;
				isDraging = false;
				model.updateTrace("Création d'un " + model.get(model.getMaxId()).getClass().getSimpleName());

			} else if (isDraging == true && selectedId != -1) // pour l'historique
			{
				model.updateTrace("Déplacement de l'objet " + selectedId);
			}
		}

		public void mouseClicked(MouseEvent e) {

			if (start == null || end == null) {
				if (isResize == 0)
					selectedId = model.selectedForm(new Point(e.getX(), e.getY()));
			}

		}

	}

	class ButtonEvent implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if (arg0.getActionCommand() == "rectangle") {
				model.setCurrentForm(FormType.rectangle);

			} else if (arg0.getActionCommand() == "ovale") {
				model.setCurrentForm(FormType.ovale);

			} else if (arg0.getActionCommand() == "up") {
				if (selectedId != -1) {
					int index = model.getIndexById(selectedId);
					if (index != -1 && index > 0) {
						model.swap(index, index - 1);
					}
				}
			} else if (arg0.getActionCommand() == "down") {
				if (selectedId != -1) {
					int index = model.getIndexById(selectedId);
					if (index != -1 && index < model.maxIndex() - 1) {
						model.swap(index, index + 1);
					}
				}
			} else if (arg0.getActionCommand() == "delete") {
				if (selectedId != -1) {
					int index = model.getIndexById(selectedId);
					if (index != -1) {
						model.updateTrace("Suppression de l'objet  " + selectedId);

						selectedId = -1;
						currentId = -1;
						start = null;
						end = null;
						isDraging = false;
						model.remove(index);

					}
				}
			}
		}
	}

	class JListForm implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			JList list = (JList) e.getSource();
			int selection = list.getSelectedIndex();
			if (selection != -1) {
				int id = model.getIdByIndex(selection);
				if (id != selectedId) {
					selectedId = id;
					model.selectedForm(selection);
				}

			}

		}
	}

	class JListTrace extends MouseAdapter {

		public void mouseClicked(MouseEvent mouseEvent) {

			JList<String> theList = (JList) mouseEvent.getSource();
			if (mouseEvent.getClickCount() == 1) {
				int index = theList.locationToIndex(mouseEvent.getPoint());
				if (index >= 0) {
					model.changeTracePosition(index);
				}
			}
		}

	}

	class MenuBarListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand() == "save") {
				JFileChooser file = new JFileChooser();
				file.showSaveDialog(null);
				model.saveFile(file.getSelectedFile().getAbsolutePath());
			} else if (e.getActionCommand() == "open") {
				JFileChooser file = new JFileChooser();
				file.showOpenDialog(null);
				model.readFile(file.getSelectedFile().getAbsolutePath());
			}
		}

	}

}
