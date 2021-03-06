package uk.org.sucu.tatupload2.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import uk.org.sucu.tatupload2.R;

/**
 * An asynchronous task that handles the Google Apps Script Execution API call.
 * Placing the API calls in their own task ensures the UI stays responsive.
 */
public class MakeSheetTask extends AsyncTask<Void, Void, Void> {

    private Exception mLastError = null;

    ProgressDialog mProgress;
    Activity mActivity;

    private String sheetName;

    public MakeSheetTask(String sheetName, ProgressDialog progress, Activity activity){

        mProgress = progress;
        mActivity = activity;

        this.sheetName = sheetName;

    }

    @Override
    protected void onPreExecute() {
        mProgress.show();
    }

    /**
     * Background task to call Google Apps Script Execution API.
     * @param params no parameters needed for this task.
     */
    @Override
    protected Void doInBackground(Void... params) {
        try {
            ApiAccessor.createSheet(sheetName);
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void output) {
        mProgress.hide();
        Toast.makeText(mActivity, R.string.sheet_create_success, Toast.LENGTH_SHORT).show();
        // extend method when used and update the UI as needed (message queues etc)
    }

    @Override
    protected void onCancelled() {
        mProgress.hide();
        if (mLastError != null) {
            if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                AuthManager.showGooglePlayServicesAvailabilityErrorDialog(
                        ((GooglePlayServicesAvailabilityIOException) mLastError)
                                .getConnectionStatusCode(), mActivity);
            } else if (mLastError instanceof UserRecoverableAuthIOException) {
                AuthManager.setAuthReason(AuthManager.CREATING);
                mActivity.startActivityForResult(
                        ((UserRecoverableAuthIOException) mLastError).getIntent(),
                        AuthManager.REQUEST_AUTHORIZATION);
            } else {
                String errorMsg = mActivity.getString(R.string.sheet_create_error) + mLastError.getMessage();
                Toast.makeText(mActivity, errorMsg, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mActivity, R.string.sheet_create_cancel, Toast.LENGTH_SHORT).show();
        }
    }
}
