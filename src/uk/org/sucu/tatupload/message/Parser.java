package uk.org.sucu.tatupload.message;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import uk.org.sucu.tatupload.TatUploadApplication;
import android.net.Uri;

public class Parser {

	private static String[] flavourProperty = {"ham","cheese","tomato","pineapple"};
	private static String[] locationProperty = {"monte","glen","connaught","bencraft","highfield","archers","gateley","south hill",
		"library","stags","susu","bridge","hartley",
		"road"," rd","avenue", "gardens","street"," st","terrace",
		"hobbit","jesters","sobar",
		"block","flat","floor","room"};
	private static String[] questionProperty = {"who","what","where","when","why","how","could","would","?"};


	public static ArrayList<String> getQuestion(String message){
		message = message.toLowerCase();
		ArrayList<String> question = new ArrayList<String>();
		String[] sentences = splitSentences(message);
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

	public static ArrayList<String> getLocation(String message){
		message = message.toLowerCase();
		ArrayList<String> location = new ArrayList<String>();
		String[] sentences = splitSentences(message);
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

	public static ArrayList<String> getFlavours(String message){
		ArrayList<String> flavours = new ArrayList<String>();
		String[] messageWords = splitWords(message.toLowerCase());
		for(String word : messageWords){
			for(String flavour : flavourProperty){
				if(word.equals(flavour)){
					flavours.add(flavour);
				}
			}
		}
		return flavours;
	}

	public static String concatenateArrayList(ArrayList<String> strings){
		StringBuilder builder = new StringBuilder();
		Iterator<String> iterator = strings.iterator();

		while (iterator.hasNext()) {
			String line = iterator.next();
			builder.append(line);
			if (iterator.hasNext()) {
				builder.append(' ');
			}
		}

		return builder.toString();
	}

	public static String timeStampToString(long timeStampMillis){
		Date d = new Date(timeStampMillis);
		return (DateFormat.getDateTimeInstance().format(d));
	}

	public static Uri createNewFormUri(String formName){
		//TODO should not accept empty string
		try{
			formName = URLEncoder.encode(formName, "utf-8");
		} catch (UnsupportedEncodingException e){

		}

		StringBuilder builder = new StringBuilder();
		builder.append(TatUploadApplication.getScriptURL());
		builder.append("?action=create&");
		builder.append("formName=");
		builder.append(formName);

		String uri = builder.toString();

		return Uri.parse(uri);
	}

	public static Uri createUploadUri(String formName, String number, String question, String location, String toastie, String sms){

		StringBuilder builder = new StringBuilder();

		try{
			formName = URLEncoder.encode(formName, "utf-8");
			question = URLEncoder.encode(question, "utf-8");
			location = URLEncoder.encode(location, "utf-8");
			toastie = URLEncoder.encode(toastie, "utf-8");
			sms = URLEncoder.encode(sms, "utf-8");
		} catch (UnsupportedEncodingException e){

		}

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("action", "upload");
		params.put("formName", formName);
		params.put("number", number);
		params.put("question", question);
		params.put("location", location);
		params.put("toastie", toastie);
		params.put("SMS", sms);

		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
		builder.append(TatUploadApplication.getScriptURL()).append("?");

		while (iterator.hasNext()) {
			Entry<String, String> param = iterator.next();
			builder.append(param.getKey()).append('=').append(param.getValue());
			if (iterator.hasNext()) {
				builder.append('&');
			}
		}

		String uri = builder.toString();

		return Uri.parse(uri);
	}

	private static String[] splitSentences(String message){
		return message.split("(?<=[?!.])");		
	}

	public static String[] splitWords(String sentence){
		return sentence.split("[[ ]*|[,]*|[\\.]*|[:]*|[;]*|[/]*|[!]*|[?]*|[+]*]+");
	}


}
