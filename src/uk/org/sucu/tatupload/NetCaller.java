package uk.org.sucu.tatupload;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Browser;

public class NetCaller {

	//While it would be nice to handle everything in-app, it seems for the time being I'll need to go via the browser.
	public static void callScript(Uri uri, Context context){
		//Opens the link in either the default browser or the first one in the list of available apps if multiple are installed
		final Intent browserIntent = new Intent(Intent.ACTION_VIEW);
	    browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    browserIntent.setData(uri);
	    //open all this apps requests in the same tab, prevents new ones with each call
	    browserIntent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
	  		
	    // 1: Try to find the default browser and launch the URL with it
	    final ResolveInfo defaultResolution = context.getPackageManager().resolveActivity(browserIntent, PackageManager.MATCH_DEFAULT_ONLY);
	    if (defaultResolution != null) {
	        final ActivityInfo activity = defaultResolution.activityInfo;
	        if (!activity.name.equals("com.android.internal.app.ResolverActivity")) {
	            browserIntent.setClassName(activity.applicationInfo.packageName, activity.name);
	            context.startActivity(browserIntent);
	            return;
	        }
	    }
	    // 2: Try to find anything that we can launch the URL with. Pick up the first one that can.
	    final List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(browserIntent, PackageManager.MATCH_DEFAULT_ONLY);
	    if (!resolveInfoList.isEmpty()) {
	        browserIntent.setClassName(resolveInfoList.get(0).activityInfo.packageName, resolveInfoList.get(0).activityInfo.name);
	        context.startActivity(browserIntent);
	        return;
	    }
	}
	
	public static void callScriptWithChoice(Uri uri, Context context){

		//While it would be nice to handle everything in-app, it seems for the time being I'll need to go via the browser.
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
		//open all this apps requests in the same tab, prevents new ones with each call
		browserIntent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
		//allows a new task to be started outside of a current task
		browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(browserIntent);
	}

	public static boolean isOnline(Context context){

		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
		boolean online =  activeNetworkInfo != null && activeNetworkInfo.isConnected();

		return online;
	}
	//TODO prevent wifi sleep?
	public static boolean isOnlineWithErrorBox(Context context){
		boolean online =  isOnline(context);
		//show an error dialog if there's no network connection
		if(!online){
			new AlertDialog.Builder(context)
			.setTitle("Problem")  
			.setMessage("There is no network connection available.")
			.setPositiveButton(android.R.string.ok, null)  
			.setCancelable(false)  
			.create()  
			.show();
		}

		return online;
	}

}
