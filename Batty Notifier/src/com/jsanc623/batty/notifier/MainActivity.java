package com.jsanc623.batty.notifier;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class MainActivity extends Activity {
	public DataProvider DataProvider = new DataProvider(MainActivity.this);
	public static int db_showTemp = 0;
	public static int db_showHealth = 0;
	public static int db_showVoltage = 0;
	public static int db_showVoltageMilli = 0;
	public static int db_showStatus = 0;
	public static int db_periodicToasts = 0;
	
	public void retrieveFromDb(){
		Toast.makeText(MainActivity.this, "inretrieve,beforedprovider", Toast.LENGTH_LONG).show();
		DataProvider.open();
		Toast.makeText(MainActivity.this, "inretrieve,beforedata", Toast.LENGTH_LONG).show();
		Cursor data = DataProvider.getRecord(1);
		//DataProvider.close();
		/*db_showTemp = data.getInt(2);
		db_showHealth = data.getInt(3);
		db_showVoltage = data.getInt(4);
		db_showVoltageMilli = data.getInt(5);
		db_showStatus = data.getInt(6);
		db_periodicToasts = data.getInt(7);*/
	}
	
	public void updateRecord(int db_showTemp, int db_showHealth, int db_showVoltage, int db_showVoltageMilli, int db_showStatus, int db_periodicToasts){
		DataProvider.open();
		DataProvider.updateRecord(1, db_showTemp, db_showHealth, db_showVoltage, db_showVoltageMilli, db_showStatus, db_periodicToasts);
		DataProvider.close();
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        retrieveFromDb();
        
        Switch onOff = (Switch)findViewById(R.id.MainActivity_OnOffSwitch);
        onOff.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            	if(isChecked == true)
            		startService(new Intent(MainActivity.this,NotificationService.class));
            	else 
            		stopService(new Intent(MainActivity.this,NotificationService.class));
            }
        });
        
        /*Switch showTemp = (Switch)findViewById(R.id.MainActivity_ShowTempSwitch);
        if(db_showTemp == 1) showTemp.setChecked(true); else showTemp.setChecked(false);
        showTemp.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            	if(isChecked == true)
            		updateRecord(1, db_showHealth, db_showVoltage, db_showVoltageMilli, db_showStatus, db_periodicToasts);
            	else 
            		updateRecord(0, db_showHealth, db_showVoltage, db_showVoltageMilli, db_showStatus, db_periodicToasts);
            }
        });
        
        Switch showHealth = (Switch)findViewById(R.id.MainActivity_ShowHealthSwitch);
        if(db_showHealth == 1) showHealth.setChecked(true); else showHealth.setChecked(false);
        showHealth.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            	if(isChecked == true)
            		updateRecord(db_showTemp, 1, db_showVoltage, db_showVoltageMilli, db_showStatus, db_periodicToasts);
            	else 
            		updateRecord(db_showTemp, 0, db_showVoltage, db_showVoltageMilli, db_showStatus, db_periodicToasts);
            }
        });
        
        
        Switch showVoltage = (Switch)findViewById(R.id.MainActivity_ShowVoltageSwitch);
        if(db_showVoltage == 1) showVoltage.setChecked(true); else showVoltage.setChecked(false);
        showVoltage.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            	if(isChecked == true)
            		updateRecord(db_showTemp, db_showHealth, 1, db_showVoltageMilli, db_showStatus, db_periodicToasts);
            	else 
            		updateRecord(db_showTemp, db_showHealth, 0, db_showVoltageMilli, db_showStatus, db_periodicToasts);
            }
        });
        
        
        Switch showMillivolt = (Switch)findViewById(R.id.MainActivity_ShowMillivoltSwitch);
        if(db_showVoltageMilli == 1) showMillivolt.setChecked(true); else showMillivolt.setChecked(false);
        showMillivolt.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            	if(isChecked == true)
            		updateRecord(db_showTemp, db_showHealth, db_showVoltage, 1, db_showStatus, db_periodicToasts);
            	else 
            		updateRecord(db_showTemp, db_showHealth, db_showVoltage, 0, db_showStatus, db_periodicToasts);
            }
        });
        
        
        Switch showStatus = (Switch)findViewById(R.id.MainActivity_ShowStatusSwitch);
        if(db_showStatus == 1) showStatus.setChecked(true); else showStatus.setChecked(false);
        showStatus.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            	if(isChecked == true)
            		updateRecord(db_showTemp, db_showHealth, db_showVoltage, db_showVoltageMilli, 1, db_periodicToasts);
            	else 
            		updateRecord(db_showTemp, db_showHealth, db_showVoltage, db_showVoltageMilli, 0, db_periodicToasts);
            }
        });
        
        
        Switch showToasts = (Switch)findViewById(R.id.MainActivity_ShowToastsSwitch);
        if(db_periodicToasts == 1) showToasts.setChecked(true); else showToasts.setChecked(false);
        showToasts.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            	if(isChecked == true)
            		updateRecord(db_showTemp, db_showHealth, db_showVoltage, db_showVoltageMilli, db_showStatus, 1);
            	else 
            		updateRecord(db_showTemp, db_showHealth, db_showVoltage, db_showVoltageMilli, db_showStatus, 0);
            }
        });*/
        
    }
}