package uk.org.sucu.tatupload.message;

import java.io.Serializable;

import android.telephony.SmsMessage;

public class Text implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4990983582626547410L;
	private String number;
	private String body;
	private long timestamp;
	
	public Text(String number, String body, long timestamp){
		this.number = number;
		this.body = body;
		this.timestamp = timestamp;
	}
	
	public Text(SmsMessage msg){
		number = msg.getOriginatingAddress();
		body = msg.getMessageBody();
		timestamp = msg.getTimestampMillis();
	}
	
	public String getNumber(){
		return number;
	}
	
	public String getBody(){
		return body;
	}
	
	public long getTimestamp(){
		return timestamp;
	}
	
	public void appendBody(String extra){
		body += extra;
	}
}
