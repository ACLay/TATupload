package uk.org.sucu.tatupload.activity;

import java.util.Calendar;

import uk.org.sucu.tatupload.BrowserAccessor;
import uk.org.sucu.tatupload.NetCaller;
import uk.org.sucu.tatupload.Notifications;
import uk.org.sucu.tatupload.R;
import uk.org.sucu.tatupload.Settings;
import uk.org.sucu.tatupload.TabContent;
import uk.org.sucu.tatupload.TabManager;
import uk.org.sucu.tatupload.message.SmsList;
import uk.org.sucu.tatupload.message.Text;
import uk.org.sucu.tatupload.parse.Parser;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;


public class MainActivity extends SherlockFragmentActivity {
	
	public static final String[] TAB_TITLES = new String[]{"Incoming","Processed"};

	public final static String TEXT_MESSAGE = "uk.org.sucu.tatupload.TEXT_MESSAGE";

	public static final int TUTORIAL_VERSION = 4;//TODO update this each time the tutorial is changed.

	private Settings settings;

	private TabHost mTabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		settings = new Settings(this);
		
		setupUI(savedInstanceState);

		int versionSeen = settings.getTutorialVersionSeen();
		
		if(versionSeen == Settings.TUTORIAL_SEEN_DEFAULT){
			Intent intent = new Intent(this, TutorialActivity.class);
			startActivity(intent);
			this.finish();
			return;
		} else if(versionSeen < 4){// add extra cases inform the user of changes to the app, etc.
			//remove the now unused form name field from the preference save file.
			settings.removePreference(getString(R.string.form_name_key));
			BrowserAccessor.openBrowserChoicePopup(this, false, null);
		}
		if(versionSeen != TUTORIAL_VERSION){
			settings.setTutorialVersionShown(TUTORIAL_VERSION);
		}

	}	
	
	private void setupUI(Bundle savedInstanceState){

		if(settings.getUsed()){
			setContentView(R.layout.main_queues);
			
			Button toggleButton = (Button) findViewById(R.id.toggleTatButton);
			if(settings.getProcessingTexts()){
				toggleButton.setText(R.string.stop);
			} else {
				toggleButton.setText(R.string.start);
			}
			
			mTabHost = (TabHost) findViewById(android.R.id.tabhost);
			mTabHost.setup();
			//TODO new logo
			//TODO settings for the 2nd queue
			
			TabManager mTabManager = new TabManager(this, mTabHost, R.id.realtabcontent);
			String unprocessed = getString(R.string.unprocessed);
			Bundle bundle = new Bundle(); bundle.putInt("queue", R.string.unprocessed);
			mTabManager.addTab(mTabHost.newTabSpec(unprocessed).setIndicator(unprocessed), TabContent.class, bundle);
			String uploaded = getString(R.string.uploaded);
			bundle = new Bundle(); bundle.putInt("queue", R.string.uploaded);
			mTabManager.addTab(mTabHost.newTabSpec(uploaded).setIndicator(uploaded), TabContent.class, bundle);
			
			if (savedInstanceState != null) {
	            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
	        }

		} else {
			setContentView(R.layout.start_screen);	
		}


	}
	
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tab", mTabHost.getCurrentTabTag());
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void clearMessages(View v){
		final SmsList visibleList;
		String currentTabTag = mTabHost.getCurrentTabTag();
		if(currentTabTag.equals(getString(R.string.unprocessed))){
			visibleList = SmsList.getPendingList();
		} else if(currentTabTag.equals(getString(R.string.uploaded))){
			visibleList = SmsList.getUploadedList();
		} else {
			return;
		}
		
		//do nothing if the list is empty
		if(!visibleList.isEmpty()){
			//confirm user choice
			DialogInterface.OnClickListener action = new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//remove
					visibleList.clearList();
					if(visibleList == SmsList.getPendingList()){
						settings.savePendingTextsList();
						Notifications.updateNotification(MainActivity.this);
					} else if(visibleList == SmsList.getUploadedList()){
						settings.saveUploadedTextsList();
					}
				}
			};

			confirmChoice(action, R.string.clear_queue_button);
		}
	}

	public void startTat(View v){
		startTatNewSpreadsheet();
	}
	
	//TODO test the browser check runnables
	private void resumeTat(){
		settings.setProcessingTexts(true);
		Button toggleButton = (Button) findViewById(R.id.toggleTatButton);
		toggleButton.setText(R.string.stop);
		Notifications.updateNotification(this);
	}
	
	private void startTatNewSpreadsheet(){
		String name = getString(R.string.text_a_toastie) + " " + Parser.getCurrentDateString();
		startTatNewSpreadsheet(name);
	}
	
	@SuppressLint("InflateParams")
	private void startTatNewSpreadsheet(String defaultName){
		if(BrowserAccessor.usable(this)){
			
			View view = LayoutInflater.from(this).inflate(R.layout.setup_layout , null);
			final EditText ssNameEdit = (EditText) view.findViewById(R.id.ssNameEditText);

			ssNameEdit.setText(defaultName);

			new AlertDialog.Builder(this)
			.setTitle(R.string.start)
			.setView(view)
			.setPositiveButton(R.string.start, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					String ssName = ssNameEdit.getText().toString();
					
					if(NetCaller.isOnlineWithToast(MainActivity.this)){
						//create the spreadsheet in the browser
						Uri uri = Parser.createNewSpreadsheetUri(ssName, MainActivity.this);
						NetCaller.callScript(uri, MainActivity.this);
						//Clear the uploaded list from the previous session
						SmsList.getUploadedList().clearList();
						settings.saveUploadedTextsList();
						//If it's the first use, set used to true
						if(!settings.getUsed()){
							settings.setUsed(true);
							//Add a text to the queue explaining how to use the queue
							SmsList.getPendingList().addText(new Text(getString(R.string.app_name),getString(R.string.queue_explanation),Calendar.getInstance().getTimeInMillis()));
							settings.savePendingTextsList();
							setupUI(null);//the toggle button must be created before resumeTat() the button's text
						}

						resumeTat();

					} else {
						//If no network connection is available, reopen the dialog
						startTatNewSpreadsheet(ssName);
					}


				}
			})
			.setNegativeButton(android.R.string.cancel, null)
			.create()
			.show();

		} else {
			final String name = defaultName;
			Runnable code = new Runnable(){
				@Override
				public void run() {
					startTatNewSpreadsheet(name);
				}
			};
			BrowserAccessor.openBrowserChoicePopup(this, false, code);
		}
	}
	
	private void stopTat(){
		
		DialogInterface.OnClickListener positiveAction = new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//stop processing new texts
				settings.setProcessingTexts(false);
				//change button text to Start
				Button toggleButton = (Button) findViewById(R.id.toggleTatButton);
				toggleButton.setText(R.string.start);
				Notifications.updateNotification(MainActivity.this);
			}
		};
		
		confirmChoice(positiveAction, R.string.stop);
	}
	
	public void toggleTat(View v){
		//if TAT is running
		if(settings.getProcessingTexts()){
			stopTat();
		} else {
			if(SmsList.getPendingList().isEmpty()){
				startTatNewSpreadsheet();
			} else {
				clearQueueBeforeStartDialog();
			}
			
			
		}
	}
	
	private void clearQueueBeforeStartDialog(){
		new AlertDialog.Builder(this)
		.setTitle(R.string.start)
		.setMessage("Clear unprocessed message list before starting?")
		.setNegativeButton(android.R.string.cancel, null)
		.setNeutralButton(R.string.no, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				startTatNewSpreadsheet();
			}
		})
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				SmsList.getPendingList().clearList();
				settings.savePendingTextsList();
				startTatNewSpreadsheet();
			}
		})
		.create()
		.show();
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
		.setNegativeButton(android.R.string.cancel, null)
		.create()
		.show();
	}
	
	public void showSettings(){
		Intent intent = new Intent(this, OptionActivity.class);
		startActivity(intent);
	}


	public boolean onOptionsItemSelected(MenuItem item){
		int itemId  = item.getItemId();
		if(itemId == R.id.action_settings){
			showSettings();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
		
	}
	
}
