package uk.org.sucu.tatupload.message;

import java.util.ArrayList;
import java.util.Collection;

import uk.org.sucu.tatupload.MessageArrayAdapter;
import uk.org.sucu.tatupload.R;
import android.content.Context;

public class SmsList {

	private static ArrayList<Text> texts = new ArrayList<Text>();
	private static MessageArrayAdapter adapter;
	
	public static void addText(Text msg){
		synchronized(texts){
			texts.add(msg);
		}
		adapter.notifyDataSetChanged();
	}
	
	public static void addTexts(Collection<Text> msgs){
		synchronized(texts){
			for(Text t : msgs){
				texts.add(t);
			}
		}
		adapter.notifyDataSetChanged();
	}
	
	public static void clearList(){
		synchronized(texts){
			texts.clear();
		}
		adapter.notifyDataSetChanged();
	}
	
	public static void removeText(Text msg){
		synchronized(texts){
			texts.remove(msg);
		}
		adapter.notifyDataSetChanged();
	}
	
	public static MessageArrayAdapter getMessageArrayAdapter(Context context){
		adapter = new MessageArrayAdapter(context, R.id.messageListView, texts);
		return adapter;
	}
}
