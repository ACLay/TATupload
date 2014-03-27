package uk.org.sucu.tatupload;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import uk.org.sucu.tatupload.parse.Parser;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class SetupActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_layout);
		EditText nameBox = (EditText) findViewById(R.id.editText1);
		//generate a reccomended form name based on the date.
		Date d = new Date(Calendar.getInstance().getTimeInMillis());
		String date = DateFormat.getDateInstance().format(d);
		nameBox.setText(date + " " + getString(R.string.text_a_toastie));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setup, menu);
		return true;
	}

	public void startTat(View v){

		if(NetCaller.isOnlineWithErrorBox(this)){

			((TatUploadApplication) getApplication()).setProcessingTexts(true);

			EditText formNameEdit = (EditText) findViewById(R.id.editText1);
			String formName = formNameEdit.getText().toString();
			((TatUploadApplication) getApplication()).setFormName(formName);

			Uri uri = Parser.createNewFormUri(formName);
			NetCaller.callScript(uri, this);

			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			
			this.finish();
		}
	}

	public void cancel(View v){
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		this.finish();
	}

}
