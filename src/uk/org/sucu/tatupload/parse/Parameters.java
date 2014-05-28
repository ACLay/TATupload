package uk.org.sucu.tatupload.parse;

import java.io.IOException;
import java.io.NotSerializableException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.pig.impl.util.ObjectSerializer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Parameters {

	public static final String FLAVOUR_PARAMETER = "uk.org.sucu.tatupload.FLAVOUR_PARAMETER";
	public static final String LOCATION_PARAMETER = "uk.org.sucu.tatupload.LOCATION_PARAMETER";
	public static final String QUESTION_PARAMETER = "uk.org.sucu.tatupload.QUESTION_PARAMETER";
	public static final String PARAMETER = "uk.org.sucu.tatupload.PARAMETER"; //identifies a parameter being passed in an intent extra
	
	public static final ArrayList<String> flavourParameter = new ArrayList<String>();
	public static final ArrayList<String> locationParameter = new ArrayList<String>();
	public static final ArrayList<String> questionParameter = new ArrayList<String>();

	public static void restoreDefaults(){
		loadParameters(getDefaultList(FLAVOUR_PARAMETER), getDefaultList(LOCATION_PARAMETER), getDefaultList(QUESTION_PARAMETER));
	}
	
	public static void loadParameters(ArrayList<String> flavour, ArrayList<String> location, ArrayList<String> question){
		flavourParameter.clear();
		for(String s : flavour){
			flavourParameter.add(s);
		}
		
		questionParameter.clear();
		for(String s : question){
			questionParameter.add(s);
		}
		
		locationParameter.clear();
		for(String s : location){
			locationParameter.add(s);
		}
	}
	
	public static ArrayList<String> getDefaultList(String identifier){
		if(identifier.equals(FLAVOUR_PARAMETER)){
			return new ArrayList<String>(Arrays.asList("ham","cheese","tomato","pineapple"));
		} else if(identifier.equals(LOCATION_PARAMETER)){
			return new ArrayList<String>(Arrays.asList("library","bar","pub","club","road","rd","avenue","gardens","street","st","terrace","block","flat","floor","room"));
		} else if(identifier.equals(QUESTION_PARAMETER)){
			return new ArrayList<String>(Arrays.asList("who","what","where","when","why","how","could","would","should","?"));
		}
		return null;
	}
	
	public static ArrayList<String> getList(String identifier){
		if(identifier.equals(FLAVOUR_PARAMETER)){
			return flavourParameter;
		} else if(identifier.equals(LOCATION_PARAMETER)){
			return locationParameter;
		} else if(identifier.equals(QUESTION_PARAMETER)){
			return questionParameter;
		}
		return null;
	}
	
	public static String getAsString(String identifier)throws NotSerializableException{
		return asString(getList(identifier));
	}
	
	public static String asString(ArrayList<String> list) throws NotSerializableException{
		
		try {
			return ObjectSerializer.serialize(list);
		} catch (IOException e) {
			throw new NotSerializableException(e.getMessage());
		}
	}
	
	public static boolean isValidIdentifier(String identifier){
		if(getList(identifier) == null){
			return false;
		} else {
			return true;
		}
	}
	
	public static void saveParameter(String identifier, Context context) throws IOException{
		if(isValidIdentifier(identifier)){
			
			String data;
			try {
				data = Parameters.getAsString(identifier);
			} catch (NotSerializableException e) {
				// TODO Auto-generated catch block
				throw new IOException(e.getMessage());
			}
			
			
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putString(identifier, data);
			editor.commit();
		}
	}
	

}
