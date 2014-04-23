package uk.org.sucu.tatupload;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
	
}
