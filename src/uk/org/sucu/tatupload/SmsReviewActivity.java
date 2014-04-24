package uk.org.sucu.tatupload;

import java.util.ArrayList;

import uk.org.sucu.tatupload.message.SmsList;
import uk.org.sucu.tatupload.message.Text;
import uk.org.sucu.tatupload.parse.Parser;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SmsReviewActivity extends Activity {
	//TODO number blacklist
	private Text text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//extract the sms parameters from the intent
		Intent intent = getIntent();
		
		text = (Text) intent.getSerializableExtra(MainActivity.TEXT_MESSAGE);

		//create the UI
		setContentView(R.layout.activity_sms_review);

		//fill the phone number textview
		TextView numberText = (TextView) findViewById(R.id.senderNumberTextView);
		numberText.setText(text.getNumber());
		//fill the recieved time textview
		TextView timeText = (TextView) findViewById(R.id.timeRecievedTextView);
		String time = Parser.timeStampToString(text.getTimestamp());
		timeText.setText(time);
		//fill the editTexts
		performDefaultSplit();

		// Show the Up button in the action bar.
		setupActionBar();
	}

	private void performDefaultSplit(){

		EditText bodyEdit = (EditText) findViewById(R.id.messageBodyEditText);
		bodyEdit.setText(text.getBody());

		EditText questionEdit = (EditText) findViewById(R.id.messageQuestionEditText);
		ArrayList<String> questions = Parser.getQuestion(text.getBody());
		questionEdit.setText(Parser.concatenateArrayList(questions));

		EditText locationEdit = (EditText) findViewById(R.id.messageLocationEditText);
		ArrayList<String> locations = Parser.getLocation(text.getBody());
		locationEdit.setText(Parser.concatenateArrayList(locations));

		EditText toastieEdit = (EditText) findViewById(R.id.messageToastieEditText);
		ArrayList<String> flavours = Parser.getFlavours(text.getBody());
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

		if(NetCaller.isOnlineWithErrorBox(this)){

			EditText questionEdit = (EditText) findViewById(R.id.messageQuestionEditText);
			EditText locationEdit = (EditText) findViewById(R.id.messageLocationEditText);
			EditText toastieEdit = (EditText) findViewById(R.id.messageToastieEditText);
			EditText bodyEdit = (EditText) findViewById(R.id.messageBodyEditText);
			String formName = SettingsAccessor.getFormName(this);

			String question = questionEdit.getText().toString();
			String location = locationEdit.getText().toString();
			String toastie = toastieEdit.getText().toString();
			String body = bodyEdit.getText().toString();
			
			Uri uri = Parser.createUploadUri(formName, text.getNumber(), question, location, toastie, body, this);
			NetCaller.callScript(uri, this);
			
			SmsList.removeText(text);

			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			this.finish();
		}
		
	}

	public void undoChanges(View v){
		performDefaultSplit();
	}

	public void discardText(View v){
		//TODO R U SURE BOX
		SmsList.removeText(text);

		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		this.finish();
	}

	public void cancel(View v){
		this.finish();
	}

	//TODO upload time text was received
}
