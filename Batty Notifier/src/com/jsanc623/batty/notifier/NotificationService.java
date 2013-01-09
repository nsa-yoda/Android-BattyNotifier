package com.jsanc623.batty.notifier;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;
import android.widget.Toast;

public class NotificationService extends Service {
	public static final int NOTIFICATION_ID = 1;
	
    @Override 
    public IBinder onBind(Intent arg0) {
          return null;
    }
    
	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
		public NotificationManager myNotificationManager;
		
    	@SuppressWarnings("deprecation")
		@Override
    	public void onReceive(Context context, Intent intent) {
    		int level = intent.getIntExtra("level", 0);
    		int temp = intent.getIntExtra("temperature", 0) / 10;
    		int voltage = intent.getIntExtra("voltage", 0) / 1000;
    		Toast.makeText(context, String.valueOf(level + "% Charge"), Toast.LENGTH_SHORT).show();
    		
    		// Determine charging status
    		int status = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);
    		String strStatus;
    		if (status == BatteryManager.BATTERY_STATUS_CHARGING){
    			strStatus = "Charging";
    		} else if (status == BatteryManager.BATTERY_STATUS_DISCHARGING || 
    				   status == BatteryManager.BATTERY_STATUS_NOT_CHARGING){
    			strStatus = "Not Charging";
    		} else if (status == BatteryManager.BATTERY_STATUS_FULL){
    			strStatus = "Full";
    		} else {
    			strStatus = "Status Unknown";
    		}
    		
    		CharSequence NotificationTicket = "Batty Notifier Started!";
    	    CharSequence NotificationTitle = level + "% and " + strStatus;
    	    CharSequence NotificationContent = "Temp: " + temp + "°C, Voltage: " + voltage + "V";
    		
    	    myNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);    	    
    		Notification notification = new Notification(R.drawable.battery, NotificationTicket, 0);
    	    Intent notificationIntent = new Intent(context, NotificationService.class);
    	    PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
    	    notification.setLatestEventInfo(context, NotificationTitle, NotificationContent, contentIntent);
    	    notification.flags |= Notification.FLAG_ONGOING_EVENT;
    	    myNotificationManager.notify(NOTIFICATION_ID, notification);
    	}
    };
    
    @Override
    public void onCreate() {
          super.onCreate();
          Toast.makeText(this, "Starting Batty Notifier...", Toast.LENGTH_SHORT).show();          
          this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }
    
    @Override
    public void onDestroy() {
          super.onDestroy();
          Toast.makeText(this, "Stopping Batty Notifier...", Toast.LENGTH_SHORT).show();
          NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); 
          notificationManager.cancel(NOTIFICATION_ID);
    }
}