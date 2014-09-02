package uk.org.sucu.tatupload.activity;

import java.util.ArrayList;
import uk.org.sucu.tatupload.BrowserAccessor;
import uk.org.sucu.tatupload.NetCaller;
import uk.org.sucu.tatupload.R;
import uk.org.sucu.tatupload.message.SmsList;
import uk.org.sucu.tatupload.message.Text;
import uk.org.sucu.tatupload.parse.Parser;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SmsReviewActivity extends Activity {
	//TODO number blacklist
	private Text text;

	@SuppressLint("InflateParams")
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
		//add the control buttons
		LinearLayout layout = (LinearLayout) findViewById(R.id.smsReviewRoot);
		LinearLayout buttons;
		int rot = getResources().getConfiguration().orientation % 2;
		if(rot == 0){//landscape
			buttons = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.sms_review_button_landscape, null);
		} else {//portrait
			buttons = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.sms_review_button_portrait, null);
		}
		layout.addView(buttons);
		
		// Show the Up button in the action bar.
		setupActionBar();
	}

	private void performDefaultSplit(){

		EditText bodyEdit = (EditText) findViewById(R.id.messageBodyEditText);
		bodyEdit.setText(text.getBody());

		EditText questionEdit = (EditText) findViewById(R.id.messageQuestionEditText);
		ArrayList<String> questions = Parser.getQuestion(text.getBody());
		questionEdit.setText(Parser.concatenateArrayList(questions, ""));

		EditText locationEdit = (EditText) findViewById(R.id.messageLocationEditText);
		ArrayList<String> locations = Parser.getLocation(text.getBody());
		locationEdit.setText(Parser.concatenateArrayList(locations, ""));

		EditText toastieEdit = (EditText) findViewById(R.id.messageToastieEditText);
		ArrayList<String> flavours = Parser.getFlavours(text.getBody());
		toastieEdit.setText(Parser.concatenateArrayList(flavours, ", "));
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
		case R.id.action_settings:
			Intent intent = new Intent(this, OptionActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	public void uploadMessage(View v){

		if(BrowserAccessor.usable(this)){
			uploadMessage();
		} else {
			Runnable code = new Runnable(){
				@Override
				public void run() {
					uploadMessage();
				}
			};
			BrowserAccessor.openBrowserChoicePopup(this, false, code);
		}
	}
	
	
	private void uploadMessage(){
		if(NetCaller.isOnlineWithToast(this)){

			EditText questionEdit = (EditText) findViewById(R.id.messageQuestionEditText);
			EditText locationEdit = (EditText) findViewById(R.id.messageLocationEditText);
			EditText toastieEdit = (EditText) findViewById(R.id.messageToastieEditText);
			EditText bodyEdit = (EditText) findViewById(R.id.messageBodyEditText);
			
			String question = questionEdit.getText().toString();
			String location = locationEdit.getText().toString();
			String toastie = toastieEdit.getText().toString();
			String body = bodyEdit.getText().toString();
			String time = Parser.timeStampToString(text.getTimestamp());
			
			Uri uri = Parser.createUploadUri(text.getNumber(), question, location, toastie, body, time, this);
			NetCaller.callScript(uri, this);
			
			SmsList.removeText(text);
			SmsList.saveQueue(this);

			this.finish();
		}
		
	}

	public void undoChanges(View v){
		performDefaultSplit();
	}

	public void discardText(View v){
		final Activity act = this;
		new AlertDialog.Builder(this)
		.setTitle(R.string.discard)
		.setMessage(R.string.confirm_choice)
		.setPositiveButton(R.string.discard, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				SmsList.removeText(text);
				SmsList.saveQueue(SmsReviewActivity.this);
				act.finish();
			}
		})
		.setNegativeButton(android.R.string.cancel, null)
		.create()
		.show();
		

	}

	public void cancel(View v){
		this.finish();
	}

}
