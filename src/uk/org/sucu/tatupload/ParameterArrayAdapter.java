package uk.org.sucu.tatupload;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ParameterArrayAdapter extends ArrayAdapter<String> {

	public ParameterArrayAdapter(Context context, int resource,	List<String> objects) {
		super(context, resource, objects);
	}

	public View getView(int position, View convertView, ViewGroup parent){
		TextView tv = new TextView(getContext());
		tv.setText(getItem(position));
		return tv;
	}
	
}
