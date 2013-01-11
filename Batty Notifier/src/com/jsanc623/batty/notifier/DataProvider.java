package com.jsanc623.batty.notifier;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataProvider {
    public static final String KEY_ROWID = "id";
    public static final String KEY_SHOW_TEMP = "show_temp";
    public static final String KEY_SHOW_HEALTH = "show_health";
    public static final String KEY_SHOW_VOLTAGE = "show_voltage";
    public static final String KEY_SHOW_VOLTAGE_MILLIVOLT = "show_voltage_millivolt";
    public static final String KEY_SHOW_STATUS = "show_status";
    public static final String KEY_SHOW_PERIODIC_TOASTS = "show_periodic_toasts";
    private static final String TAG = "DataProvider";
    
    private static final String DATABASE_NAME = "BattyNotifierDB";
    private static final String DATABASE_TABLE = "settings";
    private static final int    DATABASE_VERSION = 2;
    
	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

    private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE + " (" + 
    		                                      KEY_ROWID                     + " integer primary key autoincrement, " + 
    		                                      KEY_SHOW_TEMP                 + " integer, " + 
    		                                      KEY_SHOW_HEALTH               + " integer, " + 
    		                                      KEY_SHOW_VOLTAGE              + " integer, " + 
    		                                      KEY_SHOW_VOLTAGE_MILLIVOLT    + " integer, " +
    		                                      KEY_SHOW_STATUS               + " integer, " + 
    		                                      KEY_SHOW_PERIODIC_TOASTS      + " integer, " + ");";

    public DataProvider(Context ctx){
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
        
    private static class DatabaseHelper extends SQLiteOpenHelper{
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
        	try {
        		db.execSQL(DATABASE_CREATE);	
        	} catch (SQLException e) {
        		e.printStackTrace();
        	}
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }    

    //---opens the database---
    public DataProvider open() throws SQLException{
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---    
    public void close(){
        DBHelper.close();
    }
    
    //---insert a record into the database---
    public long insertRecord(int showTemp, int showHealth, int showVoltage, 
    		                 int showVoltageMilli, int showStatus, int periodicToasts){
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_SHOW_TEMP, showTemp);
        initialValues.put(KEY_SHOW_HEALTH, showHealth);
        initialValues.put(KEY_SHOW_VOLTAGE, showVoltage);
        initialValues.put(KEY_SHOW_VOLTAGE_MILLIVOLT, showVoltageMilli);
        initialValues.put(KEY_SHOW_STATUS, showStatus);
        initialValues.put(KEY_SHOW_PERIODIC_TOASTS, periodicToasts);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }


    
    //---deletes a particular record---
    public boolean deleteContact(long rowId){
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //---retrieves all the records---
    public Cursor getAllRecords(){
        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_SHOW_TEMP, KEY_SHOW_HEALTH, 
        		KEY_SHOW_VOLTAGE, KEY_SHOW_VOLTAGE_MILLIVOLT, KEY_SHOW_STATUS, 
        		KEY_SHOW_PERIODIC_TOASTS}, null, null, null, null, null);
    }

    //---retrieves a particular record---
    public Cursor getRecord(long rowId) throws SQLException{
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_SHOW_TEMP, KEY_SHOW_HEALTH, 
                		KEY_SHOW_VOLTAGE, KEY_SHOW_VOLTAGE_MILLIVOLT, KEY_SHOW_STATUS, 
                		KEY_SHOW_PERIODIC_TOASTS}, 
                KEY_ROWID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates a record---
    public boolean updateRecord(int rowId, int showTemp, int showHealth, int showVoltage, 
            int showVoltageMilli, int showStatus, int periodicToasts){
        ContentValues args = new ContentValues();
        if(showTemp >= 0){ args.put(KEY_SHOW_TEMP, showTemp); }
        if(showHealth >= 0){ args.put(KEY_SHOW_HEALTH, showHealth); }
        if(showVoltage >= 0){ args.put(KEY_SHOW_VOLTAGE, showVoltage); }
        if(showVoltageMilli >= 0){ args.put(KEY_SHOW_VOLTAGE_MILLIVOLT, showVoltageMilli); }
        if(showStatus >= 0){ args.put(KEY_SHOW_STATUS, showStatus); }
        if(periodicToasts >= 0){ args.put(KEY_SHOW_PERIODIC_TOASTS, periodicToasts); }
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

	public String getDatabaseName() {
		return DATABASE_NAME;
	}
}