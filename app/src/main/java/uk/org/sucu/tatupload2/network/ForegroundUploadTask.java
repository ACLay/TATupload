package uk.org.sucu.tatupload2.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import uk.org.sucu.tatupload2.R;
import uk.org.sucu.tatupload2.message.Text;

public class ForegroundUploadTask extends UploadTask {

    private ProgressDialog mProgress;
    private Activity mActivity;

    public ForegroundUploadTask(Text message, ProgressDialog progress, Activity activity){
        super(message, activity);
        mProgress = progress;
        mActivity = activity;
    }

    public ForegroundUploadTask(Text message, ProgressDialog progress, Activity activity, String question, String location, String toastie, String body){
        super(message, activity, question, location, toastie, body);
        mProgress = progress;
        mActivity = activity;
    }

    @Override
    protected void onPreExecute() {
        mProgress.show();
    }

    @Override
    protected void onPostExecute(Void output) {
        mProgress.hide();
        super.onPostExecute(output);
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
                AuthManager.setAuthReason(AuthManager.UPLOADING);
                mActivity.startActivityForResult(
                        ((UserRecoverableAuthIOException) mLastError).getIntent(),
                        AuthManager.REQUEST_AUTHORIZATION);
            } else {
                String errorMsg = mActivity.getString(R.string.text_upload_error) + mLastError.getMessage();
                Toast.makeText(mActivity, errorMsg, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mActivity, R.string.text_upload_cancel, Toast.LENGTH_SHORT).show();
        }
    }
}
