package uk.org.sucu.tatupload;

import java.util.ArrayList;
import java.util.Collection;

import uk.org.sucu.tatupload.message.SmsReceiver;
import uk.org.sucu.tatupload.message.Text;
import uk.org.sucu.tatupload.views.ChangeFormNamePopup;
import uk.org.sucu.tatupload.views.NewFormPopup;
import uk.org.sucu.tatupload.views.QueuedSmsView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;


public class MainActivity extends Activity {
	
	public final static String TEXT_MESSAGE = "uk.org.sucu.tatupload.TEXT_MESSAGE";

	private static boolean tutorialNeedsShown = true;

	private MessageArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setupUI();

		if(tutorialNeedsShown){
			//show the tutorial once per run.
			new AlertDialog.Builder(this)
			.setTitle("TATupload")  
			.setMessage("This app uses a google web script to create, and upload texts to, a google form. "
					+ "The scripts run in your phones browser and will require authorisation from your google account the first time it is run. "
					+ "Login information is stored by your browser, and is not accessed by TATupload. "
					+ "The documents created will be in the Google drive belonging to the google account signed into the browser. "
					+ "Before texts can be uploaded the app will need to create a form and spreadsheet, or have the name of one it has made before, entered.")
					.setPositiveButton("Okay", null)  
					.setCancelable(false)  
					.create()  
					.show();

			tutorialNeedsShown = false;
		}

		SmsReceiver.giveMainActivity(this);

	}

	//TODO REMOVE FROM QUEUE WHEN PROCESSED

	
	private void setupUI(){
		//get screen rotation
		int rot = getResources().getConfiguration().orientation % 2;
		//load UI based on rotation
		if(rot == 0){
			setContentView(R.layout.activity_main_landscape);
		} else {
			setContentView(R.layout.activity_main_portrait);
		}

		populateFields();


		ListView messageView = (ListView) findViewById(R.id.messageListView);
		adapter = new MessageArrayAdapter(this, R.id.messageListView, TatUploadApplication.getMessageList());
		messageView.setAdapter(adapter);

		messageView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {

				if(isOnline()){

					// Get the sms message contained in the clicked object
					QueuedSmsView smsView = (QueuedSmsView) v;
					Text sms = smsView.getSMS();
					//send it in an intent to an SmsReviewActivity
					Intent intent = new Intent(v.getContext(), SmsReviewActivity.class);
					intent.putExtra(TEXT_MESSAGE, sms);
					startActivity(intent);
				} else {

				}
			}

		});
	}

	private void populateFields(){
		ToggleButton processTexts = (ToggleButton) findViewById(R.id.processingToggleButton);
		processTexts.setChecked(TatUploadApplication.getProcessingTexts());

		TextView formNameText = (TextView) findViewById(R.id.formNameTextView);
		formNameText.setText(TatUploadApplication.getFormName());

		CheckBox confirmSplit = (CheckBox) findViewById(R.id.confirmSplitCheckBox);
		confirmSplit.setChecked(TatUploadApplication.getConfirmSplit());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void toggleProcessing(View v){
		ToggleButton toggle = (ToggleButton) v;

		if(TatUploadApplication.getFormName().equals("")){

			new AlertDialog.Builder(this)
			.setTitle("Error")  
			.setMessage("You need to enter a form name to be able to process texts")
			.setPositiveButton("Okay", null)  
			.setCancelable(false)  
			.create()  
			.show();

			toggle.setChecked(false);
		} else {
			boolean processing = toggle.isChecked();
			TatUploadApplication.setProcessingTexts(processing);
		}		
	}

	public void toggleConfirmSplit(View v){
		CheckBox box = (CheckBox) findViewById(R.id.confirmSplitCheckBox);
		boolean confirmSplit = box.isChecked();
		TatUploadApplication.setConfirmSplit(confirmSplit);
	}

	public void modifyParameters(View v){
		//TODO allow modification of parameters
		new AlertDialog.Builder(this)
		.setTitle("Notice")  
		.setMessage("Currently, it is not possible to change the data extraction parameters. The button is just a placeholder.")
		.setPositiveButton(android.R.string.ok, null)  
		.setCancelable(false)  
		.create()  
		.show();
	}

	public void changeFormName(View v){
		ChangeFormNamePopup popup = new ChangeFormNamePopup(this);
		popup.showAtLocation(findViewById(R.id.main), Gravity.CENTER, 0, 0);
	}

	public void buildNewForm(View v){
		if(isOnline()){
			NewFormPopup popup = new NewFormPopup(this);
			popup.showAtLocation(findViewById(R.id.main), Gravity.CENTER, 0, 0);
		}
	}

	public void clearMessages(View v){
		ArrayList<Text> messages = TatUploadApplication.getMessageList();
		synchronized(messages){
			while(!messages.isEmpty()){
				messages.remove(0);
			}
		}
		adapter.notifyDataSetChanged();
	}
	//synchronized prevents these 2 from fighting.
	public void addMessages(Collection<Text> msgs){
		ArrayList<Text> messages = TatUploadApplication.getMessageList();
		synchronized(messages){
			for(Text txt : msgs){
				messages.add(txt);
			}
		}
		adapter.notifyDataSetChanged();
	}

	public boolean isOnline(){
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
		boolean online =  activeNetworkInfo != null && activeNetworkInfo.isConnected();
		//show an error dialog if there's no network connection
		if(!online){
			new AlertDialog.Builder(this)
			.setTitle("Problem")  
			.setMessage("There is no network connection available.")
			.setPositiveButton(android.R.string.ok, null)  
			.setCancelable(false)  
			.create()  
			.show();
		}

		return online;
	}

}
