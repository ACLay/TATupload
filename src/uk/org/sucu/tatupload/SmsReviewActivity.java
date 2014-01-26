package uk.org.sucu.tatupload;

import java.util.ArrayList;

import uk.org.sucu.tatupload.message.Parser;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SmsReviewActivity extends Activity {

	private String messageBody;
	private String number;
	private long timeStamp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//extract the sms parameters from the intent
		Intent intent = getIntent();
		messageBody = intent.getStringExtra(MainActivity.MESSAGE_BODY);
		number = intent.getStringExtra(MainActivity.MESSAGE_NUMBER);
		timeStamp = intent.getLongExtra(MainActivity.MESSAGE_TIME, 0);
		
		//create the UI
		setContentView(R.layout.activity_sms_review);
		
		//fill the phone number textview
		TextView numberText = (TextView) findViewById(R.id.senderNumberTextView);
		numberText.setText(number);
		//fill the recieved time textview
		TextView timeText = (TextView) findViewById(R.id.timeRecievedTextView);
		String time = new Parser().timeStampToString(timeStamp);
		timeText.setText(time);
		//fill the editTexts
		performDefaultSplit();
		
		// Show the Up button in the action bar.
		setupActionBar();
	}
	
	private void performDefaultSplit(){
		Parser par = new Parser();
		
		EditText bodyEdit = (EditText) findViewById(R.id.messageBodyEditText);
		bodyEdit.setText(messageBody);
		
		EditText questionEdit = (EditText) findViewById(R.id.messageQuestionEditText);
		ArrayList<String> questions = par.getQuestion(messageBody);
		questionEdit.setText(par.concatenateArrayList(questions));
		
		EditText locationEdit = (EditText) findViewById(R.id.messageLocationEditText);
		ArrayList<String> locations = par.getLocation(messageBody);
		locationEdit.setText(par.concatenateArrayList(locations));
		
		EditText toastieEdit = (EditText) findViewById(R.id.messageToastieEditText);
		ArrayList<String> flavours = par.getFlavours(messageBody);
		toastieEdit.setText(par.concatenateArrayList(flavours));
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sms_review, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	public void uploadMessage(View v){
		
	}
	
	public void undoChanges(View v){
		performDefaultSplit();
	}
	
	public void discardText(View v){
		this.finish();
	}
	
	
}
