package uk.org.sucu.tatupload.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;

public class NetManager {

    /**
     * Extend the given HttpRequestInitializer (usually a credentials object)
     * with additional initialize() instructions.
     *
     * @param requestInitializer the initializer to copy and adjust; typically
     *         a credential object.
     * @return an initializer with an extended read timeout.
     */
    public static HttpRequestInitializer setHttpTimeout(
            final HttpRequestInitializer requestInitializer) {
        return new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest)
                    throws java.io.IOException {
                requestInitializer.initialize(httpRequest);
                // This allows the API to call (and avoid timing out on)
                // functions that take up to 6 minutes to complete (the maximum
                // allowed script run time), plus a little overhead.
                httpRequest.setReadTimeout(380000);
            }
        };
    }


    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    public static boolean isOnline(Context context){
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static boolean isOnlineWithToast(Context context){
        boolean online = isOnline(context);

        if(!online){
            Toast.makeText(context, "There is no network connection available.", Toast.LENGTH_SHORT).show();
        }

        return online;
    }
}
