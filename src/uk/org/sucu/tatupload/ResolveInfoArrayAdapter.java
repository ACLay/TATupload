package uk.org.sucu.tatupload;

import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ResolveInfoArrayAdapter extends ArrayAdapter<ResolveInfo> {

	private PackageManager pm;

	public ResolveInfoArrayAdapter(Context context, int resource, List<ResolveInfo> objects) {
		super(context, resource, objects);
		pm = context.getPackageManager();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		//TODO include app icon
		return makeView(position);
	}
	
	private View makeView(int position){
		
		View view = LayoutInflater.from(getContext()).inflate(R.layout.browser_data_layout, null);
		TextView tv = (TextView) view.findViewById(R.id.browserNameLabel);
		ResolveInfo info = getItem(position);

		tv.setText(info.loadLabel(pm));

		return view;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent){
		return makeView(position);
	}
}
