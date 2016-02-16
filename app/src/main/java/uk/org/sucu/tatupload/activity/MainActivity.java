package uk.org.sucu.tatupload.activity;

import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.Calendar;

import uk.org.sucu.tatupload.Notifications;
import uk.org.sucu.tatupload.R;
import uk.org.sucu.tatupload.Settings;
import uk.org.sucu.tatupload.TabContent;
import uk.org.sucu.tatupload.TabManager;
import uk.org.sucu.tatupload.message.SmsList;
import uk.org.sucu.tatupload.message.Text;
import uk.org.sucu.tatupload.network.AuthManager;
import uk.org.sucu.tatupload.network.MakeSheetTask;
import uk.org.sucu.tatupload.network.NetManager;
import uk.org.sucu.tatupload.parse.Parser;


public class MainActivity extends AppCompatActivity {
	
	//TODO move all strings into xml

	public final static String TEXT_MESSAGE = "uk.org.sucu.tatupload.TEXT_MESSAGE";

	public static final int TUTORIAL_VERSION = 5;//TODO update this each time the tutorial is changed.

	private Settings settings;

	private TabHost mTabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		settings = new Settings(this);
		
		setupUI(savedInstanceState);

		if (!AuthManager.isGooglePlayServicesAvailable(this)) {
			Toast.makeText(this, R.string.play_services_warning, Toast.LENGTH_SHORT).show();
			this.finish();
			return;
		}

		int versionSeen = settings.getTutorialVersionSeen();
		
		if(versionSeen == Settings.TUTORIAL_SEEN_DEFAULT){
			//new install, show them the tutorial
			Intent intent = new Intent(this, TutorialActivity.class);
			startActivity(intent);
			this.finish();
			return;
		} else if(versionSeen < 5){// add extra cases inform the user of changes to the app, etc.
			//remove the now unused form name field from the preference save file.
			settings.removePreference(getString(R.string.form_name_key));
			//remove the now unused browser settings
			settings.removePreference(getString(R.string.browser_package_key));
			settings.removePreference(getString(R.string.browser_name_key));
			//if a previous install has just been updated to the new uploading method, let them know
			//the exact message should change if TAT is running
			if(settings.getProcessingTexts()){
				new AlertDialog.Builder(this)
						.setTitle(R.string.v2_update_heading)
						.setMessage(R.string.v2_update_running)
						.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// Run the account picker
								AuthManager.chooseAccount(MainActivity.this);
							}
						})
						.setCancelable(false)
						.create()
						.show();
			} else {
				new AlertDialog.Builder(this)
						.setTitle(R.string.v2_update_heading)
						.setMessage(R.string.v2_update_not_running)
						.setPositiveButton(android.R.string.ok, null)
						.create()
						.show();
			}
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
			
			
			TabManager mTabManager = new TabManager(this, mTabHost, R.id.realtabcontent);
			String unprocessed = getString(R.string.unprocessed);
			Bundle bundle = new Bundle(); bundle.putInt(TabContent.QUEUE_ID, R.string.unprocessed);
			mTabManager.addTab(mTabHost.newTabSpec(unprocessed).setIndicator(unprocessed), TabContent.class, bundle);
			String uploaded = getString(R.string.uploaded);
			bundle = new Bundle(); bundle.putInt(TabContent.QUEUE_ID, R.string.uploaded);
			mTabManager.addTab(mTabHost.newTabSpec(uploaded).setIndicator(uploaded), TabContent.class, bundle);
			
			if (savedInstanceState != null) {
				String tabKey = getString(R.string.bundle_key_tab_selected);
	            mTabHost.setCurrentTabByTag(savedInstanceState.getString(tabKey));
	        }

		} else {
			setContentView(R.layout.start_screen);	
		}


	}
	
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mTabHost != null){
        	String tabKey = getString(R.string.bundle_key_tab_selected);
        	outState.putString(tabKey, mTabHost.getCurrentTabTag());
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Called when an activity launched here (specifically, AccountPicker
	 * and authorization) exits, giving you the requestCode you started it with,
	 * the resultCode it returned, and any additional data from it.
	 * @param requestCode code indicating which activity result is incoming.
	 * @param resultCode code indicating the result of the incoming
	 *     activity result.
	 * @param data Intent (containing result data) returned by incoming
	 *     activity result.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode) {
			case AuthManager.REQUEST_GOOGLE_PLAY_SERVICES:
				if (resultCode != RESULT_OK) {
					AuthManager.isGooglePlayServicesAvailable(this);
				}
				break;
			case AuthManager.REQUEST_ACCOUNT_PICKER:
				if (resultCode == RESULT_OK && data != null &&
						data.getExtras() != null) {
					String accountName =
							data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
					if (accountName != null) {
						AuthManager.setAccountName(accountName, this);
					}
				} else if (resultCode == RESULT_CANCELED) {
					Toast.makeText(this, R.string.no_account_selected, Toast.LENGTH_SHORT).show();
				}
				break;
			case AuthManager.REQUEST_AUTHORIZATION:
				if (resultCode != RESULT_OK) {
					AuthManager.chooseAccount(this);
				}
				break;
		}

		super.onActivityResult(requestCode, resultCode, data);
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
		if(AuthManager.isAccountSet()){
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
					
					if(NetManager.isOnlineWithToast(MainActivity.this)){
						//create the spreadsheet
						ProgressDialog mProgress = new ProgressDialog(MainActivity.this);
						mProgress.setMessage(getString(R.string.progress_creating));
						MakeSheetTask creator = new MakeSheetTask(ssName, mProgress, MainActivity.this){

							@Override
							protected void onPostExecute(Void output) {
								super.onPostExecute(output);
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
							}

						};
						creator.execute();


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
			// Run the account picker
			AuthManager.chooseAccount(this);
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
		.setMessage(R.string.clear_unprocessed)
		.setNegativeButton(android.R.string.cancel, null)
		.setNeutralButton(android.R.string.no, new DialogInterface.OnClickListener() {
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
