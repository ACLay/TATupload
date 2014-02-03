package uk.org.sucu.tatupload;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Browser;

public class NetCaller {
	
	public static void callScript(Uri uri, Context context){
		
		//While it would be nice to handle everything in-app, it seems for the time being I'll need to go via the browser.
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
		//open all this apps requests in the same tab, prevents new ones with each call
		browserIntent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
		//allows a new task to be started outside of a current task
		browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(browserIntent);
	}
	
}
