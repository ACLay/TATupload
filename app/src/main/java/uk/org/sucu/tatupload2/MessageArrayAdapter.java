package uk.org.sucu.tatupload2;

import java.util.List;

import uk.org.sucu.tatupload2.message.Text;
import uk.org.sucu.tatupload2.parse.Parser;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MessageArrayAdapter extends ArrayAdapter<Text> {

	public static class ViewHolder {
		private TextView timeText;
		private TextView numberText;
		private TextView bodyText;
		private Text sms;
		
		public ViewHolder(TextView timeTV, TextView numberTV, TextView bodyTV){
			timeText = timeTV;
			numberText = numberTV;
			bodyText = bodyTV;
		}
		
		protected void setText(Text sms){
			this.sms = sms;
		}
		
		public Text getText(){
			return sms;
		}
	}
	
	public MessageArrayAdapter(Context context, int resource, List<Text> messages) {
		super(context, resource, messages);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		
		View content = convertView;
		ViewHolder holder;
		Text sms = getItem(position);
		
		if(content == null){
			//If there is no view, build it
			content = LayoutInflater.from(getContext()).inflate(R.layout.queued_sms_view , null);
			TextView bodyText = (TextView) content.findViewById(R.id.queued_sms_view_body);
			TextView numberText = (TextView) content.findViewById(R.id.queued_sms_view_number);
			TextView timeText = (TextView) content.findViewById(R.id.queued_sms_view_time);
			holder = new ViewHolder(timeText,numberText,bodyText);
			content.setTag(holder);
		} else {
			//If there is, get the pointers to its components
			holder = (ViewHolder) content.getTag();
		}
		
		holder.setText(sms);
		//set the text fields in the view based on its text message
		holder.bodyText.setText(sms.getBody());
		holder.numberText.setText(sms.getNumber());
		String time = Parser.timeStampToString(sms.getTimestamp());
		holder.timeText.setText(time);
		
		
		return content;
	}
	
	
}
