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

@SuppressWarnings("unused")
public class NotificationService extends Service {
    public static int scale = -1;
    public static int level = -1;
    public static int voltage = -1;
    public static int temp = -1;
	
    @Override 
    public IBinder onBind(Intent arg0) {
          return null;
    }
    
	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
    	@Override
    	public void onReceive(Context arg0, Intent intent) {
    		int level = intent.getIntExtra("level", 0);
    		Toast.makeText(arg0, String.valueOf(level), Toast.LENGTH_LONG).show();
    	}
    };
    
    @Override
    public void onCreate() {
          super.onCreate();
          Toast.makeText(this, "Starting Batty Notifier...", Toast.LENGTH_SHORT).show();
         
          /*BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
              @Override
              public void onReceive(Context context, Intent batteryIntent) {
            	  NotificationService.level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            	  NotificationService.scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            	  NotificationService.temp = batteryIntent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
            	  NotificationService.voltage = batteryIntent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
              }
          };
          
          IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
          registerReceiver(batteryReceiver, filter);*/
          
          this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }
    
    /*public void onReceive(Context context, Intent intent){
        // Prepare intent which is triggered if the notification is selected
        Intent notificationIntent = new Intent(this, NotificationService.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        // Build notification
        Notification noti = new Notification.Builder(this)
            .setContentTitle(NotificationService.level + "% Charge Left")
            .setContentText("Temp: " + NotificationService.temp + "°, Voltage: " + NotificationService.voltage)
            //.setSmallIcon(R.drawable.icon) 
            .setContentIntent(pIntent).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); 
        
        // Don't hide the notification after its selected
        noti.flags |= Notification.FLAG_ONGOING_EVENT;

        notificationManager.notify(0, noti);
    }*/
    
    @Override
    public void onDestroy() {
          super.onDestroy();
          Toast.makeText(this, "Stopping Batty Notifier...", Toast.LENGTH_SHORT).show();
    }
}