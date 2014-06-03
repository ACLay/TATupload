package uk.org.sucu.tatupload;

import uk.org.sucu.tatupload.parse.Parameters;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

@SuppressWarnings("deprecation") //it uses methods replaced within new PreferenceFragments in honeycomb
public class OptionActivity extends PreferenceActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		Preference restore = findPreference(getString(R.string.restore_defaults));
		restore.setOnPreferenceClickListener(new OnPreferenceClickListener(){
			@Override
			public boolean onPreferenceClick(Preference arg0) {
				resetParameters();
				updateView();
				return true;
			}

		});
	}
	
	private void resetParameters(){
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		//clear the users parameter settings from the file, keeping the tutorial version seen
		sharedPref.edit()
			.remove(Parameters.FLAVOUR_PARAMETER)
			.remove(Parameters.LOCATION_PARAMETER)
			.remove(Parameters.QUESTION_PARAMETER)
			.commit();
		Parameters.restoreDefaults();//update the values kept in memory
	}
	
	//reload the parameters into the view.
	private void updateView(){
		CheckBoxPreference autoQueue = (CheckBoxPreference) findPreference(getString(R.string.confirm_split_key));
		autoQueue.setChecked(SettingsAccessor.getAutoQueueTexts(this));
	}
	
}
