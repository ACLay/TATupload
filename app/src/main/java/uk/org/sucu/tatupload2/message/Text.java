package uk.org.sucu.tatupload2.message;

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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Text other = (Text) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		return timestamp == other.timestamp;
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
