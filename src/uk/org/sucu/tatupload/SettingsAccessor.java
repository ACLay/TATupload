package uk.org.sucu.tatupload;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.pig.impl.util.ObjectSerializer;

import uk.org.sucu.tatupload.parse.Parameters;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class SettingsAccessor {
	
	public static boolean getProcessingTexts(Context context){
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		boolean processing = sharedPref.getBoolean(context.getString(R.string.processing_key), false);
		return processing;
	}
	
	public static boolean getAutoQueueTexts(Context context){
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		boolean confirmSplit = sharedPref.getBoolean(context.getString(R.string.confirm_split_key), false);
		return confirmSplit;
	}
	//TODO This is no longer used
	public static String getFormName(Context context){
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		String formName = sharedPref.getString(context.getString(R.string.form_name_key), "");
		return formName;
	}
	
	public static int getTutorialVersionSeen(Context context){
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		int versionSeen = sharedPref.getInt(context.getString(R.string.tutorial_ver_key), 0);
		return versionSeen;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getSavedParameter(Context context, String parameter){
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		String serialData = sharedPref.getString(parameter, null);
		ArrayList<String> list = null;
		
		try {
			list = (ArrayList<String>) ObjectSerializer.deserialize(serialData);
		} catch (IOException e) {
			if(getTutorialVersionSeen(context) != 0){
				Toast.makeText(context, context.getString(R.string.param_load_error), Toast.LENGTH_LONG).show();
			}
		}
		
		if(list == null){//The serializer will return null if the string is null or length 0
			return Parameters.getDefaultList(parameter);
		}
		return list;
	}
	
	public static String getChosenBrowserPackage(Context context){
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		String browserPackage = sharedPref.getString(context.getString(R.string.browser_package_key), null);
		return browserPackage;
	}
	
	public static String getChosenBrowserName(Context context){
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		String browserName = sharedPref.getString(context.getString(R.string.browser_name_key), null);
		return browserName;
	}
	
}
