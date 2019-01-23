package core;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileManager {
	
	private FileWriter file;
	private PrintWriter  print;
	private FileReader  reader;
	private String path;
	public FileManager(String path) {
		
		this.path = path;
		this.file = null;
		this.print = null;
		this.reader = null;

	}
	
	public void open(Boolean write) throws IOException {
		if(write) {
			this.file = new FileWriter(path);
			this.print = new PrintWriter(file);
		}else{
			this.reader = new FileReader(path);		
		}
		
	}
	
	public PrintWriter getPrintWriter() {
		return this.print;
	}
	
	public FileReader getFileReader() {
		return this.reader;
	}
	
	public void close() {
		if(print != null) {
			print.close();
		}
	}
	
}
