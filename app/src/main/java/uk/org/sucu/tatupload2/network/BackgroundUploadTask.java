package uk.org.sucu.tatupload2.network;

import android.content.Context;

import uk.org.sucu.tatupload2.Notifications;
import uk.org.sucu.tatupload2.Settings;
import uk.org.sucu.tatupload2.message.SmsList;
import uk.org.sucu.tatupload2.message.Text;

public class BackgroundUploadTask extends UploadTask {

    public BackgroundUploadTask(Text message, Context context){
        super(message, context);
    }

    public BackgroundUploadTask(Text message, Context context, String question, String location, String toastie, String body){
        super(message, context, question, location, toastie, body);
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(Void output) {
        super.onPostExecute(output);
    }

    @Override
    protected void onCancelled() {
        Settings settings = new Settings(mContext);

        SmsList.getPendingList().addText(message);
        settings.savePendingTextsList();
        Notifications.updateNotification(mContext);
    }
}
