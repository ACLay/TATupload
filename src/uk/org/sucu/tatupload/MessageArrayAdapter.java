package uk.org.sucu.tatupload;

import java.util.List;

import uk.org.sucu.tatupload.message.Text;
import uk.org.sucu.tatupload.views.QueuedSmsView;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class MessageArrayAdapter extends ArrayAdapter<Text> {

	public MessageArrayAdapter(Context context, int resource, List<Text> messages) {
		super(context, resource, messages);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		return new QueuedSmsView(getItem(position), getContext());
	}
	
	
}
