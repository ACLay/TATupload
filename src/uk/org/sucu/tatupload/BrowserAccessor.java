package uk.org.sucu.tatupload;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;

public class BrowserAccessor {

	public static List<ResolveInfo> getResolvers(Context context){
		//build an intent that launches to the webscript url
		Uri uri = Uri.parse(context.getString(R.string.scriptURL));
		Intent browserIntent = new Intent(Intent.ACTION_VIEW);
		browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		browserIntent.setData(uri);
		//return a list of the packages that can handle the intent
		return context.getPackageManager().queryIntentActivities(browserIntent, PackageManager.MATCH_DEFAULT_ONLY);
	}

	public static String getPackageName(ResolveInfo resolveInfo){
		return resolveInfo.activityInfo.packageName;		
	}
	
	public static String getName(ResolveInfo resolveInfo){
		return resolveInfo.activityInfo.name;
	}
	
	public static boolean browserSet(Context context){
		String browserPackageName = Settings.getChosenBrowserPackage(context);
		//null is returned as default if no saved browser package name is able to be loaded
		return ! browserPackageName.equals(null);
	}
	
	public static boolean isInstalled(String packageName, String className, Context context){
		//get a list of all installed packages
		List<ResolveInfo> apps = getResolvers(context);
		//return true if one has the desired class and packageName
		for(ResolveInfo app : apps){
			if(getPackageName(app).equals(packageName) && getName(app).equals(className)){
				return true;
			}
		}
		//else return false
		return false;
	}
	
	@SuppressLint("InflateParams")
	public static void openBrowserChoicePopup(final Activity activity, boolean cancelable){
		final View viewToLoad = LayoutInflater.from(activity).inflate(R.layout.browser_choice_popup, null);	
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity)
		.setTitle("Select browser")
		.setView(viewToLoad)
		.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//get the selected ResolveInfo
				Spinner spin = (Spinner) viewToLoad.findViewById(R.id.browserSpinner);
				ResolveInfo selected = (ResolveInfo) spin.getSelectedItem();
				//get the packagename
				String packageName = getPackageName(selected);
				String className = getName(selected);
				//save it
				((TatUploadApplication) activity.getApplication()).setBrowserData(packageName, className);
			}
		})
		.setCancelable(cancelable);
		//add a cancel button if applicable
		if(cancelable){
			builder.setNegativeButton(android.R.string.cancel, null);
		}
		
		builder.create();
		
		List<ResolveInfo> resolvers = getResolvers(activity);
		
		//connect the dialogs spinner to the data structure
		Spinner spin = (Spinner) viewToLoad.findViewById(R.id.browserSpinner);
		ResolveInfoArrayAdapter adapter = new ResolveInfoArrayAdapter(activity, android.R.layout.simple_spinner_item, resolvers);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(adapter);

		builder.show();
	}
	
	public static boolean usable(Context context){
		//load the browser data
		String packageName = Settings.getChosenBrowserPackage(context);
		String className = Settings.getChosenBrowserName(context);
		//check they're actual saved values, not the defaults from failure
		if(packageName == Settings.BROWSER_PACKAGE_DEFAULT || className == Settings.BROWSER_NAME_DEFAULT){
			return false;
		}
		//check it's still installed
		return isInstalled(packageName, className, context);
	}
	
}
