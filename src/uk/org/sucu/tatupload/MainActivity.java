package uk.org.sucu.tatupload;

import java.util.ArrayList;

import uk.org.sucu.tatupload.message.SmsReceiver;
import uk.org.sucu.tatupload.views.NewFormPopup;
import uk.org.sucu.tatupload.views.QueuedSmsView;
import uk.org.sucu.tatupload.views.ChangeFormIdPopup;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ToggleButton;


public class MainActivity extends Activity {

	public final static String MESSAGE_NUMBER = "uk.org.sucu.tatupload.NUMBER";
	public final static String MESSAGE_BODY = "uk.org.sucu.tatupload.BODY";
	public final static String MESSAGE_TIME = "uk.org.sucu.tatupload.TIME";
	
	
	//private ArrayList<SmsMessage> messages = new ArrayList<SmsMessage>();
	private MessageArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
				// Get the sms message contained in the clicked object
				QueuedSmsView smsView = (QueuedSmsView) v;
				SmsMessage sms = smsView.getSMS();
				//send it in an intent to an SmsReviewActivity
				Intent intent = new Intent(v.getContext(), SmsReviewActivity.class);
				intent.putExtra(MESSAGE_NUMBER, sms.getOriginatingAddress());
				intent.putExtra(MESSAGE_BODY, sms.getMessageBody());
				intent.putExtra(MESSAGE_TIME, sms.getTimestampMillis());
				startActivity(intent);
			}

		});

		SmsReceiver.giveMainActivity(this);

	}

	private void populateFields(){
		ToggleButton processTexts = (ToggleButton) findViewById(R.id.processingToggleButton);
		processTexts.setChecked(TatUploadApplication.getProcessingTexts());
		
		EditText formId = (EditText) findViewById(R.id.formIdEditText);
		formId.setText(TatUploadApplication.getFormID());
		
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
		ToggleButton toggle = (ToggleButton) findViewById(R.id.processingToggleButton);
		boolean processing = toggle.isChecked();
		TatUploadApplication.setProcessingTexts(processing);
	}

	public void toggleConfirmSplit(View v){
		CheckBox box = (CheckBox) findViewById(R.id.confirmSplitCheckBox);
		boolean confirmSplit = box.isChecked();
		TatUploadApplication.setConfirmSplit(confirmSplit);
	}
	
	public void modifyParameters(View v){
		//TODO allow modification of parameters, then enable button in xml
	}
	
	public void changeFormID(View v){
		ChangeFormIdPopup popup = new ChangeFormIdPopup(this);
		popup.showAtLocation(v, Gravity.CENTER, 0, 0);
	}

	public void buildNewForm(View v){
		NewFormPopup popup = new NewFormPopup(this);
		popup.showAtLocation(v, Gravity.CENTER, 0, 0);
	}
	
	public void clearMessages(View v){
		ArrayList<SmsMessage> messages = TatUploadApplication.getMessageList();
		synchronized(messages){
			while(!messages.isEmpty()){
				messages.remove(0);
			}
		}
		adapter.notifyDataSetChanged();
	}
	//synchronized prevents these 2 from fighting.
	public void addMessages(SmsMessage[] msgs){
		ArrayList<SmsMessage> messages = TatUploadApplication.getMessageList();
		synchronized(messages){
			for(SmsMessage m : msgs){
				messages.add(m);
			}
		}
		adapter.notifyDataSetChanged();
	}

}
