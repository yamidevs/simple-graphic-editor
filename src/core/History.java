package core;

import java.util.ArrayList;

public class History {
	private ArrayList<Paper> trace;
	private int tracePosition;

	public History(Boolean state) {
		trace= new ArrayList<Paper>();
		if(!state) {
			trace.add(new Paper(new ArrayList<Form>(),"Vide"));
		}
		
		this.tracePosition = 0;
	}
	
	public ArrayList<Paper> getTrace() {
		return this.trace;
	}
	
	public void setTrace(ArrayList<Paper> papers) {
		this.trace = papers;
	}
	
	public void add(Paper paper) {
		this.trace.add(paper);
	}
	
	public void setTracePosition(int pos) {
		this.tracePosition = pos;
	}
	
	public int getTracePosition() {
		return this.tracePosition;
	}
}
