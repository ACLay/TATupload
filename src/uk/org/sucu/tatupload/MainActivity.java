package uk.org.sucu.tatupload;

import java.util.ArrayList;

import uk.org.sucu.tatupload.message.QueuedSmsView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class MainActivity extends Activity {

	public final static String MESSAGE_NUMBER = "uk.org.sucu.tatupload.NUMBER";
	public final static String MESSAGE_BODY = "uk.org.sucu.tatupload.BODY";
	public final static String MESSAGE_TIME = "uk.org.sucu.tatupload.TIME";
	
	
	private ArrayList<SmsMessage> messages = new ArrayList<SmsMessage>();
	private boolean processingTexts = true;
	private boolean autoUpload = false;
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
		
		ListView messageView = (ListView) findViewById(R.id.messageListView);
		adapter = new MessageArrayAdapter(this, R.id.messageListView, messages);
		messageView.setAdapter(adapter);
		
		messageView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
				// Get the sms message contained in the clicked object
				QueuedSmsView smsView = (QueuedSmsView) v;
				SmsMessage sms = smsView.getSMS();
				//TODO send it in an intent to an SmsReviewActivity
				Intent intent = new Intent(v.getContext(), SmsReviewActivity.class);
				intent.putExtra(MESSAGE_NUMBER, sms.getOriginatingAddress());
				intent.putExtra(MESSAGE_BODY, sms.getMessageBody());
				intent.putExtra(MESSAGE_TIME, sms.getTimestampMillis());
				startActivity(intent);
			}
			
		});
		
		SmsReceiver.giveMessageQueue(adapter, messages);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void modifyParameters(View v){
		//TODO allow modification of parameters
	}
	
	public void clearMessages(View v){
		synchronized(messages){
			while(!messages.isEmpty()){
				messages.remove(0);
			}
		}
		adapter.notifyDataSetChanged();
	}

	
	
}
