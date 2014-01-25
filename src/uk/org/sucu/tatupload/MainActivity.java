package uk.org.sucu.tatupload;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.Window;

public class MainActivity extends Activity {

	private static String formID = "1ZqigjAlh14tcF-68uN95x0VZL-ckFrcsjRRF2zZDB5Y";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		SmsReciever rec = new SmsReciever();
		Uri uri = rec.prepareURI(formID, 
				"42",
				"I am in the stags. I want a cheese and ham toastie. Why are other religions wrong?");
		Log.i("message",uri.toString());
		
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(browserIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public static String getFormID(){
		return formID;
	}

}
