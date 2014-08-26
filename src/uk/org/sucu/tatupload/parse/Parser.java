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
		ArrayList<String> question = new ArrayList<String>();
		String[] sentences = splitSentences(message);
		for(String sentence : sentences){

			for(String s : Parameters.questionParameter){
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
		for(String l : Parameters.locationParameter){
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
			for(String flavour : Parameters.flavourParameter){
				if(word.equals(flavour)){
					flavours.add(flavour);
				}
			}
		}
		return flavours;
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
