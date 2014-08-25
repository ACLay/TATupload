package uk.org.sucu.tatupload.activity;

import java.util.Collection;

import uk.org.sucu.tatupload.BrowserAccessor;
import uk.org.sucu.tatupload.MessageArrayAdapter;
import uk.org.sucu.tatupload.NetCaller;
import uk.org.sucu.tatupload.R;
import uk.org.sucu.tatupload.Settings;
import uk.org.sucu.tatupload.message.SmsList;
import uk.org.sucu.tatupload.message.Text;
import uk.org.sucu.tatupload.parse.Parser;
import uk.org.sucu.tatupload.views.QueuedSmsView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;


public class MainActivity extends Activity {

	public final static String TEXT_MESSAGE = "uk.org.sucu.tatupload.TEXT_MESSAGE";

	public static final int TUTORIAL_VERSION = 4;//TODO update this each time the tutorial is changed.

	private MessageArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setupUI();

		int versionSeen = Settings.getTutorialVersionSeen(this);
		
		if(versionSeen == Settings.TUTORIAL_SEEN_DEFAULT){
			Intent intent = new Intent(this, TutorialActivity.class);
			startActivity(intent);
			this.finish();
			return;
		} else if(versionSeen < 4){// add extra cases inform the user of changes to the app, etc.
			//remove the now unused form name field from the preference save file.
			Settings.removePreference(getString(R.string.form_name_key),this);
			BrowserAccessor.openBrowserChoicePopup(this, false);
		}
		if(versionSeen != TUTORIAL_VERSION){
			Settings.setTutorialVersionShown(TUTORIAL_VERSION, this);
		}

	}	
	

	private void setupUI(){

		if(Settings.getUsed(this)){
			setContentView(R.layout.message_queue);
			
			Button toggleButton = (Button) findViewById(R.id.toggleTatButton);
			if(Settings.getProcessingTexts(this)){
				toggleButton.setText(R.string.stop);
			} else {
				toggleButton.setText(R.string.start);
			}
			
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
		//show nothing if the list is empty
		if(!SmsList.isEmpty()){
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
	}

	public void addMessages(Collection<Text> msgs){
		SmsList.addTexts(msgs);
	}

	public void startTat(View v){
		startTatNewSpreadsheet();
	}
	
	//TODO test these start methods, implement new webscript methods
	private void resumeTat(){
		Settings.setProcessingTexts(true, this);
		Button toggleButton = (Button) findViewById(R.id.toggleTatButton);
		toggleButton.setText(R.string.stop);
	}
	
	private void startTatNewSheet(){
		if(NetCaller.isOnlineWithToast(this)){
			Uri uri = Parser.createAddSheetUri(this,"Texts");
			NetCaller.callScript(uri, this);
			resumeTat();
		} else {
			openStartTatDialog();
		}
	}
	
	private void startTatNewSpreadsheet(){
		startTatNewSpreadsheet(getString(R.string.text_a_toastie) + " " + Parser.getCurrentDateString());
	}
	
	private void startTatNewSpreadsheet(String defaultName){
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
						
					Uri uri = Parser.createNewSpreadsheetUri(ssName, MainActivity.this);
					NetCaller.callScript(uri, MainActivity.this);
					
					if(!Settings.getUsed(MainActivity.this)){
						Settings.setUsed(true, MainActivity.this);
						setupUI();//the toggle button must be created before resumeTat() sets its text
					}
					
					resumeTat();
					
				} else {
					startTatNewSpreadsheet(ssName);
				}
				

			}
		})
		.setNegativeButton(android.R.string.cancel, null)
		.create()
		.show();
	}
	
	private void stopTat(){
		Settings.setProcessingTexts(false, this);
		Button toggleButton = (Button) findViewById(R.id.toggleTatButton);
		toggleButton.setText(R.string.start);
	}
	
	public void toggleTat(View v){
		//if TAT is running
		if(Settings.getProcessingTexts(this)){
			DialogInterface.OnClickListener positiveAction = new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//stop processing new texts
					stopTat();
				}
			};
			confirmChoice(positiveAction, R.string.stop);
		} else {
			openStartTatDialog();
		}
	}
	
	public void openStartTatDialog(){
		final RadioGroup v = (RadioGroup) LayoutInflater.from(this).inflate(R.layout.start_method_chooser , null);;
		//give choice to start in same sheet, new sheet, or new spreadsheet
		new AlertDialog.Builder(this)
		.setTitle(R.string.start)
		.setView(v)
	
		.setPositiveButton(R.string.start, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				int selected = v.getCheckedRadioButtonId();
				
				switch(selected){
				case R.id.radio_new_ss:
					startTatNewSpreadsheet();
					break;
				/*case R.id.radio_new_sheet:
					startTatNewSheet();
					break;*/ //TODO add this
				case R.id.radio_resume:
					resumeTat();
					break;
				}
			}
		})
		.setNegativeButton(android.R.string.cancel, null)
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
		
		switch (item.getItemId()){
			case R.id.action_settings:
				showSettings();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
		
	}
	
	
}
