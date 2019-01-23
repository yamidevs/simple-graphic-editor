package core;

import java.util.ArrayList;

public class Paper{
	private ArrayList<Form> forms;
	private String description;
	public Paper( ArrayList<Form> forms , String description) {
		this.forms = forms;
		this.description = description;

	}
	public Paper(String description) {
		this.forms = new ArrayList<Form>();
		this.description = description;
	}
	public ArrayList<Form> getForms(){
		return this.forms;
	}
	public void add(Form form) {
		this.forms.add(form);
	}
	
	public String getInformation() {
		return this.description;
	}
	@Override
	public String toString() {
		return this.description;
	}
	
}