package com.jsanc623.batty.notifier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoStartReceiver extends BroadcastReceiver {
	public DataProvider DataProvider;
	
    @Override
    public void onReceive(Context context, Intent intent) {
    	DataProvider = new DataProvider(context);
    	
        Intent startServiceIntent = new Intent(context, NotificationService.class);
        context.startService(startServiceIntent);
    }
}