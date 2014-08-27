package uk.org.sucu.tatupload;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ResolveInfoArrayAdapter extends ArrayAdapter<ResolveInfo> {

	private PackageManager pm;

	public ResolveInfoArrayAdapter(Context context, int resource, List<ResolveInfo> objects) {
		super(context, resource, objects);
		pm = context.getPackageManager();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		return makeView(position);
	}
	
	@SuppressLint("InflateParams")
	private View makeView(int position){
		
		ResolveInfo info = getItem(position);

		View view = LayoutInflater.from(getContext()).inflate(R.layout.browser_data_layout, null);
		
		TextView tv = (TextView) view.findViewById(R.id.browserNameLabel);
		tv.setText(info.loadLabel(pm));
		
		ImageView iv = (ImageView) view.findViewById(R.id.iconImageView);
		try {
			Drawable icon = getContext().getPackageManager().getApplicationIcon(info.activityInfo.packageName);
			iv.setImageDrawable(icon);
		} catch (NameNotFoundException e) {
			
		}
		
		return view;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent){
		return makeView(position);
	}
}
