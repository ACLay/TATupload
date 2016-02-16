package uk.org.sucu.tatupload.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import uk.org.sucu.tatupload.R;
import uk.org.sucu.tatupload.Settings;
import uk.org.sucu.tatupload.parse.Parameters;

public class TutorialActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial);
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
