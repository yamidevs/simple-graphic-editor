package core;

import java.util.ArrayList;

public class PriorityList<T extends Comparable<T>> extends ArrayList<T>{
	

	@Override
	public boolean add(T element)
	{		
		boolean value =  super.add(element);
		this.sort();
		return value;
	}
	
	public boolean remove(T element)
	{
		boolean value =  this.remove(element);
		this.sort();
		return value;
	}
	
	

	public void sort()
	{
		this.sort((u,v)->{
			return u.compareTo(v);
		});
	}
	
}
