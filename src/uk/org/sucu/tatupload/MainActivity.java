package uk.org.sucu.tatupload;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final String formID = "1ZqigjAlh14tcF-68uN95x0VZL-ckFrcsjRRF2zZDB5Y";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		int rot = getResources().getConfiguration().orientation % 2;
		
		TextView text = (TextView) findViewById(R.id.processingTextView);
		text.setText(Integer.valueOf(rot).toString());
		
		if(rot == 0){//if device is landscape
			//change the orientation so the incoming messages are queued next to, not below, the controls
			LinearLayout layout = (LinearLayout) findViewById(R.id.main_layout);
			layout.setOrientation(LinearLayout.HORIZONTAL);
		}
		
	}

	public void testUpload(){
		SmsReciever rec = new SmsReciever();
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
