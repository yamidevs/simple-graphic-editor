
package application;

import java.awt.Color;
import java.awt.Cursor;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import core.FileManager;
import core.Form;
import core.FormDescription;
import core.FormType;
import core.History;
import core.Ovale;
import core.Paper;
import core.Point;
import core.PriorityList;
import core.Rectangle;
import eventManager.AutreEvent;
import eventManager.AutreEventListener;
import eventManager.AutreEventNotifieur;

public class Model {

	private FormType currentType;
	// after priority list
	private ArrayList<Form> list;
	private AutreEventNotifieur eventManager = new AutreEventNotifieur();
	private History history;
	private int id;
	private FileManager file;
	private int maxId;

	public Model() {
		history = new History(false);
		list = new ArrayList<>();
		currentType = FormType.empty;
		this.id = 0;
		this.maxId = 0;
	}

	public int getMaxId() {
		return this.maxId;
	}

	public void setMaxId(int maxId) {
		this.maxId = maxId;
	}

	public void add(int id, Form form) {
		if (form instanceof Rectangle) {
			this.add(id, new Point(form.getX(), form.getY()),
					new Point(form.getX() + form.getHeight(), form.getY() + form.getWidth()), FormType.rectangle,
					form.getColor());
		} else if (form instanceof Ovale) {
			this.add(id, new Point(form.getX(), form.getY()),
					new Point(form.getX() + form.getHeight(), form.getY() + form.getWidth()), FormType.ovale,
					form.getColor());
		}
	}

	public boolean add(int id, Point start, Point end, Color color) {
		return this.add(id, start, end, currentType, color);
	}

	public boolean add(int id, Point start, Point end, FormType type, Color color) {
		Form form = null;
		if (type == FormType.rectangle) {

			form = new Rectangle(id, Math.min(start.getX(), end.getX()), Math.min(start.getY(), end.getY()),
					Math.abs(start.getX() - end.getX()), Math.abs(start.getY() - end.getY()), color);
		} else if (type == FormType.ovale) {
			form = new Ovale(id, Math.min(start.getX(), end.getX()), Math.min(start.getY(), end.getY()),
					Math.abs(start.getX() - end.getX()), Math.abs(start.getY() - end.getY()), color);
		}
		// ... ..

		if (form != null) {

			list.add(form);
			this.publish();
			this.id = id;

			return true;
		}
		return false;
	}

	public void remove(int index) {
		list.remove(index);
		this.publish();
	}

	public ArrayList getList() {
		return list;
	}

	public void update(int id, Point start, Point end) {
		this.update(id, start, end, false);
	}

	public void update(int id, Point start, Point end, boolean finish) {
		Form element = this.get(id);

		if (element != null) {

			element.update(Math.min(start.getX(), end.getX()), Math.min(start.getY(), end.getY()),
					Math.abs(start.getX() - end.getX()), Math.abs(start.getY() - end.getY()));

			this.publish();
		}
	}

	public void update(int id, Point position) {
		Form element = this.get(id);
		if (element != null) {
			element.update(element.getX() + position.getX(), element.getY() + position.getY(), element.getWidth(),
					element.getHeight());
			this.publish();
		}
	}
	public void update(int id , FormDescription form) {
		Form element = this.get(id);
		if (element != null) {
			element.update(form.getX(), form.getY(), form.getWidth(), form.getHeight());
			this.publish();
		}
	}
	public void update(int id, int width, int height) {
		Form element = this.get(id);
		if (element != null) {
			element.update(element.getX(), element.getY(), element.getWidth() + width, element.getHeight() + height);
			this.publish();
		}
	}

	public void update(int id, Point position, int width, int height) {
		Form element = this.get(id);
		if (element != null) {
			element.update(position.getX(), position.getY(), element.getWidth() + width, element.getHeight() + height);

			this.publish();
		}
	}

