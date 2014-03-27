package uk.org.sucu.tatupload;

import java.util.ArrayList;

import uk.org.sucu.tatupload.message.Text;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class TatUploadApplication extends Application {
	
	private static boolean processingTexts = false;
	private static boolean confirmSplit = false;
	private static ArrayList<Text> messages = new ArrayList<Text>();
	private static String formName = "";
	private static final String uploadScript = "https://script.google.com/macros/s/AKfycbzfPd5U7tbyOmK8EERxB8LPn53CzLy_nzXzAu2jb2_fYC8V_aof/exec";

	private static int tutorialVersionShown;
	
	public static boolean getProcessingTexts(){
		return processingTexts;
	}
	public void setProcessingTexts(boolean b){
		processingTexts = b;
		//TODO is there a better way to do this?
		SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean(getString(R.string.processing_key), processingTexts);
		editor.commit();
	}

	public static boolean getConfirmSplit(){
		return confirmSplit;
	}
	public void setConfirmSplit(boolean b){
		confirmSplit = b;
		
		SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean(getString(R.string.confirm_split_key), confirmSplit);
		editor.commit();
	}

	public static ArrayList<Text> getMessageList(){
		return messages;
	}
	
	public void setFormName(String newName){
		formName= newName;
		
		SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(getString(R.string.form_name_key), formName);
		editor.commit();
	}
	public static String getFormName(){
		return formName;
	}

	public static String getScriptURL(){
		return uploadScript;
	}
	
	public static int getTutorialVersionShown(){
		return tutorialVersionShown;
	}
	
	public void setTutorialVersionShown(int version){
		tutorialVersionShown = version;
		
		SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(getString(R.string.tutorial_ver_key), tutorialVersionShown);
		editor.commit();
	}
	
	public void onCreate(){
		super.onCreate();
		//Load the application settings
		SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		
		formName = sharedPref.getString(getString(R.string.form_name_key), "");
		processingTexts = sharedPref.getBoolean(getString(R.string.processing_key), false);
		confirmSplit = sharedPref.getBoolean(getString(R.string.confirm_split_key), false);
		tutorialVersionShown = sharedPref.getInt(getString(R.string.tutorial_ver_key), 0);
		
	}

}
