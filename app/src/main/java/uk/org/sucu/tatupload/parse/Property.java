package uk.org.sucu.tatupload.parse;

import java.util.ArrayList;

public class Property {

	private String primary;
	private ArrayList<String> alternatives;

	//Constructors
	public Property(String p){
		primary = p;
		alternatives = new ArrayList<String>();
	}
	public Property(String p, ArrayList<String> alt){
		primary = p;
		alternatives = alt;
	}

	//Accessors
	public String getPrimary(){
		return primary;
	}
	public ArrayList<String> getAlternatives(){
		return alternatives;
	}

	public void changePrimary(String replacement){
		primary = replacement;
	}
	public void removeAlternate(String toRemove){
		alternatives.remove(toRemove);
	}
	public void addAlternate(String toAdd){
		if(!alternatives.contains(toAdd)){
			alternatives.add(toAdd);
		}
	}

	//passed string exactly matches the property (or an alternate)
	public boolean isMatchedBy(String toCheck){
		if (primary.equals(toCheck)){
			return true;
		}
		for(String s : alternatives){
			if(s.equals(toCheck)){
				return true;
			}
		}
		return false;
	}

	//passed string contains the property (or an alternate)
	public boolean isContainedBy(String container){
		if(container.contains(primary)){
			return true;
		}
		for(String s: alternatives){
			if(container.contains(s)){
				return true;
			}
		}
		return false;
	}

}
