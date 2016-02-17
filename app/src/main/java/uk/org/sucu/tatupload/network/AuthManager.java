package uk.org.sucu.tatupload.network;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;

import java.util.Arrays;

import uk.org.sucu.tatupload.Settings;

public class AuthManager {

    public static int authReason;
    public static final int UPLOADING = 1610;
    public static final int CREATING = 616;

    public static final int REQUEST_ACCOUNT_PICKER = 1000;
    public static final int REQUEST_AUTHORIZATION = 1001;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;

    private static final String[] SCOPES = { "https://www.googleapis.com/auth/script.storage", "https://www.googleapis.com/auth/spreadsheets" };

    public static GoogleAccountCredential mCredential;

    public static void initializeCredential(Context context){

        // Initialize credentials and service object.
        Settings settings = new Settings(context);
        mCredential = GoogleAccountCredential.usingOAuth2(
                context, Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff())
                .setSelectedAccountName(settings.getPreferredAccount());
    }

    public static void setAccountName(String accountName, Context context){
        mCredential.setSelectedAccountName(accountName);
        Settings settings = new Settings(context);
        settings.setPreferredAccount(accountName);
    }

    public static boolean isAccountSet(){
        return mCredential.getSelectedAccountName() != null;
    }

    /**
     * Starts an activity in Google Play Services so the user can pick an
     * account.
     */
    public static void chooseAccount(Activity activity) {
        activity.startActivityForResult(mCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }

    public static void setAuthReason(int reason){
        authReason = reason;
    }

    /**
     * Check that Google Play services APK is installed and up to date. Will
     * launch an error dialog for the user to update Google Play Services if
     * possible.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    public static boolean isGooglePlayServicesAvailable(Activity activity) {
        final int connectionStatusCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode, activity);
            return false;
        } else if (connectionStatusCode != ConnectionResult.SUCCESS ) {
            return false;
        }
        return true;
    }

    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    public static void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode, Activity activity) {
        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                connectionStatusCode,
                activity,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

}
