package uk.org.sucu.tatupload;

import uk.org.sucu.tatupload.MessageArrayAdapter.ViewHolder;
import uk.org.sucu.tatupload.activity.SmsReviewActivity;
import uk.org.sucu.tatupload.message.SmsList;
import uk.org.sucu.tatupload.message.Text;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import android.support.v4.app.Fragment;

public class TabContent extends Fragment {

	public final static String TEXT_MESSAGE = "uk.org.sucu.tatupload.TEXT_MESSAGE";
	
	int queue;
	SmsList smsList;
	
	static TabContent newInstance(int queue){
		TabContent tc = new TabContent();
		
		Bundle args  = new Bundle();
		args.putInt("queue", queue);
		tc.setArguments(args);
		
		tc.setQueue(queue);
		
		return tc;
	}
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		queue = getArguments() != null ? getArguments().getInt("queue") : -1;
		setQueue(queue);
	}
	
	
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		outState.putInt("queue", queue);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View queue = inflater.inflate(R.layout.message_queue, container, false);
		ListView lv = (ListView) queue.findViewById(R.id.messageListView);

		MessageArrayAdapter adapter = smsList.getMessageArrayAdapter(this.getActivity());
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {

				// Get the sms message contained in the clicked object
				ViewHolder holder = (ViewHolder) v.getTag();
				Text sms = holder.getText();
				//send it in an intent to an SmsReviewActivity
				Intent intent = new Intent(v.getContext(), SmsReviewActivity.class);
				intent.putExtra(TEXT_MESSAGE, sms);
				startActivity(intent);
			}

		});

		return queue;
	}
	
	private void setQueue(int queue){
		this.queue = queue;
		
		if(queue == R.string.unprocessed){
			smsList = SmsList.getPendingList();
		} else if(queue == R.string.uploaded){
			smsList = SmsList.getUploadedList();
		} else {
			smsList = new SmsList();
		}
	}
	
}
