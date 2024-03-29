package com.ltdung.friendlychat.library.data.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class NetworkManagerImpl extends BroadcastReceiver implements NetworkManager {

    private Context context;

    private IntentFilter connectivityIntentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

    private Map<String, Listener> listeners = new HashMap<>();

    public NetworkManagerImpl(Context context){
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(isNetworkAvailable()){
            // Prevent double calling in case of onLoadFinished in BaseActivity
            if(!isInitialStickyBroadcast()){
                for(Listener listener: listeners.values()){
                    if(listener != null){
                        listener.onNetworkAvailable();
                    }
                }
            }
        }
    }

    @Override
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null){
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI
                    || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){
                return true;
            }
        }
        return false;
    }

    @Override
    public void start() {
        context.registerReceiver(this, connectivityIntentFilter);
    }

    @Override
    public void stop() {
        context.unregisterReceiver(this);
    }

    @Override
    public void add(String tag, Listener listener) {
        listeners.put(tag, listener);
    }

    @Override
    public void remove(String tag) {
        listeners.remove(tag);
    }
}
