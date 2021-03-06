package uk.org.sucu.tatupload2.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import java.util.Collection;
import java.util.HashMap;

import uk.org.sucu.tatupload2.Notifications;
import uk.org.sucu.tatupload2.Settings;
import uk.org.sucu.tatupload2.network.BackgroundUploadTask;
import uk.org.sucu.tatupload2.network.NetManager;

public class SmsReceiver extends BroadcastReceiver{

	//TODO option to upload names from contacts with messages
	@Override
	public void onReceive(Context context, Intent intent) {
		//only proceed if we're processing
		Settings settings = new Settings(context);
		if(settings.getProcessingTexts()){

			//get the SMS message passed in
			Bundle bundle = intent.getExtras();
			if (bundle != null){
				//retrieve the received SMS messages
				SmsMessage[] msgs;
				// A new method to read SMS' was introduced in kitkat, and the old one deprecated in marshmallow
				if (Build.VERSION.SDK_INT >= 19) { //KITKAT
					msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
				} else {
					Object pdus[] = (Object[]) bundle.get("pdus");
					if(pdus == null){
						return;
					}
					msgs = new SmsMessage[pdus.length];
					for(int i = 0; i < pdus.length; i++) {
						//noinspection deprecation
						msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
					}
				}

				HashMap<String,Text> numberBodyMap = new HashMap<String,Text>();
				
				for (int i = 0; i < msgs.length; i++){
					//if 2 texts in the same receive are from the same number, merge them
					String number = msgs[i].getOriginatingAddress();
					if(numberBodyMap.containsKey(number)){
						Text currentMsg = numberBodyMap.get(number);
						currentMsg.appendBody(msgs[i].getMessageBody());
					} else {
						numberBodyMap.put(number, new Text(msgs[i]));
					}
				}

				Collection<Text> texts = numberBodyMap.values();

				//process the message!
				if(settings.getAutoQueueTexts()) {
					//queue it if set to confirm before upload, the browser is not set/is uninstalled, or there's no network connection
					queueMessages(texts, context);
				} else if(!NetManager.isOnline(context)){
					//TODO PLAY ALARM WHEN TRYING TO UPLOAD AND NO NETWORK!!!
					queueMessages(texts, context);
				} else {
					for(Text m : texts){
						if(m != null){
							new BackgroundUploadTask(m, context).execute();
						}
					}
				}

			}
		}

	}
	
	private void queueMessages(Collection<Text> messages, Context context){
		SmsList.getPendingList().addTexts(messages);
		new Settings(context).savePendingTextsList();
		Notifications.updateNotification(context);
	}

}
