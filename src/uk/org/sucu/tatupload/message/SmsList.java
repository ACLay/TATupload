package uk.org.sucu.tatupload.message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.pig.impl.util.ObjectSerializer;

import uk.org.sucu.tatupload.MessageArrayAdapter;
import uk.org.sucu.tatupload.R;
import android.content.Context;

public class SmsList {

	public static final SmsList unprocessedList = new SmsList();
	public static final SmsList processedList = new SmsList();
	
	public static SmsList getPendingList(){//unprocessed, pending
		return unprocessedList;
	}
	
	public static SmsList getUploadedList(){//processed, uploaded
		return processedList;
	}
	
	private ArrayList<Text> texts = new ArrayList<Text>();
	private MessageArrayAdapter adapter = null;
	
	public void addText(Text msg){
		synchronized(texts){
			texts.add(msg);
		}
		notifyAdapter();
	}
	
	public void addTexts(Collection<Text> msgs){
		synchronized(texts){
			for(Text t : msgs){
				texts.add(t);
			}
		}
		notifyAdapter();
	}
	
	public void clearList(){
		synchronized(texts){
			texts.clear();
		}
		notifyAdapter();
	}
	
	public void removeText(Text msg){
		synchronized(texts){
			texts.remove(msg);
		}
		notifyAdapter();
	}
	
	public MessageArrayAdapter getMessageArrayAdapter(Context context){
		if(adapter == null){
			adapter = new MessageArrayAdapter(context, R.id.messageListView, texts);
		}
		return adapter;
	}
	
	private void notifyAdapter(){
		if(adapter != null){
			adapter.notifyDataSetChanged();
		}
	}
	
	public boolean isEmpty(){
		return texts.isEmpty();
	}
	
	public String getSerialList() throws IOException{
		String data;
		synchronized(texts){
			data = ObjectSerializer.serialize(texts);
		}
		return data;
	}
	
	public int getSize(){
		return texts.size();
	}
}
