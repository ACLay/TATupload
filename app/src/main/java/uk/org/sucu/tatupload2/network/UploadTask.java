package uk.org.sucu.tatupload2.network;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;

import java.io.IOException;

import uk.org.sucu.tatupload2.Notifications;
import uk.org.sucu.tatupload2.R;
import uk.org.sucu.tatupload2.Settings;
import uk.org.sucu.tatupload2.message.SmsList;
import uk.org.sucu.tatupload2.message.Text;
import uk.org.sucu.tatupload2.parse.Parser;

public class UploadTask extends AsyncTask<Void, Void, Void> {

    protected Exception mLastError = null;

    protected Context mContext;

    protected Text message;

    private boolean preParsed;
    private String question, location, toastie, body;

    public UploadTask(Text message, Context context){

        mContext = context;

        this.message = message;
        preParsed = false;
    }

    public UploadTask(Text message, Context activity, String question, String location, String toastie, String body){
        mContext = activity;

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

    }

    @Override
    protected void onPostExecute(Void output) {
        // Set the text as uploaded if not already
        Toast.makeText(mContext, R.string.text_upload_success, Toast.LENGTH_SHORT).show();

        Settings settings = new Settings(mContext);

        SmsList pendingList = SmsList.getPendingList();
        if (pendingList.contains(message)) {
            pendingList.removeText(message);
            settings.savePendingTextsList();
        }
        if (settings.getStoringProcesseds()) {
            SmsList uploadedList = SmsList.getUploadedList();
            if (!uploadedList.contains(message)){
                SmsList.getUploadedList().addText(message);
                settings.saveUploadedTextsList();
            }
        }

        Notifications.updateNotification(mContext);
    }

    @Override
    protected void onCancelled() {

    }
}
