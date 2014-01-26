package uk.org.sucu.tatupload;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

	private static final String formID = "1ZqigjAlh14tcF-68uN95x0VZL-ckFrcsjRRF2zZDB5Y";
	
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
		
	}

	public void testUpload(){
		SmsReceiver rec = new SmsReceiver();
		Uri uri = rec.prepareURI(formID, 
				"0123456789",
				"I am in hartley library. I want a ham and tomato toastie. What is God's favourite cake?");
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
