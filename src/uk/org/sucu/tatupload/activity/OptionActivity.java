package uk.org.sucu.tatupload.activity;

import uk.org.sucu.tatupload.BrowserAccessor;
import uk.org.sucu.tatupload.R;
import android.os.Bundle;
import android.preference.Preference;
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

		Preference restore = findPreference(getString(R.string.browser));
		restore.setOnPreferenceClickListener(new OnPreferenceClickListener(){
			@Override
			public boolean onPreferenceClick(Preference arg0) {
				BrowserAccessor.openBrowserChoicePopup(OptionActivity.this, true);
				return true;
			}

		});

	}

}
