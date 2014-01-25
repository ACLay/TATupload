package uk.org.sucu.tatupload.message;

import java.util.ArrayList;

public class Parser {
	

	String[] flavourProperty = {"ham","cheese","tomato","pineapple","nutella","bbq"};
	String[] locationProperty = {"monte","glen","connaught","bencraft","highfield","archers","gateley","south hill",
			"library","stags","susu","bridge","hartley",
			"road"," rd","avenue", "gardens","street"," st","terrace",
			"hobbit","jesters","sobar",
			"block","flat","floor","room"};
	String[] questionProperty = {"who","what","where","when","why","how","could","would","is","?"};
	
	
	
	public ArrayList<String> getQuestion(String message){
		ArrayList<String> question = new ArrayList<String>();
		String[] sentences = message.split("(?<=[?.])");
		for(String sentence : sentences){
			
			for(String s : questionProperty){
				if (sentence.contains(s)){;
					question.add(sentence);
				}
			}
			
		}
		
		ArrayList<String> uniques = new ArrayList<String>();
		for (String s : question){
			if (!uniques.contains(s)){
				uniques.add(s);
			}
		}
		
		
		return uniques;
	}
	
	public ArrayList<String> getLocation(String message){
		ArrayList<String> location = new ArrayList<String>();
		String[] sentences = message.split("(?<=[?.,])");
		for(String l : locationProperty){
			for(String sentence : sentences){
				if(sentence.contains(l)){
					location.add(sentence);
				}
			}
		}
		
		ArrayList<String> uniques = new ArrayList<String>();
		for (String s : location){
			if (!uniques.contains(s)){
				uniques.add(s);
			}
		}
		
		return uniques;
	}
	
	public ArrayList<String> getFlavours(String message){
		ArrayList<String> flavours = new ArrayList<String>();
		for(String s : flavourProperty){
			if(message.contains(s)){
				flavours.add(s);
			}
		}
		return flavours;
	}
	
	
}
