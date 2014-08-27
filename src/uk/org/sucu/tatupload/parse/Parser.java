package uk.org.sucu.tatupload.parse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import uk.org.sucu.tatupload.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;

@SuppressLint("DefaultLocale")
public class Parser {

	public static ArrayList<String> getQuestion(String message){
		message = message.toLowerCase();
		ArrayList<String> questions = new ArrayList<String>();
		String[] sentences = splitSentences(message);
		//for each sentence in the message
		for(String sentence : sentences){
			for(String qID : Parameters.questionParameter){
				//if it contains a question identifier, add it to the list of questions
				if (sentence.contains(qID)){
					questions.add(sentence);
					break;
				}
			}
		}
		
		return questions;
	}

	public static ArrayList<String> getLocation(String message){
		message = message.toLowerCase();
		ArrayList<String> locations = new ArrayList<String>();
		String[] sentences = splitSentences(message);
		//for each sentence in the message
		for(String sentence : sentences){
			//if it contains a location identifier, add it to the list of locations
			for(String l : Parameters.locationParameter){
				if(sentence.contains(l)){
					locations.add(sentence);
					break;
				}
			}
		}

		return locations;
	}

	public static ArrayList<String> getFlavours(String message){
		ArrayList<String> flavours = new ArrayList<String>();
		ArrayList<String> parameter = Parameters.flavourParameter;
		String[] sentences = splitSentences(message.toLowerCase());
		//for each sentence in the message
		for(String sentence : sentences){
			String[] words = splitWords(sentence);
			//for each word in the sentence
			word_loop:for(int i = 0; i < words.length; i++){
				//for each flavour in the parameter
				for(int j = 0; j < parameter.size(); j++){
					String flavour = parameter.get(j);
					String[] flavour_words = splitWords(flavour);
					boolean added = false;
					//for each word in that flavour
					for(int k = 0; k < flavour_words.length; k++){
						//if the word and flavour word match
						if(words[i].equals(flavour_words[k])){
							//add the flavour to the list if it hasn't already been (from this match)
							if(!added){
								flavours.add(flavour);
								added = true;
							}
							//check the next word
							i++;
							//if there is no next word, finish the sentence
							if(i >= words.length){
								break word_loop;
							}
						}else if(added){//if this word doesn't match, but previous ones did match this flavour
							//restart flavour check on this word;
							j = -1;//(will be incremented to 0 by j control loop)
							break;
						}//if no match, and the flavour not already added, it will check the next word in the flavour against the sentence word
					}
				}
			}
		}
		return flavours;
	}
	
	private static boolean sentenceContainsWord(String sentence, String word){
		String[] words = splitWords(sentence.toLowerCase());
		for(String sentenceWord : words){
			if(sentenceWord.equals(word)){
				return true;
			}
		}
		return false;
	}

	public static String concatenateArrayList(ArrayList<String> strings, String divider){
		StringBuilder builder = new StringBuilder();
		Iterator<String> iterator = strings.iterator();

		while (iterator.hasNext()) {
			String line = iterator.next();
			builder.append(line);
			if (iterator.hasNext()) {
				builder.append(divider);
			}
		}

		return builder.toString();
	}

	public static String timeStampToString(long timeStampMillis){
		Date d = new Date(timeStampMillis);
		return (DateFormat.getDateTimeInstance().format(d));
	}
	
	public static String getCurrentDateString(){
		Date d = new Date(Calendar.getInstance().getTimeInMillis());
		String date = DateFormat.getDateInstance().format(d);
		return date;
	}

	public static Uri createNewSpreadsheetUri(String sheetName, Context context){
		//TODO should not accept empty string
		try{
			sheetName = URLEncoder.encode(sheetName, "utf-8");
		} catch (UnsupportedEncodingException e){

		}

		StringBuilder builder = new StringBuilder();
		builder.append(context.getString(R.string.scriptURL));
		builder.append("?action=create&");
		builder.append("sheetName=");
		builder.append(sheetName);

		String uri = builder.toString();

		return Uri.parse(uri);
	}

	public static Uri createUploadUri(String number, String question, String location, String toastie, String sms, String time, Context context){

		StringBuilder builder = new StringBuilder();

		try{
			number = URLEncoder.encode(number, "utf-8");
			question = URLEncoder.encode(question, "utf-8");
			location = URLEncoder.encode(location, "utf-8");
			toastie = URLEncoder.encode(toastie, "utf-8");
			sms = URLEncoder.encode(sms, "utf-8");
			time = URLEncoder.encode(time, "utf-8");
		} catch (UnsupportedEncodingException e){

		}

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("action", "upload");
		params.put("number", number);
		params.put("question", question);
		params.put("location", location);
		params.put("toastie", toastie);
		params.put("SMS", sms);
		params.put("time", time);

		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
		builder.append(context.getString(R.string.scriptURL)).append("?");

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

	public static String[] splitSentences(String message){
		return message.split("(?<=[?!.[\\n][\\r]])");		
	}

	public static String[] splitWords(String sentence){
		return sentence.split("[[ ]*|[,]*|[\\.]*|[:]*|[;]*|[/]*|[!]*|[?]*|[+]*|[\\n]*|[\\r]*]+");
	}
	
}
