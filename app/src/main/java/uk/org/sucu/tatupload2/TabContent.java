package uk.org.sucu.tatupload2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import uk.org.sucu.tatupload2.MessageArrayAdapter.ViewHolder;
import uk.org.sucu.tatupload2.activity.MainActivity;
import uk.org.sucu.tatupload2.activity.SmsReviewActivity;
import uk.org.sucu.tatupload2.message.SmsList;
import uk.org.sucu.tatupload2.message.Text;
import uk.org.sucu.tatupload2.network.ForegroundUploadTask;

public class TabContent extends Fragment {

	public static final String QUEUE_ID = "queue";

	// Track which text the menu is for
	private static Text menuSms = null;

	int queue;
	SmsList smsList;
	
	static TabContent newInstance(int queue){
		TabContent tc = new TabContent();
		
		Bundle args  = new Bundle();
		args.putInt(QUEUE_ID, queue);
		tc.setArguments(args);
		
		tc.setQueue(queue);
		
		return tc;
	}
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		queue = getArguments() != null ? getArguments().getInt(QUEUE_ID) : -1;
		setQueue(queue);
	}
	
	
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		outState.putInt(QUEUE_ID, queue);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View queue = inflater.inflate(R.layout.message_queue, container, false);
		ListView lv = (ListView) queue.findViewById(R.id.messageListView);

		// Allow the entries in the tab's message list to have context menus
		registerForContextMenu(lv);

		MessageArrayAdapter adapter = smsList.getMessageArrayAdapter(this.getActivity());
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {

				// Get the sms message contained in the clicked object
				ViewHolder holder = (ViewHolder) v.getTag();
				menuSms = holder.getText();
				//send it in an intent to an SmsReviewActivity
				v.showContextMenu();
			}

		});

		return queue;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = this.getActivity().getMenuInflater();
		inflater.inflate(R.menu.queued_text, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item){

		switch (item.getItemId()){
			case R.id.text_upload:
				uploadText(getActivity());
				return true;
			case R.id.text_review:
				// open an instance of SmsReviewActivity
				Intent intent = new Intent(getContext(), SmsReviewActivity.class);
				intent.putExtra(MainActivity.TEXT_MESSAGE, menuSms);
				startActivity(intent);
				return true;
			case R.id.text_discard:
				// double check they want to delete it
				new AlertDialog.Builder(getContext())
						.setTitle(R.string.discard)
						.setMessage(R.string.confirm_choice)
						.setPositiveButton(R.string.discard, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								SmsList pendingList = SmsList.getPendingList();
								SmsList uploadedList = SmsList.getUploadedList();
								if(pendingList.contains(menuSms)){
									pendingList.removeText(menuSms);
									new Settings(getContext()).savePendingTextsList();
									Notifications.updateNotification(getContext());
								} else if(uploadedList.contains(menuSms)){
									uploadedList.removeText(menuSms);
									new Settings(getContext()).saveUploadedTextsList();
								}
							}
						})
						.setNegativeButton(android.R.string.cancel, null)
						.create()
						.show();
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}

	public static void retryTextUpload(Activity activity){
		uploadText(activity);
	}

	private static void uploadText(Activity activity){
		// make the task to upload the text
		ProgressDialog mProgress = new ProgressDialog(activity);
		mProgress.setMessage(activity.getString(R.string.progress_uploading));
		ForegroundUploadTask uploader = new ForegroundUploadTask(menuSms, mProgress, activity);
		uploader.execute();
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
