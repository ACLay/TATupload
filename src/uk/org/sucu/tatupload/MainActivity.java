package uk.org.sucu.tatupload;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SmsReciever rec = new SmsReciever();
		rec.processMessage("1K_53Zp5cvliE_ZfUiJJ3Qmjw0dB8A7npUm0huHDd8mQ", 
				"42",
				"I'm in the stags. I want a cheese toastie. Why are other religions wrong?");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
