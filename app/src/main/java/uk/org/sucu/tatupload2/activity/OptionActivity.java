package uk.org.sucu.tatupload2.activity;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import uk.org.sucu.tatupload2.Notifications;
import uk.org.sucu.tatupload2.R;
import uk.org.sucu.tatupload2.Settings;
import uk.org.sucu.tatupload2.network.AuthManager;

public class OptionActivity extends AppCompatActivity {

	private String currentAccount;

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

		currentAccount = new Settings(this).getPreferredAccount();

		if (savedInstanceState == null) {
			Fragment newFragment = new MyPreferenceFrag();
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add(android.R.id.content, newFragment).commit();
		}
	}

	/**
	 * Called when an activity launched here (specifically, AccountPicker
	 * and authorization) exits, giving you the requestCode you started it with,
	 * the resultCode it returned, and any additional data from it.
	 * @param requestCode code indicating which activity result is incoming.
	 * @param resultCode code indicating the result of the incoming
	 *     activity result.
	 * @param data Intent (containing result data) returned by incoming
	 *     activity result.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode) {
			case AuthManager.REQUEST_GOOGLE_PLAY_SERVICES:
				if (resultCode != RESULT_OK) {
					AuthManager.isGooglePlayServicesAvailable(this);
				}
				break;
			case AuthManager.REQUEST_ACCOUNT_PICKER:
				if (resultCode == RESULT_OK && data != null &&
						data.getExtras() != null) {
					String accountName =
							data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
					if (accountName != null) {
						AuthManager.setAccountName(accountName, this);
						successfulAuthorisation();
					}
				} else if (resultCode == RESULT_CANCELED) {
					Toast.makeText(this, R.string.no_account_selected, Toast.LENGTH_SHORT).show();
				}
				break;
			case AuthManager.REQUEST_AUTHORIZATION:
				if (resultCode != RESULT_OK) {
					AuthManager.chooseAccount(this);
				} else {
					successfulAuthorisation();
				}
				break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void successfulAuthorisation(){
		Settings settings = new Settings(this);
		String newAccount = settings.getPreferredAccount();
		String prevAccount = currentAccount;
		// if the account changes, stop TAT
		if (prevAccount != null && newAccount != null && !newAccount.equals(prevAccount)) {
				settings.setProcessingTexts(false);
		}
		currentAccount = newAccount;
	}

}
