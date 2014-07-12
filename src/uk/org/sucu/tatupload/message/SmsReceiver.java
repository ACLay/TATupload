package uk.org.sucu.tatupload.message;

import java.util.Collection;
import java.util.HashMap;

import uk.org.sucu.tatupload.NetCaller;
import uk.org.sucu.tatupload.SettingsAccessor;
import uk.org.sucu.tatupload.parse.Parser;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver{

	//TODO option to upload names from contacts with messages
	@Override
	public void onReceive(Context context, Intent intent) {
		//only proceed if we're processing
		if(SettingsAccessor.getProcessingTexts(context)){

			//get the SMS message passed in
			Bundle bundle = intent.getExtras();
			SmsMessage[] msgs = null;
			if (bundle != null){
				//retrieve the SMS message received
				Object[] pdus = (Object[]) bundle.get("pdus");
				msgs = new SmsMessage[pdus.length];

				HashMap<String,Text> numberBodyMap = new HashMap<String,Text>();
				
				for (int i = 0; i < msgs.length; i++){
					//createFromPdu to be deprecated soon... new method added in 4.4 KitKat
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
				if(SettingsAccessor.getAutoQueueTexts(context)){
					//queue it if we're confirming before upload, or there's no network connection
					queueMessages(texts);
					
				} else if(!NetCaller.isOnline(context)){
					//store current volume and set it to max
					AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
					int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
					int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
					audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, maxVolume, AudioManager.FLAG_PLAY_SOUND);
					//TODO PLAY ALARM WHEN TRYING TO UPLOAD AND NO NETWORK!!!
					Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
					Ringtone r = RingtoneManager.getRingtone(context.getApplicationContext(), soundUri);
					r.play();
					//restore volume to previous level
					audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, currentVolume, AudioManager.FLAG_PLAY_SOUND);
					queueMessages(texts);
				}else {
					for(Text m : texts){
						if(m != null){
							autoProcess(m, context);
						}
					}
				}

			}
		}

	}
	
	private void queueMessages(Collection<Text> messages){
		SmsList.addTexts(messages);
	}

	public void autoProcess(Text text, Context context){

		String body = text.getBody();
		String number = text.getNumber();

		String question = "";
		for(String s : Parser.getQuestion(body)){
			question += s;
		}

		String location = "";
		for(String s : Parser.getLocation(body)){
			location += s;
		}

		String toastie = "";
		for(String s : Parser.getFlavours(body)){
			toastie += s + " ";
		}

		String time = Parser.timeStampToString(text.getTimestamp());

		Uri uri = Parser.createUploadUri(number, question, location, toastie, body, time, context);
		NetCaller.callScript(uri, context);

	}

}
