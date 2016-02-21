package uk.org.sucu.tatupload2;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.pig.impl.util.ObjectSerializer;

import uk.org.sucu.tatupload2.message.SmsList;
import uk.org.sucu.tatupload2.message.Text;
import uk.org.sucu.tatupload2.parse.Parameters;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class Settings {
	
	public static final boolean PROCESSING_TEXTS_DEFAULT = false;
	public static final boolean AUTO_QUEUE_TEXTS_DEFAULT = false;
	public static final boolean STORING_PROCESSED_TEXTS_DEFAULT = true;
	public static final int TUTORIAL_SEEN_DEFAULT = 0;
	private static final String SAVED_SERIAL_ARRAYLIST_DEFAULT = null;
	public static final boolean USED_DEFAULT = false;
	public static final boolean NOTIFICATION_DEFAULT = true;
	public static final String ACCOUNT_DEFAULT = null;
	
	private SharedPreferences sharedPref;
	private Context context;

	public Settings(Context context){
		sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		this.context = context;
	}

	public boolean getProcessingTexts(){
		return sharedPref.getBoolean(context.getString(R.string.processing_key), PROCESSING_TEXTS_DEFAULT);
	}
	
	public boolean getAutoQueueTexts(){
		return sharedPref.getBoolean(context.getString(R.string.confirm_split_key), AUTO_QUEUE_TEXTS_DEFAULT);
	}
	
	public boolean getStoringProcesseds(){
		return sharedPref.getBoolean(context.getString(R.string.store_processed_key), STORING_PROCESSED_TEXTS_DEFAULT);
	}
	
	public int getTutorialVersionSeen(){
		return sharedPref.getInt(context.getString(R.string.tutorial_ver_key), TUTORIAL_SEEN_DEFAULT);
	}
	
	public ArrayList<String> getSavedParameter(String parameter){
		
		ArrayListLoader<String> loader = new ArrayListLoader<String>();
		loader.setExceptionRunnable(new Runnable(){
			public void run() {
				//show a loading error if there should be data to load
				if(getTutorialVersionSeen() != TUTORIAL_SEEN_DEFAULT){
					Toast.makeText(context, context.getString(R.string.param_load_error), Toast.LENGTH_LONG).show();
				}
			}
		});
		ArrayList<String> params = loader.loadList(parameter);
		if(params == null){
			params = Parameters.getDefaultList(parameter, context);
		}
		return params;
	}
	
	public ArrayList<Text> loadPendingTexts(){
		ArrayListLoader<Text> loader = new ArrayListLoader<Text>();
		ArrayList<Text> texts = loader.loadList(context.getString(R.string.pending_text_list_key));
		if(texts == null){
			texts = new ArrayList<Text>();
		}
		return texts;
	}
	
	
	public ArrayList<Text> loadUploadedTexts(){
		ArrayListLoader<Text> loader = new ArrayListLoader<Text>();
		ArrayList<Text> texts = loader.loadList(context.getString(R.string.processed_text_list_key));
		if(texts == null){
			texts = new ArrayList<Text>();
		}
		return texts;
	}
	
	
	private class ArrayListLoader<T>{
		
		private Runnable exceptionRunnable = null;
		
		public void setExceptionRunnable(Runnable runner){
			exceptionRunnable = runner;
		}
		
		@SuppressWarnings("unchecked")
		public ArrayList<T> loadList(String key){
			String serialData = sharedPref.getString(key, SAVED_SERIAL_ARRAYLIST_DEFAULT);
			ArrayList<T> list = null;
			
			try {
				list = (ArrayList<T>) ObjectSerializer.deserialize(serialData);
			} catch (IOException e) {
				if(exceptionRunnable != null){
					exceptionRunnable.run();
				}
			}
			
			return list;
		}
		
	}

	
	public boolean getUsed(){
		return sharedPref.getBoolean(context.getString(R.string.used_key), USED_DEFAULT);
	}
	
	public boolean getShowingNotification(){
		return sharedPref.getBoolean(context.getString(R.string.show_notification_key), NOTIFICATION_DEFAULT);
	}

	public String getPreferredAccount(){
		return sharedPref.getString(context.getString(R.string.preferred_account_key), ACCOUNT_DEFAULT);
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
	
	public void setUsed(boolean used){
		getEditor()
		.putBoolean(context.getString(R.string.used_key), used)
		.commit();
	}

	public void setPreferredAccount(String account){
		getEditor()
		.putString(context.getString(R.string.preferred_account_key),account)
		.commit();
	}
	
	public void savePendingTextsList(){
		saveSmsList(SmsList.getPendingList(), context.getString(R.string.pending_text_list_key));
	}
	
	public void saveUploadedTextsList(){
		saveSmsList(SmsList.getUploadedList(), context.getString(R.string.processed_text_list_key));
	}
	
	private void saveSmsList(SmsList list, String key){
		String data = null;
		try {
			data = list.getSerialList();
		} catch (IOException e) {
			Toast.makeText(context, "TATupload was unable to save a text list", Toast.LENGTH_SHORT).show();
		}
		
		getEditor()
		.putString(key, data)
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
		return sharedPref.edit();
	}
}
