package uk.org.sucu.tatupload2.activity;

import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import uk.org.sucu.tatupload2.Notifications;
import uk.org.sucu.tatupload2.R;
import uk.org.sucu.tatupload2.Settings;
import uk.org.sucu.tatupload2.message.SmsList;
import uk.org.sucu.tatupload2.message.Text;
import uk.org.sucu.tatupload2.network.AuthManager;
import uk.org.sucu.tatupload2.network.NetManager;
import uk.org.sucu.tatupload2.network.UploadTextTask;
import uk.org.sucu.tatupload2.parse.Parser;


public class SmsReviewActivity extends AppCompatActivity {

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

	private void setupActionBar() {
		ActionBar actionbar = getSupportActionBar();
		if(actionbar != null) {
			actionbar.setDisplayHomeAsUpEnabled(true);
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
		int itemId = item.getItemId();
		if(itemId == R.id.action_settings){
			Intent intent = new Intent(this, OptionActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	public void uploadMessage(View v){
		uploadMessage();
	}


	private void uploadMessage(){
		if(NetManager.isOnlineWithToast(this)){

			if(AuthManager.isAccountSet()) {
				// retrieve the edited values
				EditText questionEdit = (EditText) findViewById(R.id.messageQuestionEditText);
				EditText locationEdit = (EditText) findViewById(R.id.messageLocationEditText);
				EditText toastieEdit = (EditText) findViewById(R.id.messageToastieEditText);
				EditText bodyEdit = (EditText) findViewById(R.id.messageBodyEditText);

				String question = questionEdit.getText().toString();
				String location = locationEdit.getText().toString();
				String toastie = toastieEdit.getText().toString();
				String body = bodyEdit.getText().toString();
				// make the task to upload the text
				ProgressDialog mProgress = new ProgressDialog(this);
				mProgress.setMessage(getString(R.string.progress_uploading));
				UploadTextTask uploader = new UploadTextTask(text, mProgress, this, question, location, toastie, body) {

					@Override
					protected void onPostExecute(Void output) {
						super.onPostExecute(output);
						SmsReviewActivity.this.finish();
					}

				};
				uploader.execute();
			} else {
				// Run the account picker
				AuthManager.setAuthReason(AuthManager.UPLOADING);
				AuthManager.chooseAccount(this);
			}
		}

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
				} else {
					// Successful authorisation, try upload again.
					uploadMessage();
				}
				break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	public void undoChanges(View v){
		performDefaultSplit();
	}

	public void discardText(View v){
		new AlertDialog.Builder(this)
				.setTitle(R.string.discard)
				.setMessage(R.string.confirm_choice)
				.setPositiveButton(R.string.discard, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SmsList pendingList = SmsList.getPendingList();
						SmsList uploadedList = SmsList.getUploadedList();
						if(pendingList.contains(text)){
							pendingList.removeText(text);
							new Settings(SmsReviewActivity.this).savePendingTextsList();
							Notifications.updateNotification(SmsReviewActivity.this);
						} else if(uploadedList.contains(text)){
							uploadedList.removeText(text);
							new Settings(SmsReviewActivity.this).saveUploadedTextsList();
						}
						SmsReviewActivity.this.finish();
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
