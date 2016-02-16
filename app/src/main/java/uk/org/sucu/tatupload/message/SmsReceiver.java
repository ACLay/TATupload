package uk.org.sucu.tatupload.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.google.android.gms.auth.GoogleAuthException;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import uk.org.sucu.tatupload.Notifications;
import uk.org.sucu.tatupload.Settings;
import uk.org.sucu.tatupload.network.ApiAccessor;
import uk.org.sucu.tatupload.network.NetManager;

public class SmsReceiver extends BroadcastReceiver{

	//TODO option to upload names from contacts with messages
	@Override
	public void onReceive(Context context, Intent intent) {
		//only proceed if we're processing
		Settings settings = new Settings(context);
		if(settings.getProcessingTexts()){

			//get the SMS message passed in
			Bundle bundle = intent.getExtras();
			SmsMessage[] msgs = null;
			if (bundle != null){
				//retrieve the SMS message received
				Object[] pdus = (Object[]) bundle.get("pdus");
				msgs = new SmsMessage[pdus.length];

				HashMap<String,Text> numberBodyMap = new HashMap<String,Text>();
				
				for (int i = 0; i < msgs.length; i++){
					//createFromPdu to be deprecated soon
					msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
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
							autoProcess(m, context);
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

	private void autoProcess(Text text, Context context){

		Settings settings = new Settings(context);

		try {
			ApiAccessor.uploadText(text);
		} catch (IOException | GoogleAuthException e) {
			SmsList.getPendingList().addText(text);
			settings.savePendingTextsList();
			Notifications.updateNotification(context);
			return;
		}


		if(settings.getStoringProcesseds()){
			SmsList.getUploadedList().addText(text);
			settings.saveUploadedTextsList();
		}
		
	}

}
