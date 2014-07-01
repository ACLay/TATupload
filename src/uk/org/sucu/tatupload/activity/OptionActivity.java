package uk.org.sucu.tatupload.activity;

import uk.org.sucu.tatupload.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;

@SuppressWarnings("deprecation") //it uses methods replaced within new PreferenceFragments in honeycomb
public class OptionActivity extends PreferenceActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
	
}
