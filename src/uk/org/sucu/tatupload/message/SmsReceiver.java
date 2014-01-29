package uk.org.sucu.tatupload.message;

import java.util.Collection;
import java.util.HashMap;

import uk.org.sucu.tatupload.MainActivity;
import uk.org.sucu.tatupload.TatUploadApplication;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver{

	private static MainActivity activity;

	public static void giveMainActivity(MainActivity main){
		activity = main;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		//only proceed if we're processing
		if(TatUploadApplication.getProcessingTexts()){

			//get the SMS message passed in
			Bundle bundle = intent.getExtras();
			SmsMessage[] msgs = null;
			if (bundle != null){
				//retrieve the SMS message received
				Object[] pdus = (Object[]) bundle.get("pdus");
				msgs = new SmsMessage[pdus.length];

				//TODO MULTIPART TEXTS
				HashMap<String,Text> numberBodyMap = new HashMap<String,Text>();
				//TODO use uk time
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
				if(TatUploadApplication.getConfirmSplit() || !activity.isOnline()){
					//queue it if we're confirming before upload, or there's no network connection
					activity.addMessages(texts);
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

	public void autoProcess(Text text, Context context){

		String body = text.getBody();
		String number = text.getNumber();
		String formName = TatUploadApplication.getFormName();

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
		//keeping the original message may be useful?

		//TODO this code block is identical to one in SmsReviewActivity
		Uri uri = Parser.createUploadUri(formName, number, question, location, toastie, body);

		//While it would be nice to handle everything in-app, it seems for the time being I'll need to go via the browser.
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
		//open all this apps requests in the same tab, prevents new ones with each call
		browserIntent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
		//allows a new task to be started outside of a current task
		browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(browserIntent);

	}


}
