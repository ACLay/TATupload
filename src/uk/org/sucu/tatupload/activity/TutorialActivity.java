package uk.org.sucu.tatupload.activity;

import uk.org.sucu.tatupload.BrowserAccessor;
import uk.org.sucu.tatupload.R;
import uk.org.sucu.tatupload.TatUploadApplication;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TutorialActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial);
	}
	
	public void setupFlavours(View v){
		Intent intent = new Intent(this, ParameterViewActivity.class);
		intent.putExtra(getString(R.string.parameter_indicator), getString(R.string.flavour_parameter));
		startActivity(intent);
	}
	
	public void setupLocation(View v){
		Intent intent = new Intent(this, ParameterViewActivity.class);
		intent.putExtra(getString(R.string.parameter_indicator), getString(R.string.location_parameter));
		startActivity(intent);
	}
	
	public void setBrowser(View v){
		BrowserAccessor.openBrowserChoicePopup(this, false);
	}
	
	public void done(View v){
		
		if(BrowserAccessor.browserSet(this)){
			//update the saved value of seen tutorial version
			((TatUploadApplication)getApplication()).setTutorialVersionShown(MainActivity.TUTORIAL_VERSION);
			//relaunch the main activity
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			this.finish();
		} else {
			//show a 'you must set a browser' popup if no browser is set.
			new AlertDialog.Builder(this)
			.setTitle("TATupload")  
			.setMessage("You must set a browser for TATupload to use before continuing.")
			.setPositiveButton("Okay", null)
			.create()
			.show();
		}
	}
}
