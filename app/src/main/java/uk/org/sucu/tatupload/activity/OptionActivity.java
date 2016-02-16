package uk.org.sucu.tatupload.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v7.preference.PreferenceFragmentCompat;

import uk.org.sucu.tatupload.Notifications;
import uk.org.sucu.tatupload.R;
import uk.org.sucu.tatupload.network.AuthManager;

public class OptionActivity extends AppCompatActivity {

	public static class MyPreferenceFrag extends PreferenceFragmentCompat {

		@Override
		public void onCreatePreferences(Bundle bundle, String s) {

			addPreferencesFromResource(R.xml.preferences);

			Preference account = findPreference(getString(R.string.account));
			account.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference arg0) {
					AuthManager.chooseAccount(getActivity());
					return true;
				}

			});

			Preference notify = findPreference(getString(R.string.show_notification_key));
			notify.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					Boolean processing = (Boolean) newValue;
					if (processing) {
						Notifications.displayNotification(getContext());
					} else {
						Notifications.hideNotification(getContext());
					}
					return true;
				}

			});
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			Fragment newFragment = new MyPreferenceFrag();
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add(android.R.id.content, newFragment).commit();
		}
	}
	
	public void onStart(){
		super.onStart();


	}

}
