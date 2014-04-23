package uk.org.sucu.tatupload;

import uk.org.sucu.tatupload.parse.Parameters;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		CheckBox confirmSplit = (CheckBox) findViewById(R.id.checkBox1);
		confirmSplit.setChecked(SettingsAccessor.getAutoQueueTexts(this));
		
		TextView formLabel = (TextView) findViewById(R.id.textView1);
		
		formLabel.setText(getString(R.string.form_name) + SettingsAccessor.getFormName(this));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings_, menu);
		return true;
	}
	
	public void toggleAutoQueue(View v){
		CheckBox confirmSplitBox = (CheckBox) v;
		boolean confirm = confirmSplitBox.isChecked();
		((TatUploadApplication) getApplication()).setConfirmSplit(confirm);
	}
	
	public void showFlavourParams(View v){
		Intent intent = new Intent(this, ParameterViewActivity.class);
		intent.putExtra(Parameters.PARAMETER, Parameters.FLAVOUR_PARAMETER);
		startActivity(intent);
	}
	
	public void showLocationParams(View v){
		Intent intent = new Intent(this, ParameterViewActivity.class);
		intent.putExtra(Parameters.PARAMETER, Parameters.LOCATION_PARAMETER);
		startActivity(intent);
	}
	
	public void showQuestionParams(View v){
		Intent intent = new Intent(this, ParameterViewActivity.class);
		intent.putExtra(Parameters.PARAMETER, Parameters.QUESTION_PARAMETER);
		startActivity(intent);
	}
	
	public void restoreDefaults(View v){
		Parameters.restoreDefaults();
		Parameters.saveParameter(Parameters.FLAVOUR_PARAMETER,this);
		Parameters.saveParameter(Parameters.LOCATION_PARAMETER,this);
		Parameters.saveParameter(Parameters.QUESTION_PARAMETER,this);
	}
	
}
