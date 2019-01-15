package uk.org.sucu.tatupload2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import uk.org.sucu.tatupload2.R;
import uk.org.sucu.tatupload2.Settings;
import uk.org.sucu.tatupload2.parse.Parameters;

public class TutorialActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial);

		TextView view = (TextView)findViewById(R.id.privacy_policy_text);
		view.setMovementMethod(LinkMovementMethod.getInstance());
	}
	
	public void setupFlavours(View v){
		Intent intent = new Intent(this, ParameterViewActivity.class);
		intent.putExtra(Parameters.PARAMETER, Parameters.FLAVOUR_PARAMETER);
		startActivity(intent);
	}
	
	public void setupLocation(View v){
		Intent intent = new Intent(this, ParameterViewActivity.class);
		intent.putExtra(Parameters.PARAMETER, Parameters.LOCATION_PARAMETER);
		startActivity(intent);
	}
	
	public void done(View v){
		//update the saved value of seen tutorial version
		new Settings(this).setTutorialVersionShown(MainActivity.TUTORIAL_VERSION);
		//relaunch the main activity
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		this.finish();
	}
}
