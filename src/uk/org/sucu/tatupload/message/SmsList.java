package uk.org.sucu.tatupload.message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.pig.impl.util.ObjectSerializer;

import uk.org.sucu.tatupload.MessageArrayAdapter;
import uk.org.sucu.tatupload.R;
import android.content.Context;

public class SmsList {

	private static ArrayList<Text> texts = new ArrayList<Text>();
	private static MessageArrayAdapter adapter = null;
	
	public static void addText(Text msg){
		synchronized(texts){
			texts.add(msg);
		}
		notifyAdapter();
	}
	
	public static void addTexts(Collection<Text> msgs){
		synchronized(texts){
			for(Text t : msgs){
				texts.add(t);
			}
		}
		notifyAdapter();
	}
	
	public static void clearList(){
		synchronized(texts){
			texts.clear();
		}
		notifyAdapter();
	}
	
	public static void removeText(Text msg){
		synchronized(texts){
			texts.remove(msg);
		}
		notifyAdapter();
	}
	
	public static MessageArrayAdapter getMessageArrayAdapter(Context context){
		if(adapter == null){
			adapter = new MessageArrayAdapter(context, R.id.messageListView, texts);
		}
		return adapter;
	}
	
	private static void notifyAdapter(){
		if(adapter != null){
			adapter.notifyDataSetChanged();
		}
	}
	
	public static boolean isEmpty(){
		return texts.isEmpty();
	}
	
	public static String getSerialList() throws IOException{
		String data;
		synchronized(texts){
			data = ObjectSerializer.serialize(texts);
		}
		return data;
	}
	
	public static int getSize(){
		return texts.size();
	}
}
