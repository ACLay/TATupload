package uk.org.sucu.tatupload;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import uk.org.sucu.tatupload.message.Parser;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
		}
		
	}
	
	public String processMessage(String formId, String number, String message){
		//https://script.google.com/macros/s/AKfycbzfPd5U7tbyOmK8EERxB8LPn53CzLy_nzXzAu2jb2_fYC8V_aof/exec
		Parser p = new Parser();
		String question = p.getQuestion(message).toString();
		String location = p.getLocation(message).toString();
		String toastie  = p.getFlavours(message).toString();
		
		try{
			HttpClient hc = new DefaultHttpClient();
			String url = "https://script.google.com/macros/s/AKfycbzfPd5U7tbyOmK8EERxB8LPn53CzLy_nzXzAu2jb2_fYC8V_aof/exec?formID=" + formId +
					"&number=" + number +
					"&question=" + question +
					"&location=" + location +
					"&toastie=" + toastie +
					"&SMS=" + message;
			HttpGet get = new HttpGet(url);
			HttpResponse rp = hc.execute(get);
			if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				String result = EntityUtils.toString(rp.getEntity());
				return result;
			}
			
		} catch (Exception e){
			
		}
		
		return "unable to do thing!";
	}

}
