package uk.org.sucu.tatupload.activity;

import uk.org.sucu.tatupload.NetCaller;
import uk.org.sucu.tatupload.R;
import uk.org.sucu.tatupload.TatUploadApplication;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
	
	public void testBrowser(View v){
		Uri uri = Uri.parse(getString(R.string.scriptURL)+"?action=test");
		NetCaller.callScriptWithChoice(uri, this);
	}
	
	public void done(View v){
		//update the saved value of seen tutorial version
		((TatUploadApplication)getApplication()).setTutorialVersionShown(MainActivity.TUTORIAL_VERSION);
		//relaunch the main activity
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		this.finish();
	}
}