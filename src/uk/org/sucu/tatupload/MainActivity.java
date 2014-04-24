package uk.org.sucu.tatupload;

import java.util.Collection;

import uk.org.sucu.tatupload.message.SmsList;
import uk.org.sucu.tatupload.message.Text;
import uk.org.sucu.tatupload.views.QueuedSmsView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class MainActivity extends Activity {

	public final static String TEXT_MESSAGE = "uk.org.sucu.tatupload.TEXT_MESSAGE";

	private static final int TUTORIAL_VERSION = 3;//TODO update this each time the tutorial is changed.

	private MessageArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setupUI();

		int versionShown = SettingsAccessor.getTutorialVersionSeen(this);
		
		if(versionShown < TUTORIAL_VERSION){
			//show the tutorial once per run.
			new AlertDialog.Builder(this)
			.setTitle("TATupload")  
			.setMessage("This app uses a google web script to create, and upload texts to, a google form. "
					+ "The script run in your phones browser and will require authorisation from your google account the first time it is run. "
					+ "Login information is stored by your browser, and is not accessed by TATupload. "
					+ "The documents created will be in the Google drive belonging to the google account signed into the browser. "
					+ "Before starting, please go to settings and enter names of flavours and locations you will be serving so they can be (hopefully) identified.")
					.setPositiveButton("Okay", null)  
					.setCancelable(false)  
					.create()  
					.show();

			((TatUploadApplication)getApplication()).setTutorialVersionShown(TUTORIAL_VERSION);
		}

	}	
	

	private void setupUI(){

		if(SettingsAccessor.getProcessingTexts(this)){
			setContentView(R.layout.message_queue);
			ListView messageView = (ListView) findViewById(R.id.messageListView);
			adapter = SmsList.getMessageArrayAdapter(this);
			messageView.setAdapter(adapter);

			messageView.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {

					// Get the sms message contained in the clicked object
					QueuedSmsView smsView = (QueuedSmsView) v;
					Text sms = smsView.getSMS();
					//send it in an intent to an SmsReviewActivity
					Intent intent = new Intent(v.getContext(), SmsReviewActivity.class);
					intent.putExtra(TEXT_MESSAGE, sms);
					startActivity(intent);
				}

			});
		} else {
			setContentView(R.layout.start_screen);	
		}


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void clearMessages(View v){
		SmsList.clearList();
	}
	//synchronized prevents these 2 from fighting.
	//TODO should these methods be static in the application?
	public void addMessages(Collection<Text> msgs){
		SmsList.addTexts(msgs);
	}

	public void startTat(View v){
		Intent intent = new Intent(this, SetupActivity.class);
		startActivity(intent);
		finish();
	}
	
	public void stopTat(View v){
		//TODO is there a better way to do this?
		((TatUploadApplication) getApplication()).setProcessingTexts(false);
		setupUI();
	}
	
	public void showSettings(View v){
		Intent intent = new Intent(this, OptionActivity.class);
		startActivity(intent);
	}

	
}
