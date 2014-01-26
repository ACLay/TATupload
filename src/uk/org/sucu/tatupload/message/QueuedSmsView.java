package uk.org.sucu.tatupload.message;

import android.content.Context;
import android.telephony.SmsMessage;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QueuedSmsView extends LinearLayout {

	
	private SmsMessage sms;
	
	public QueuedSmsView(SmsMessage text, Context context){
		super(context);
		sms = text;
		
		this.setOrientation(VERTICAL);
		
		LinearLayout header = new LinearLayout(context);
		TextView timeText = new TextView(context);
		TextView numberText = new TextView(context);
		TextView messageText = new TextView(context);
		
		Parser p = new Parser();
		timeText.setText(p.timeStampToString(sms.getTimestampMillis()));
		numberText.setText(sms.getOriginatingAddress());
		messageText.setText(sms.getMessageBody());
		
		header.addView(timeText);
		header.addView(numberText);
		this.addView(header);
		this.addView(messageText);
		
	}
	
	public SmsMessage getSMS(){
		return sms;
	}
	
}
