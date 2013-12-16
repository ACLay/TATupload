package uk.org.sucu.tatupload.message;

import java.util.ArrayList;

public class Property {
	
	private String name = null;
	private ArrayList<String> options = null;
	
	public Property(String name, ArrayList<String> options){
		this.name = name;
		this.options = options;
	}
	
	public void addOption(String option){
		options.add(option);
	}
	
	public String getName(){
		return name;
	}
	
	public ArrayList<String> getOptions(){
		return options;
	}
	
}
