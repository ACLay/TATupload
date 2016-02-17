package uk.org.sucu.tatupload.parse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.pig.impl.util.ObjectSerializer;

import uk.org.sucu.tatupload.R;
import android.content.Context;

public class Parameters {
	//These constants are duplicated in strings.xml so other xml files can load them
	public static final String FLAVOUR_PARAMETER = "uk.org.sucu.tatupload.FLAVOUR_PARAMETER";
	public static final String LOCATION_PARAMETER = "uk.org.sucu.tatupload.LOCATION_PARAMETER";
	public static final String QUESTION_PARAMETER = "uk.org.sucu.tatupload.QUESTION_PARAMETER";
	public static final String PARAMETER = "uk.org.sucu.tatupload.PARAMETER"; //identifies a parameter being passed in an intent extra
	
	public static final ArrayList<String> flavourParameter = new ArrayList<String>();
	public static final ArrayList<String> locationParameter = new ArrayList<String>();
	public static final ArrayList<String> questionParameter = new ArrayList<String>();

	public static void restoreDefaults(Context context){
		loadParameters(getDefaultList(FLAVOUR_PARAMETER, context), getDefaultList(LOCATION_PARAMETER, context), getDefaultList(QUESTION_PARAMETER, context));
	}
	
	public static void loadParameters(ArrayList<String> flavour, ArrayList<String> location, ArrayList<String> question){
		loadParameter(FLAVOUR_PARAMETER, flavour);
		loadParameter(QUESTION_PARAMETER, question);
		loadParameter(LOCATION_PARAMETER, location);
	}
	
	public static void loadParameter(String identifier, ArrayList<String> values){
		if(isValidIdentifier(identifier)){
			ArrayList<String> param = getList(identifier);
			param.clear();
			for(String s : values){
				param.add(s);
			}
		}
	}
	
	public static ArrayList<String> getDefaultList(String identifier, Context context){
		switch (identifier) {
			case FLAVOUR_PARAMETER: {
				String[] defaults = context.getResources().getStringArray(R.array.default_flavours);
				return new ArrayList<String>(Arrays.asList(defaults));
			}
			case LOCATION_PARAMETER: {
				String[] defaults = context.getResources().getStringArray(R.array.default_locations);
				return new ArrayList<String>(Arrays.asList(defaults));
			}
			case QUESTION_PARAMETER: {
				String[] defaults = context.getResources().getStringArray(R.array.default_questions);
				return new ArrayList<String>(Arrays.asList(defaults));
			}
		}
		return null;
	}
	
	public static ArrayList<String> getList(String identifier){
		switch (identifier) {
			case FLAVOUR_PARAMETER:
				return flavourParameter;
			case LOCATION_PARAMETER:
				return locationParameter;
			case QUESTION_PARAMETER:
				return questionParameter;
		}
		return null;
	}
	
	public static String getParamHeading(String identifier, Context c){
		
		if(isValidIdentifier(identifier)){
			if(identifier.equals(FLAVOUR_PARAMETER)){
				return c.getString(R.string.flavour_heading);
			}
			if(identifier.equals(LOCATION_PARAMETER)){
				return c.getString(R.string.location_heading);
			}
			if(identifier.equals(QUESTION_PARAMETER)){
				return c.getString(R.string.question_heading);
			}
		}
		
		return c.getString(R.string.invalid_heading);
				
	}
	
	public static String getParamExplanation(String identifier, Context c){
		if(isValidIdentifier(identifier)){
			if(identifier.equals(FLAVOUR_PARAMETER)){
				return c.getString(R.string.flavour_explanation);
			}
			if(identifier.equals(LOCATION_PARAMETER)){
				return c.getString(R.string.location_explanation);
			}
			if(identifier.equals(QUESTION_PARAMETER)){
				return c.getString(R.string.question_explanation);
			}
		}
		
		return c.getString(R.string.invalid_heading);
			
	}
	
	public static String getAsSerialString(String identifier)throws IOException{
		ArrayList<String> list = getList(identifier);
		
		return ObjectSerializer.serialize(list);
	}
	
	public static boolean isValidIdentifier(String identifier){
		return getList(identifier) != null;
	}

}
