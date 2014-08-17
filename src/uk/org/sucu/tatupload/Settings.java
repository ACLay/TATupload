package uk.org.sucu.tatupload;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.pig.impl.util.ObjectSerializer;

import uk.org.sucu.tatupload.parse.Parameters;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class Settings {
	
	public static final boolean PROCESSING_TEXTS_DEFAULT = false;
	public static final boolean AUTO_QUEUE_TEXTS_DEFAULT = false;
	public static final int TUTORIAL_SEEN_DEFAULT = 0;
	private static final String SAVED_PARAMETER_DEFAULT = null;
	public static final String BROWSER_PACKAGE_DEFAULT = null;
	public static final String BROWSER_NAME_DEFAULT = null;

	public static boolean getProcessingTexts(Context context){
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		boolean processing = sharedPref.getBoolean(context.getString(R.string.processing_key), PROCESSING_TEXTS_DEFAULT);
		return processing;
	}
	
	public static boolean getAutoQueueTexts(Context context){
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		boolean confirmSplit = sharedPref.getBoolean(context.getString(R.string.confirm_split_key), AUTO_QUEUE_TEXTS_DEFAULT);
		return confirmSplit;
	}
	
	public static int getTutorialVersionSeen(Context context){
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		int versionSeen = sharedPref.getInt(context.getString(R.string.tutorial_ver_key), TUTORIAL_SEEN_DEFAULT);
		return versionSeen;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getSavedParameter(Context context, String parameter){
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		String serialData = sharedPref.getString(parameter, SAVED_PARAMETER_DEFAULT);
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
		String browserPackage = sharedPref.getString(context.getString(R.string.browser_package_key), BROWSER_PACKAGE_DEFAULT);
		return browserPackage;
	}
	
	public static String getChosenBrowserName(Context context){
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		String browserName = sharedPref.getString(context.getString(R.string.browser_name_key), BROWSER_NAME_DEFAULT);
		return browserName;
	}

	
	public static void setProcessingTexts(boolean processingTexts, Context context){
		getEditor(context)
		.putBoolean(context.getString(R.string.processing_key), processingTexts)
		.commit();
	}

	public static void setConfirmSplit(boolean confirmSplit, Context context){
		getEditor(context)
		.putBoolean(context.getString(R.string.confirm_split_key), confirmSplit)
		.commit();
	}
	
	public static void setTutorialVersionShown(int version, Context context){
		getEditor(context)
		.putInt(context.getString(R.string.tutorial_ver_key), version)
		.commit();
	}
	
	public static void setBrowserData(String packageName, String name, Context context){
		getEditor(context)
		.putString(context.getString(R.string.browser_package_key), packageName)
		.putString(context.getString(R.string.browser_name_key), name)
		.commit();
	}
	
	public static void removePreference(String key, Context context){
		getEditor(context)
		.remove(key)
		.commit();
	}
	
	
	@SuppressLint("CommitPrefEdits")
	private static SharedPreferences.Editor getEditor(Context context){
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPref.edit();
		return editor;
	}
}
