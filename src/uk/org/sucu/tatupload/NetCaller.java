package uk.org.sucu.tatupload;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Browser;
import android.widget.Toast;

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
	    Settings settings = new Settings(context);
	    String packageName = settings.getChosenBrowserPackage();
	    String className = settings.getChosenBrowserName();
	    //add it to the intent
	    browserIntent.setClassName(packageName, className);
	    //start the browser
	    context.startActivity(browserIntent);
	}

	public static boolean isOnline(Context context){

		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
		boolean online =  activeNetworkInfo != null && activeNetworkInfo.isConnected();

		return online;
	}
	//TODO prevent wifi sleep?
	public static boolean isOnlineWithToast(Context context){
		boolean online = isOnline(context);
		
		if(!online){
			Toast.makeText(context, "There is no network connection available.", Toast.LENGTH_SHORT).show();
		}

		return online;
	}

}
