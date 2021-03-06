package com.android.foodorderapp.extras;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.app.Dialog;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import static android.content.Context.CONNECTIVITY_SERVICE;

public class Broadcast extends BroadcastReceiver {
    //Register
    public static final String NETWORK_AVAILABLE_ACTION = "com.android.foodorderapp.NetworkAvailable";
    public static final String IS_NETWORK_AVAILABLE = "isNetworkAvailable";


    @Override
    public void onReceive(Context context, Intent intent) {
        Intent networkStateIntent = new Intent(NETWORK_AVAILABLE_ACTION);
        networkStateIntent.putExtra(IS_NETWORK_AVAILABLE, isConnectedToInternet(context));
        LocalBroadcastManager.getInstance(context).sendBroadcast(networkStateIntent);

    }

    private boolean isConnectedToInternet(Context context) {
        try {
            if (context != null) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
            return false;
        } catch (Exception e) {
            Log.e(Broadcast.class.getName(), e.getMessage());
            return false;
        }
    }
}