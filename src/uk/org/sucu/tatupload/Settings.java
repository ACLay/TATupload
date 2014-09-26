package uk.org.sucu.tatupload;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.pig.impl.util.ObjectSerializer;

import uk.org.sucu.tatupload.message.SmsList;
import uk.org.sucu.tatupload.message.Text;
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
	public static final boolean USED_DEFAULT = false;
	public static final boolean NOTIFICATION_DEFAULT = true;
	
	private SharedPreferences sharedPref;
	private Context context;

	public Settings(Context context){
		sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		this.context = context;
	}

	public boolean getProcessingTexts(){
		boolean processing = sharedPref.getBoolean(context.getString(R.string.processing_key), PROCESSING_TEXTS_DEFAULT);
		return processing;
	}
	
	public boolean getAutoQueueTexts(){
		boolean confirmSplit = sharedPref.getBoolean(context.getString(R.string.confirm_split_key), AUTO_QUEUE_TEXTS_DEFAULT);
		return confirmSplit;
	}
	
	public int getTutorialVersionSeen(){
		int versionSeen = sharedPref.getInt(context.getString(R.string.tutorial_ver_key), TUTORIAL_SEEN_DEFAULT);
		return versionSeen;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getSavedParameter(String parameter){
		String serialData = sharedPref.getString(parameter, SAVED_PARAMETER_DEFAULT);
		ArrayList<String> list = null;
		
		try {
			list = (ArrayList<String>) ObjectSerializer.deserialize(serialData);
		} catch (IOException e) {
			if(getTutorialVersionSeen() != TUTORIAL_SEEN_DEFAULT){
				Toast.makeText(context, context.getString(R.string.param_load_error), Toast.LENGTH_LONG).show();
			}
		}
		
		if(list == null){//The serializer will return null if the string is null or length 0
			return Parameters.getDefaultList(parameter, context);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Text> getSavedTexts(){
		String serialData = sharedPref.getString(context.getString(R.string.saved_queue_key), SAVED_PARAMETER_DEFAULT);
		ArrayList<Text> list = null;
		
		try {
			list = (ArrayList<Text>) ObjectSerializer.deserialize(serialData);
		} catch (IOException e) {

		}
		
		if(list == null){
			list = new ArrayList<Text>();
		}
		return list;
	}
	
	public String getChosenBrowserPackage(){
		String browserPackage = sharedPref.getString(context.getString(R.string.browser_package_key), BROWSER_PACKAGE_DEFAULT);
		return browserPackage;
	}
	
	public String getChosenBrowserName(){
		String browserName = sharedPref.getString(context.getString(R.string.browser_name_key), BROWSER_NAME_DEFAULT);
		return browserName;
	}
	
	public boolean getUsed(){
		boolean used = sharedPref.getBoolean(context.getString(R.string.used_key), USED_DEFAULT);
		return used;
	}
	
	public boolean getShowingNotification(){
		boolean showing = sharedPref.getBoolean(context.getString(R.string.show_notification_key), NOTIFICATION_DEFAULT);
		return showing;
	}

	
	public void setProcessingTexts(boolean processingTexts){
		getEditor()
		.putBoolean(context.getString(R.string.processing_key), processingTexts)
		.commit();
	}

	public void setConfirmSplit(boolean confirmSplit){
		getEditor()
		.putBoolean(context.getString(R.string.confirm_split_key), confirmSplit)
		.commit();
	}
	
	public void setTutorialVersionShown(int version){
		getEditor()
		.putInt(context.getString(R.string.tutorial_ver_key), version)
		.commit();
	}
	
	public void setBrowserData(String packageName, String name){
		getEditor()
		.putString(context.getString(R.string.browser_package_key), packageName)
		.putString(context.getString(R.string.browser_name_key), name)
		.commit();
	}
	
	public void setUsed(boolean used){
		getEditor()
		.putBoolean(context.getString(R.string.used_key), used)
		.commit();
	}
	
	public void saveSmsList(){
		String data = null;
		try {
			data = SmsList.getSerialList();
		} catch (IOException e) {

		}
		
		getEditor()
		.putString(context.getString(R.string.saved_queue_key), data)
		.commit();
	}
	
	public void saveParameter(String identifier) throws IOException{
		if(Parameters.isValidIdentifier(identifier)){
			
			String data = Parameters.getAsSerialString(identifier);
			
			getEditor()
			.putString(identifier, data)
			.commit();
		}
	}
	
	
	public void removePreference(String key){
		getEditor()
		.remove(key)
		.commit();
	}
	
	
	@SuppressLint("CommitPrefEdits")
	private SharedPreferences.Editor getEditor(){
		SharedPreferences.Editor editor = sharedPref.edit();
		return editor;
	}
}
