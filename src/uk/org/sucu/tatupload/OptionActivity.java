package uk.org.sucu.tatupload;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class OptionActivity extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        
        
    }

}
