package uk.org.sucu.tatupload;

import java.io.IOException;
import java.util.ArrayList;

import uk.org.sucu.tatupload.parse.Parameters;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class TatUploadApplication extends Application {
	
	private static boolean processingTexts = false;
	private static boolean confirmSplit = false;
	//private static ArrayList<Text> messages = new ArrayList<Text>();
	private static String formName = "";
	//private static final String uploadScript = "https://script.google.com/macros/s/AKfycbzfPd5U7tbyOmK8EERxB8LPn53CzLy_nzXzAu2jb2_fYC8V_aof/exec";

	private static int tutorialVersionShown;
	
	public void setProcessingTexts(boolean b){
		processingTexts = b;
		//TODO is there a better way to do this?
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean(getString(R.string.processing_key), processingTexts);
		editor.commit();
	}

	public void setConfirmSplit(boolean b){
		confirmSplit = b;
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean(getString(R.string.confirm_split_key), confirmSplit);
		editor.commit();
	}
	
	public void setFormName(String newName){
		formName= newName;
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(getString(R.string.form_name_key), formName);
		editor.commit();
	}
	
	public void setTutorialVersionShown(int version){
		tutorialVersionShown = version;
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(getString(R.string.tutorial_ver_key), tutorialVersionShown);
		editor.commit();
	}
	
	public void onCreate(){
		super.onCreate();
		//Load the application settings
		//TODO can this bit be done in another section?
		
		ArrayList<String> flavourList = SettingsAccessor.getSavedParameter(this, Parameters.FLAVOUR_PARAMETER);	
		ArrayList<String> locationList = SettingsAccessor.getSavedParameter(this, Parameters.LOCATION_PARAMETER);
		ArrayList<String> questionList = SettingsAccessor.getSavedParameter(this, Parameters.QUESTION_PARAMETER);

		Parameters.loadParameters(flavourList, locationList, questionList);
		
	}


}
