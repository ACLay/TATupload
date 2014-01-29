package uk.org.sucu.tatupload;

import java.util.ArrayList;

import uk.org.sucu.tatupload.views.QueuedSmsView;
import android.content.Context;
import android.telephony.SmsMessage;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class MessageArrayAdapter extends ArrayAdapter<SmsMessage> {

	public MessageArrayAdapter(Context context, int resource, ArrayList<SmsMessage> messages) {
		super(context, resource, messages);
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		return new QueuedSmsView(getItem(position), getContext());
	}
	
	
}
