package uk.org.sucu.tatupload.message;

import uk.org.sucu.tatupload.MainActivity;
import uk.org.sucu.tatupload.TatUploadApplication;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import android.telephony.SmsMessage;
import android.util.Log;

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
			String str = "";
			if (bundle != null){
				//retrieve the SMS message received
				Object[] pdus = (Object[]) bundle.get("pdus");
				msgs = new SmsMessage[pdus.length];
				for (int i=0; i<msgs.length; i++){
					//createFromPdu to be deprecated soon... new method added in 4.4 KitKat
					msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);

					str += "SMS from " + msgs[i].getOriginatingAddress();
					str += " :";
					str += msgs[i].getMessageBody().toString();
					str += "\n";
				}
				//display the new SMS message
				Log.i("message",str);


				//process the message!
				if(TatUploadApplication.getConfirmSplit()){
					activity.addMessages(msgs);
				} else {
					for(SmsMessage m : msgs){
						autoProcess(m, context);
					}
				}

			}
		}

	}

	public void autoProcess(SmsMessage text, Context context){

		Parser p = new Parser();
		String body = text.getMessageBody();
		String number = text.getOriginatingAddress();
		String formID = TatUploadApplication.getFormID();

		String question = "";
		for(String s : p.getQuestion(body)){
			question += s;
		}

		String location = "";
		for(String s : p.getLocation(body)){
			location += s;
		}

		String toastie = "";
		for(String s : p.getFlavours(body)){
			toastie += s + " ";
		}
		//keeping the original message may be useful?

		//TODO this code block is identical to one in SmsReviewActivity
		Uri uri = p.createUploadUri(formID, number, question, location, toastie, body);

		//While it would be nice to handle everything in-app, it seems for the time being I'll need to go via the browser.
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
		//open all this apps requests in the same tab, prevents new ones with each call
		browserIntent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
		//allows a new task to be started outside of a current task
		browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(browserIntent);

	}


}
