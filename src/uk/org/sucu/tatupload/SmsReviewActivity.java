package uk.org.sucu.tatupload;

import java.util.ArrayList;

import uk.org.sucu.tatupload.message.Parser;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Browser;
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
		String time = Parser.timeStampToString(timeStamp);
		timeText.setText(time);
		//fill the editTexts
		performDefaultSplit();
		
		// Show the Up button in the action bar.
		setupActionBar();
	}
	
	private void performDefaultSplit(){
		
		EditText bodyEdit = (EditText) findViewById(R.id.messageBodyEditText);
		bodyEdit.setText(messageBody);
		
		EditText questionEdit = (EditText) findViewById(R.id.messageQuestionEditText);
		ArrayList<String> questions = Parser.getQuestion(messageBody);
		questionEdit.setText(Parser.concatenateArrayList(questions));
		
		EditText locationEdit = (EditText) findViewById(R.id.messageLocationEditText);
		ArrayList<String> locations = Parser.getLocation(messageBody);
		locationEdit.setText(Parser.concatenateArrayList(locations));
		
		EditText toastieEdit = (EditText) findViewById(R.id.messageToastieEditText);
		ArrayList<String> flavours = Parser.getFlavours(messageBody);
		toastieEdit.setText(Parser.concatenateArrayList(flavours));
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
		EditText questionEdit = (EditText) findViewById(R.id.messageQuestionEditText);
		EditText locationEdit = (EditText) findViewById(R.id.messageLocationEditText);
		EditText toastieEdit = (EditText) findViewById(R.id.messageToastieEditText);
		EditText bodyEdit = (EditText) findViewById(R.id.messageBodyEditText);
		String formName = TatUploadApplication.getFormName();
		
		String question = questionEdit.getText().toString();
		String location = locationEdit.getText().toString();
		String toastie = toastieEdit.getText().toString();
		String body = bodyEdit.getText().toString();
		
		//TODO this code block is identical to one in SmsReceiver
		Uri uri = Parser.createUploadUri(formName, number, question, location, toastie, body);
				
		//While it would be nice to handle everything in-app, it seems for the time being I'll need to go via the browser.
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
		//open all this apps requests in the same tab, prevents new ones with each call
		browserIntent.putExtra(Browser.EXTRA_APPLICATION_ID, getPackageName());
		//allows a new task to be started outside of a current task
		browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.startActivity(browserIntent);
		
	}
	
	public void undoChanges(View v){
		performDefaultSplit();
	}
	
	public void discardText(View v){
		this.finish();
	}
	
	
}
