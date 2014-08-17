package uk.org.sucu.tatupload.activity;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import uk.org.sucu.tatupload.BrowserAccessor;
import uk.org.sucu.tatupload.NetCaller;
import uk.org.sucu.tatupload.R;
import uk.org.sucu.tatupload.TatUploadApplication;
import uk.org.sucu.tatupload.parse.Parser;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SetupActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_layout);
		EditText nameBox = (EditText) findViewById(R.id.formNameEditText);
		//generate a recommended form name based on the date.
		Date d = new Date(Calendar.getInstance().getTimeInMillis());
		String date = DateFormat.getDateInstance().format(d);
		nameBox.setText(date + " " + getString(R.string.text_a_toastie));
	}


	public void startTat(View v){
		//if the browser is unusable, get them to choose a new one
		if(!BrowserAccessor.usable(this)){
			BrowserAccessor.openBrowserChoicePopup(this, false);
		}
		
		if(NetCaller.isOnlineWithErrorBox(this)){

			EditText formNameEdit = (EditText) findViewById(R.id.formNameEditText);
			String formName = formNameEdit.getText().toString();

			Uri uri = Parser.createNewFormUri(formName, this);
			NetCaller.callScript(uri, this);

			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			
			((TatUploadApplication) getApplication()).setProcessingTexts(true);

			this.finish();
		}
	}

	public void cancel(View v){
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		this.finish();
	}

}