	public Form get(int id) {
		if (list.size() == 0)
			return null;

		Form element = null;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getId() == id) {
				element = list.get(i);
				break;
			}
		}
		return element;
	}

	public int selectedForm(Point p) {
		return selectedForm(p, false, true);
	}

	public int selectedForm(Point p, boolean self) {
		return selectedForm(p, self, true);
	}

	public int selectedForm(Point p, boolean self, boolean setting) {
		// order by priority
		int selectedForm = -1;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).isCollision(p)) {
				if (list.get(i).getSelected() && self) {
					if (setting)
						list.get(i).setSelected(false);
				} else {
					if (setting)
						list.get(i).setSelected(true);
					selectedForm = list.get(i).getId();
				}

			} else {
				if (setting)
					list.get(i).setSelected(false);
			}
		}

		this.publish();
		return selectedForm;
	}

	public void selectedForm(int id) {
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setSelected(false);
		}
		list.get(id).setSelected(true);
		this.publish();
	}

	public int getIdByIndex(int index) {
		return list.get(index).getId();
	}

	public int getIndexById(int id) {
		int index = -1;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getId() == id) {
				index = i;
				break;
			}

		}

		return index;
	}

	public int maxIndex() {
		return list.size();
	}

	public void swap(int fIndex, int sIndex) {
		Collections.swap(list, fIndex, sIndex);
		this.publish();
	}

	public void changeColor(Form form, Color color) {
		form.setColor(color);
		this.publish();
	}

	public void undo() {

		if (this.history.getTracePosition() > 0) {
			this.history.setTracePosition(this.history.getTracePosition() - 1);
			list = (ArrayList<Form>) this.history.getTrace().get(this.history.getTracePosition()).getForms().clone();

		}

		this.publish();
	}

	public void redo() {
		if (this.history.getTracePosition() + 1 < this.history.getTrace().size()) {
			this.history.setTracePosition(this.history.getTracePosition() + 1);
			list = (ArrayList<Form>) this.history.getTrace().get(this.history.getTracePosition()).getForms().clone();
		}

		this.publish();
	}

	public void updateTrace(String information) {
		History tmp = new History(true);

		for (int i = 0; i <= this.history.getTracePosition(); i++) {
			core.Paper paper = new core.Paper(history.getTrace().get(i).getInformation());
			for (int j = 0; j < history.getTrace().get(i).getForms().size(); j++) {
				try {
					paper.add(history.getTrace().get(i).getForms().get(j).clone());// .add();
				} catch (CloneNotSupportedException e) {
					System.out.println("can't clone the object");
				}
			}

			tmp.add(paper);
		}

		core.Paper paper = new core.Paper(information);
		for (int i = 0; i < list.size(); i++) {
			try {
				paper.add(list.get(i).clone());
			} catch (CloneNotSupportedException e) {
				System.out.println("can't clone the object");
			}
		}
		tmp.add(paper);
		tmp.setTracePosition(tmp.getTrace().size() - 1);
		this.history = tmp;

	}

	public void changeTracePosition(int pos) {
		this.history.setTracePosition(pos);
		list = (ArrayList<Form>) this.history.getTrace().get(this.history.getTracePosition()).getForms().clone();

		this.publish();
	}

	public void saveFile(String path) {
		file = new FileManager(path);

		try {
			file.open(true);
			for (int i = 0; i < list.size(); i++) {
				// type|x|y|height|width|Color
				Form form = list.get(i);
				file.getPrintWriter().printf("%s|%d|%d|%d|%d|%d\n", form.getClass().getSimpleName(), form.getX(),
						form.getY(), form.getHeight(), form.getWidth(), form.getColor().getRGB());
			}
			file.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void readFile(String path) {
		/*
		 * file = new FileManager(path); try { file.open(false); BufferedReader
		 * bufferreader = new BufferedReader(file.getFileReader()); String data;
		 * 
		 * while ((data = bufferreader.readLine()) != null) { data =
		 * bufferreader.readLine(); System.out.println(data); String[] split =
		 * data.split("\\|"); Point start = new Point(Integer.parseInt(split[1]),
		 * Integer.parseInt(split[2])); Point end = new Point(start.getX() +
		 * Integer.parseInt(split[3]), start.getY() + Integer.parseInt(split[4])); Color
		 * color = new Color(Integer.parseInt(split[5])); System.out.println(split[0]);
		 * FormType type; if (split[0].equalsIgnoreCase("Rectangle")) { type =
		 * FormType.rectangle; } else { type = FormType.ovale; }
		 * 
		 * 
		 * this.add(++maxId, start, end, type, color);
		 * 
		 * } file.close(); this.publish(); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
		FileReader fileReader;
		try {
			fileReader = new FileReader(path);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			List<String> lines = new ArrayList<String>();
			String data = null;

			while ((data = bufferedReader.readLine()) != null) {
				System.out.println(data);
				String[] split = data.split("\\|");
				Point start = new Point(Integer.parseInt(split[1]), Integer.parseInt(split[2]));
				Point end = new Point(start.getX() + Integer.parseInt(split[3]),
						start.getY() + Integer.parseInt(split[4]));
				Color color = new Color(Integer.parseInt(split[5]));
				System.out.println(split[0]);
				FormType type;
				if (split[0].equalsIgnoreCase("Rectangle")) {
					type = FormType.rectangle;
				} else {
					type = FormType.ovale;
				}

				this.add(++maxId, start, end, type, color);
			}

			bufferedReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {

		}

		this.publish();

	}

	public void remove(Form form) {
		list.remove(form);
		this.publish();
	}

	public void publish() {
		eventManager.publish(new AutreEvent(this, history));
		eventManager.publish(new AutreEvent(this, list));
	}

	public void changeCursor(int cursor) {
		eventManager.publish(new AutreEvent(this, new Cursor(cursor)));
	}

	public void setCurrentForm(FormType type) {
		this.currentType = type;
	}

	public void subscribe(AutreEventListener listener) {
		eventManager.addAutreEventListener(listener);
	}

	public void unsubscribe(AutreEventListener listener) {
		eventManager.removeAutreEventListener(listener);
	}

}
