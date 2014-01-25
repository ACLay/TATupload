package uk.org.sucu.tatupload;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import uk.org.sucu.tatupload.message.Parser;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReciever extends BroadcastReceiver{


	@Override
	public void onReceive(Context context, Intent intent) {
		
		//get the SMS message passed in
		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;
		String str = "";
		if (bundle != null){
			//retrieve the SMS message received
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			for (int i=0; i<msgs.length; i++){
				msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);


				str += "SMS from " + msgs[i].getOriginatingAddress();
				str += " :";
				str += msgs[i].getMessageBody().toString();
				str += "\n";
			}
			//display the new SMS message
			Log.i("message",str);
			String formID = MainActivity.getFormID();
			Uri uri = prepareURI(formID,"0","message");

			//While it would be nice to handle everything in-app, it seems for the time being I'll need to go via the browser.
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
			//open all this apps requests in the same tab, prevents new ones with each call
			browserIntent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
			context.startActivity(browserIntent);

		}

	}

	public Uri prepareURI(String formId, String number, String message){
		//https://script.google.com/macros/s/AKfycbzfPd5U7tbyOmK8EERxB8LPn53CzLy_nzXzAu2jb2_fYC8V_aof/exec
		Parser p = new Parser();
		String question = "";
		for(String s : p.getQuestion(message)){
			question += s;
		}

		String location = "";
		for(String s : p.getLocation(message)){
			location += s;
		}

		String toastie = "";
		for(String s : p.getFlavours(message)){
			toastie += s;
		}
		//keeping the original message may be useful?
		String encodedMessage = "";
		try{
			question = URLEncoder.encode(question, "utf-8");
			location = URLEncoder.encode(location, "utf-8");
			toastie = URLEncoder.encode(toastie, "utf-8");
			encodedMessage = URLEncoder.encode(message, "utf-8");
		} catch (UnsupportedEncodingException e){

		}


		//url of the upload script
		String url = "https://script.google.com/macros/s/AKfycbzfPd5U7tbyOmK8EERxB8LPn53CzLy_nzXzAu2jb2_fYC8V_aof/exec";

		Map<String, String> params = new HashMap<String, String>();
		params.put("formID", formId);
		params.put("number", number);
		params.put("question", question);
		params.put("location", location);
		params.put("toastie", toastie);
		params.put("SMS", encodedMessage);

		StringBuilder bodyBuilder = new StringBuilder();
		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
		bodyBuilder.append(url).append("?");

		while (iterator.hasNext()) {
			Entry<String, String> param = iterator.next();
			bodyBuilder.append(param.getKey()).append('=').append(param.getValue());
			if (iterator.hasNext()) {
				bodyBuilder.append('&');
			}
		}

		String request = bodyBuilder.toString();
		Log.i("parameters", request);

		return Uri.parse(request);

	}



}








