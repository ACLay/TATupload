package uk.org.sucu.tatupload.activity;

import java.util.Collection;

import uk.org.sucu.tatupload.MessageArrayAdapter;
import uk.org.sucu.tatupload.R;
import uk.org.sucu.tatupload.SettingsAccessor;
import uk.org.sucu.tatupload.TatUploadApplication;
import uk.org.sucu.tatupload.message.SmsList;
import uk.org.sucu.tatupload.message.Text;
import uk.org.sucu.tatupload.views.QueuedSmsView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class MainActivity extends Activity {

	public final static String TEXT_MESSAGE = "uk.org.sucu.tatupload.TEXT_MESSAGE";

	public static final int TUTORIAL_VERSION = 3;//TODO update this each time the tutorial is changed.

	private MessageArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setupUI();

		int versionShown = SettingsAccessor.getTutorialVersionSeen(this);
		
		if(versionShown == 0){
			Intent intent = new Intent(this, TutorialActivity.class);
			startActivity(intent);
			this.finish();
			return;
		} else {// add extra cases inform the user of changes to the app, etc.
			
		}
		if(versionShown != TUTORIAL_VERSION){
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
		//confirm user choice
		DialogInterface.OnClickListener action = new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//remove
				SmsList.clearList();
			}
		};
		
		confirmChoice(action, R.string.clear_queue_button);
	}

	public void addMessages(Collection<Text> msgs){
		SmsList.addTexts(msgs);
	}

	public void startTat(View v){
		Intent intent = new Intent(this, SetupActivity.class);
		startActivity(intent);
		finish();
	}
	
	public void stopTat(View v){
		DialogInterface.OnClickListener action = new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//stop processing
				((TatUploadApplication) getApplication()).setProcessingTexts(false);
				//rebuild UI
				setupUI();
			}
		};
		confirmChoice(action, R.string.stop);
	}
	
	public void showSettings(View v){
		showSettings();
	}
	
	private void confirmChoice(DialogInterface.OnClickListener action, int action_name){
		//confirm user choice
		new AlertDialog.Builder(this)
		.setTitle(action_name)
		.setMessage(R.string.confirm_choice)
		.setPositiveButton(action_name, action)
		.setNegativeButton(R.string.cancel, null)
		.create()
		.show();
	}
	
	public void showSettings(){
		Intent intent = new Intent(this, OptionActivity.class);
		startActivity(intent);
	}

	public boolean onOptionsItemSelected(MenuItem item){
		
		switch (item.getItemId()){
			case R.id.action_settings:
				showSettings();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
		
	}
	
	
}
