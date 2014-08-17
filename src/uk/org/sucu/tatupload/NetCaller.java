package uk.org.sucu.tatupload;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
	  	//get data about the chosen browser
	    String packageName = SettingsAccessor.getChosenBrowserPackage(context);
	    String className = SettingsAccessor.getChosenBrowserName(context);
	    //add it to the intent
	    browserIntent.setClassName(packageName, className);
	    //start the browser
	    context.startActivity(browserIntent);
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
