package application;

import java.awt.EventQueue;

public class GUI {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Model model = new Model();
					Controller controller = new Controller(model);
					Vue window = new Vue(model,controller);
					//window.pack();
					//window.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
