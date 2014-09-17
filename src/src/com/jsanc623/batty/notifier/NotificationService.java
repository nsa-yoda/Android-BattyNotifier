package com.jsanc623.batty.notifier;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.BatteryManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class NotificationService extends Service {
	public static final int FM_NOTIFICATION_ID = 1;
	public static int SHOW_TEMP = 1; // whether to show temperature in status bar
	public static int SHOW_HEALTH = 1; // whether to show battery health in status bar
	public static int SHOW_VOLTAGE = 1; // whether to show voltage in status bar
	public static int SHOW_VOLTAGE_MILLIVOLT = 1; // whether to show millivolts in status bar
	public static int SHOW_STATUS = 1; // whether to show Charging/Not Charging, etc
	public static int SHOW_PERIODIC_TOASTS = 1; // whether to show periodic toast messages with charge level
	
    @Override 
    public IBinder onBind(Intent arg0) {
          return null;
    }
    
	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
    	@SuppressWarnings({ "unused" })
		@Override
    	public void onReceive(Context context, Intent intent) {
    		int level = intent.getIntExtra("level", 0);
    		int temp = intent.getIntExtra("temperature", 0) / 10;
    		int voltage = intent.getIntExtra("voltage", 0);
    		int status = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);
            int scale = intent.getIntExtra("scale", -1);
            int health = intent.getIntExtra("health", -1);
            boolean isPresent = intent.getBooleanExtra("present", false);
    		String strStatus;
    		String strHealth = "Health: ";
    		
    		// Calculate level
    		if (level >= 0 && scale > 0) {
    			level = (level * 100) / scale;
    		}
    		
    		if(SHOW_PERIODIC_TOASTS == 1){
    			Toast.makeText(context, String.valueOf(level + "% Charge"), Toast.LENGTH_SHORT).show();
    		}
    		
    		// Determine battery status    		
    		switch(status){
				case BatteryManager.BATTERY_STATUS_CHARGING : strStatus = "Charging"; break;
				case BatteryManager.BATTERY_STATUS_DISCHARGING : strStatus = "Discharging"; break;
				case BatteryManager.BATTERY_STATUS_NOT_CHARGING : strStatus = "Not Charging"; break;
				case BatteryManager.BATTERY_STATUS_FULL : strStatus = "Full"; break;
				default : strStatus = "Status Unknown";
    		}
    		
    		
    		// Determine battery health
    		switch(health){
				case BatteryManager.BATTERY_HEALTH_COLD : strHealth += "Cold"; break;
				case BatteryManager.BATTERY_HEALTH_DEAD : strHealth += "Dead"; break;
				case BatteryManager.BATTERY_HEALTH_GOOD : strHealth += "Good"; break;
				case BatteryManager.BATTERY_HEALTH_OVERHEAT : strHealth += "Overheat"; break;
				case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE : strHealth += "Over Voltage"; break;
				case BatteryManager.BATTERY_HEALTH_UNKNOWN : strHealth += "Unknown"; break;
				case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE : strHealth += "Unspc Failure"; break;
				default : strStatus = "Health Unknown";
    		}
    		
    		// The initial call out notification
    		String NotificationTicket = "Batty Notifier Started!";
    		
    		// The status user sees upon pulling notification bar down
    	    String NotificationTitle = level + "%";
    	    if(SHOW_STATUS == 1){ NotificationTitle += " and " + strStatus; }
    	    
    	    // The content show underneath the battery percentage
    	    String NotificationContent = "";
    	    if(SHOW_TEMP == 1) { NotificationContent += temp + "°C  "; }
    	    if(SHOW_VOLTAGE == 1) {
    	    	if(SHOW_VOLTAGE_MILLIVOLT == 1){
    	    		NotificationContent += voltage + "mV  ";
    	    	} else {
    	    		NotificationContent += voltage / 1000 + "V  ";
    	    	}
    	    }
    	    if(SHOW_HEALTH == 1) { NotificationContent += strHealth; }

    	    Bitmap largeIcon = (Bitmap)BitmapFactory.decodeResource(context.getResources(), R.drawable.battery);
    	    
    	    addNotification(NotificationTitle, NotificationContent, NotificationTicket, largeIcon, level);
    	}
    }; 
    
    @Override
    public void onCreate() {
          super.onCreate();
          this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }
    
    @Override
    public void onDestroy() {
          super.onDestroy();
          Toast.makeText(this, "Stopping Batty Notifier...", Toast.LENGTH_SHORT).show();
          removeNotification();
    }
    
    // Start notification
    private void addNotification(String title, String body, String ticker, Bitmap largeIcon, int iconLevel) {
	    NotificationCompat.Builder builder =  
	            new NotificationCompat.Builder(this)  
	            .setSmallIcon(R.drawable.levellist, iconLevel)
	            .setLargeIcon(largeIcon)
	            .setContentTitle(title)  
	            .setContentText(body)
	            .setOngoing(true)
	            .setNumber(iconLevel)
	            .setTicker(ticker);  
	
	    Intent notificationIntent = new Intent(this, MainActivity.class);  
	    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,   
	            PendingIntent.FLAG_UPDATE_CURRENT);  
	    builder.setContentIntent(contentIntent);  
	
	    // Add as notification  
	    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);  
	    manager.notify(FM_NOTIFICATION_ID, builder.build());  
	}  

	// Remove notification  
	private void removeNotification() {  
	    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);  
	    manager.cancel(FM_NOTIFICATION_ID);  
	}  
}