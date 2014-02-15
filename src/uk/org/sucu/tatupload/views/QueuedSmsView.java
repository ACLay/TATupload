package uk.org.sucu.tatupload.views;

import uk.org.sucu.tatupload.message.Text;
import uk.org.sucu.tatupload.parse.Parser;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QueuedSmsView extends LinearLayout {

	
	private Text sms;
	
	public QueuedSmsView(Text text, Context context){
		super(context);
		sms = text;
		
		this.setOrientation(VERTICAL);
		
		LinearLayout header = new LinearLayout(context);
		TextView timeText = new TextView(context);
		TextView numberText = new TextView(context);
		TextView messageText = new TextView(context);
		
		timeText.setText(Parser.timeStampToString(sms.getTimestamp()));
		numberText.setText(sms.getNumber());
		messageText.setText(sms.getBody());
		
		header.addView(timeText);
		header.addView(numberText);
		this.addView(header);
		this.addView(messageText);
		
	}
	
	public Text getSMS(){
		return sms;
	}
	
}
