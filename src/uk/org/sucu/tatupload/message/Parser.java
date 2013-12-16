package uk.org.sucu.tatupload.message;

import java.util.ArrayList;
import java.util.Arrays;

public class Parser {
	

	ArrayList<String> flavourProperty = new ArrayList<String>(Arrays.asList(new String[]{"ham","cheese","tomato","pineapple","allery","allergic"}));
	ArrayList<String> locationProperty = new ArrayList<String>(Arrays.asList(new String[] {"monte","glen","connaught","bencraft","block","stags","road"}));
	ArrayList<String> questionProperty = new ArrayList<String>(Arrays.asList(new String[] {"who","what","where","when","why","how","could","would","is","?"}));
	
	public static void parseMessage(String message){
		
		String s = System.console().readLine();
		String[] sentences = s.split(".");
		for(String sen : sentences){
			System.out.println(sen);
		}
	}
	
	public ArrayList<String> getQuestion(String message){
		ArrayList<String> question = new ArrayList<String>();
		String[] sentences = message.split("(?<=[?.!])");
		for(String sentence : sentences){
			
			if (sentence.endsWith("?")){
				question.add(sentence);
			}
			
		}
		return question;
	}
	
	public ArrayList<String> getLocation(String message){
		ArrayList<String> location = new ArrayList<String>();
		String[] sentences = message.split("(?=[?.!])");
		for(String l : locationProperty){
			for(String sentence : sentences){
				if(sentence.contains(l)){
					location.add(sentence);
				}
			}
		}
		
		return location;
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
