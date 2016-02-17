package uk.org.sucu.tatupload.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import java.io.IOException;

import uk.org.sucu.tatupload.Notifications;
import uk.org.sucu.tatupload.R;
import uk.org.sucu.tatupload.Settings;
import uk.org.sucu.tatupload.message.SmsList;
import uk.org.sucu.tatupload.message.Text;
import uk.org.sucu.tatupload.parse.Parser;

public class UploadTextTask extends AsyncTask<Void, Void, Void> {

    private Exception mLastError = null;

    private ProgressDialog mProgress;
    private Activity mActivity;

    private Text message;

    private boolean preParsed;
    private String question, location, toastie, body;

    public UploadTextTask(Text message, ProgressDialog progress, Activity activity){

        mProgress = progress;
        mActivity = activity;

        this.message = message;
        preParsed = false;
    }

    public UploadTextTask(Text message, ProgressDialog progress, Activity activity, String question, String location, String toastie, String body){
        mProgress = progress;
        mActivity = activity;

        this.message = message;
        preParsed = true;

        this.question = question;
        this.location = location;
        this.toastie = toastie;
        this.body = body;
    }

    /**
     * Background task to call Google Apps Script Execution API.
     * @param params no parameters needed for this task.
     */
    @Override
    protected Void doInBackground(Void... params) {
        if(preParsed){
            String time = Parser.timeStampToString(message.getTimestamp());
            try{
                ApiAccessor.uploadText(message.getNumber(),question,location, toastie,body,time);
            } catch (IOException | GoogleAuthException e) {
                mLastError = e;
                cancel(true);
            }
        } else {
            try {
                ApiAccessor.uploadText(message);
            } catch (IOException | GoogleAuthException e) {
                mLastError = e;
                cancel(true);
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        mProgress.show();
    }

    @Override
    protected void onPostExecute(Void output) {
        mProgress.hide();
        // Set the text as uploaded if not already
        Toast.makeText(mActivity, R.string.text_upload_success, Toast.LENGTH_SHORT).show();

        SmsList pendingList = SmsList.getPendingList();
        if (pendingList.contains(message)) {
            Settings settings = new Settings(mActivity);
            pendingList.removeText(message);
            settings.savePendingTextsList();
            if (settings.getStoringProcesseds()) {
                SmsList.getUploadedList().addText(message);
                settings.saveUploadedTextsList();
            }
        }

        Notifications.updateNotification(mActivity);
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
