package uk.org.sucu.tatupload;

import java.util.ArrayList;
import uk.org.sucu.tatupload.parse.Parameters;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class TatUploadApplication extends Application {
	
	//TODO move settings into Settings class
	public void setProcessingTexts(boolean processingTexts){
		getEditor()
		.putBoolean(getString(R.string.processing_key), processingTexts)
		.commit();
	}

	public void setConfirmSplit(boolean confirmSplit){
		getEditor()
		.putBoolean(getString(R.string.confirm_split_key), confirmSplit)
		.commit();
	}
	
	public void setTutorialVersionShown(int version){
		getEditor()
		.putInt(getString(R.string.tutorial_ver_key), version)
		.commit();
	}
	
	public void setBrowserData(String packageName, String name){
		getEditor()
		.putString(getString(R.string.browser_package_key), packageName)
		.putString(getString(R.string.browser_name_key), name)
		.commit();
	}
	
	public void removePreference(String key){
		getEditor()
		.remove(key)
		.commit();
	}
	
	
	@SuppressLint("CommitPrefEdits")
	private SharedPreferences.Editor getEditor(){
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sharedPref.edit();
		return editor;
	}
	
	public void onCreate(){
		super.onCreate();
		//Load the data extraction parameters, which need de-serializing to be usable, for improved access time.
		
		ArrayList<String> flavourList = Settings.getSavedParameter(this, Parameters.FLAVOUR_PARAMETER);	
		ArrayList<String> locationList = Settings.getSavedParameter(this, Parameters.LOCATION_PARAMETER);
		ArrayList<String> questionList = Settings.getSavedParameter(this, Parameters.QUESTION_PARAMETER);

		Parameters.loadParameters(flavourList, locationList, questionList);
	}


}
