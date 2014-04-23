package uk.org.sucu.tatupload.parse;

import java.util.ArrayList;

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
	
	public static final String defaultFlavour = "ham:cheese:tomato:pinapple";
	public static final String defaultQuestion = "who:what:where:when:why:how:could:would:should:?";
	public static final String defaultLocation = "library:bar:pub:club:road: rd:avenue:gardens:street: st:terrace:block:flat:floor:room:";
	
	public static void restoreDefaults(){
		loadParameters(defaultFlavour, defaultLocation, defaultQuestion);
	}
	
	public static void loadParameters(String flavour, String location, String question){
		flavourParameter.clear();
		String[] flavours = flavour.split(":");
		for(String s : flavours){
			flavourParameter.add(s);
		}
		
		questionParameter.clear();
		String[] questions = question.split(":");
		for(String s : questions){
			questionParameter.add(s);
		}
		
		locationParameter.clear();
		String[] locations = location.split(":");
		for(String s : locations){
			locationParameter.add(s);
		}
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
	
	public static String getAsString(String identifier){
		return asString(getList(identifier));
	}
	
	public static String asString(ArrayList<String> list){
		if(list == null){
			return "";
		}
		if(list.size() == 0){
			return "";
		}
		StringBuilder builder = new StringBuilder();
	
		builder.append(list.get(1));
		
		for(int i=0; i < list.size(); i++){
			builder.append(":");
			builder.append(list.get(i));
		}
		
		return builder.toString();
	}
	
	public static boolean isValidIdentifier(String identifier){
		if(getList(identifier) == null){
			return false;
		} else {
			return true;
		}
	}
	
	public static void saveParameter(String identifier, Context context){
		if(identifier != null){
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putString(identifier, Parameters.getAsString(identifier));
			editor.commit();
		}
	}
}
