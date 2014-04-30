package uk.org.sucu.tatupload;

import uk.org.sucu.tatupload.parse.Parameters;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

@SuppressWarnings("deprecation") //it uses methods replaced within new PreferenceFragments in honeycomb
public class OptionActivity extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);


	}

	public void onStart(){
		super.onStart();
		//TODO only allow restore to default when processing is turned off.
		final int ver_seen = SettingsAccessor.getTutorialVersionSeen(this);

		final PreferenceActivity c = this;

		Preference restore = findPreference(getString(R.string.restore_defaults));
		restore.setOnPreferenceClickListener(new OnPreferenceClickListener(){
			@Override
			public boolean onPreferenceClick(Preference arg0) {
				SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(c);
				//clear the user settings from the file, keeping the tutorial version seen
				sharedPref.edit()
					.clear()
					.putInt(getString(R.string.tutorial_ver_key), ver_seen)
					.commit();
				Parameters.restoreDefaults();//update the values kept in memory
				//remake the settings screen so the new values are shown
				c.finish();
				c.startActivity(c.getIntent());
				return true;
			}

		});

	}

}
