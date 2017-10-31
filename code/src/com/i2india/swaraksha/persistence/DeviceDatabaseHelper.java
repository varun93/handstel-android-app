package com.i2india.swaraksha.persistence;

import java.util.ArrayList;
import java.util.Date;

import org.i2india.swaraksha.model.DeviceMode;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DeviceDatabaseHelper {

	private static final String TAG = DeviceDatabaseHelper.class
			.getSimpleName();

	// database configuration
	// if you want the onUpgrade to run then change the database_version
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "mydatabase.db";
	
	// table configuration
	private static final String TABLE_NAME = "Device"; // Table name
	private static final String DEVICE_TABLE_COLUMN_ID = "_id"; // a column
																// named "_id"
																// is required
																// for cursor
	private static final String DEVICE_TABLE_COLUMN_NAME = "deviceName";
	private static final String DEVICE_TABLE_COLUMN_IMEI = "deviceIMEI";
	private static final String DEVICE_TABLE_COLUMN_SIM = "deviceSIM";
	private static final String DEVICE_TABLE_COLUMN_PASSWORD = "devicePassword";
	private static final String DEVICE_TABLE_LOCATION_LAST_UPDATED = "locationLastupdated";
	private static final String DEVICE_TABLE_BATTERY_LAST_UPDATED = "batteryLastupdated";

	// real database is in here
	private DatabaseOpenHelper openHelper;
	// database obtained from here
	private SQLiteDatabase database;

	// this is a wrapper class. that means, from outside world, anyone will
	// communicate with DeviceDatabaseHelper,
	// but under the hood actually DatabaseOpenHelper class will perform
	// database CRUD operations
	public DeviceDatabaseHelper(Context aContext) {

		// get the database from the context obtained from the main activity
		openHelper = new DatabaseOpenHelper(aContext);
		database = openHelper.getWritableDatabase();
	}

	public void addDevice(DeviceMode device) {

		Long current_time = new Date().getTime();
		ContentValues contentValues = new ContentValues();
		contentValues.put(DEVICE_TABLE_COLUMN_NAME, device.getDeviceName());
		contentValues.put(DEVICE_TABLE_COLUMN_IMEI, device.getDeviceIMEI());
		contentValues.put(DEVICE_TABLE_COLUMN_SIM, device.getDeviceSIM());
		contentValues.put(DEVICE_TABLE_COLUMN_PASSWORD,
				device.getDevicePassword());
		contentValues.put(DEVICE_TABLE_LOCATION_LAST_UPDATED,current_time.toString());
		database.insert(TABLE_NAME, null, contentValues);

	}

	public ArrayList<DeviceMode> selectAllDevices() {

		ArrayList<DeviceMode> devices = new ArrayList<DeviceMode>();
		String buildSQL = "SELECT * FROM " + TABLE_NAME;
		Cursor cursor = database.rawQuery(buildSQL, null);

		while (true) {
			if (!cursor.moveToNext()) {
				cursor.close();
				return devices;
			}
			DeviceMode deviceMode = new DeviceMode();
			deviceMode.setDeviceName(cursor.getString(cursor
					.getColumnIndex("deviceName")));
			deviceMode.setDeviceIMEI(cursor.getString(cursor
					.getColumnIndex("deviceIMEI")));
			deviceMode.setDeviceSIM(cursor.getString(cursor
					.getColumnIndex("deviceSIM")));
			deviceMode.setDevicePassword(cursor.getString(cursor
					.getColumnIndex("devicePassword")));

			devices.add(deviceMode);
		}
	}

	public boolean deleteDeviceByID(int deviceID) {
		return database.delete(TABLE_NAME, DEVICE_TABLE_COLUMN_ID + "="
				+ deviceID, null) > 0;
	}

	public Cursor getAllDevices() {

		String buildSQL = "SELECT * FROM " + TABLE_NAME;

		Log.d(TAG, "getAllData SQL: " + buildSQL);

		return database.rawQuery(buildSQL, null);
	}

	public int getDeviceId(String deviceName) {
		int deviceID = 0;
		Cursor cursor = database.query(TABLE_NAME, null,
				DEVICE_TABLE_COLUMN_NAME + "=?", new String[] { deviceName },
				null, null, null);
		if (cursor.moveToFirst())
			deviceID = cursor.getInt(cursor
					.getColumnIndex(DEVICE_TABLE_COLUMN_ID));

		return deviceID;
	}

	public boolean isDevicePresent(String deviceName) {

		DeviceMode device = getDeviceByName(deviceName);

		return (device != null);
	}

	// this DatabaseOpenHelper class will actually be used to perform database
	// related operation

	public boolean checkDeviceNumber(String number) {

		Cursor cursor = database.query(TABLE_NAME, null,
				DEVICE_TABLE_COLUMN_SIM + "=?", new String[] { number }, null,
				null, null);
		return cursor.getCount() > 0;
	}

	public String getLastLocationUpdateTime(String deviceName)
	{
		
		String lastUpdated = null;
		Cursor cursor = database.query(TABLE_NAME, null,
				DEVICE_TABLE_COLUMN_NAME + "=?", new String[] {deviceName}, null,
				null, null);
		
		if (cursor.moveToFirst())
			lastUpdated = cursor.getString(cursor
					.getColumnIndex(DEVICE_TABLE_LOCATION_LAST_UPDATED));


		return lastUpdated;

	}
	
	
	public void updateLocationStatus(String time,String deviceName)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put(DEVICE_TABLE_LOCATION_LAST_UPDATED, time);
		
		database.update(TABLE_NAME,contentValues,DEVICE_TABLE_COLUMN_NAME + "=?", new String[]{deviceName});
	}
	
	public DeviceMode getDeviceByName(String deviceName) {
		DeviceMode deviceMode = null;
		Cursor cursor = database.query(TABLE_NAME, null,
				DEVICE_TABLE_COLUMN_NAME + "=?", new String[] { deviceName },
				null, null, null);
		if (cursor.moveToFirst()) {
			deviceMode = new DeviceMode();
			deviceMode.setDeviceName(cursor.getString(cursor
					.getColumnIndex("deviceName")));
			deviceMode.setDeviceIMEI(cursor.getString(cursor
					.getColumnIndex("deviceIMEI")));
			deviceMode.setDeviceSIM(cursor.getString(cursor
					.getColumnIndex("deviceSIM")));
			deviceMode.setDevicePassword(cursor.getString(cursor
					.getColumnIndex("devicePassword")));
		}
		return deviceMode;
	}

	private class DatabaseOpenHelper extends SQLiteOpenHelper {

		public DatabaseOpenHelper(Context aContext) {
			super(aContext, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase sqLiteDatabase) {
			// Create your tables here

			// CREATE TABLE if not exists Devices ([deviceID] INTEGER PRIMARY
			// KEY AUTOINCREMENT,[deviceName] text,[deviceIMEI] text,[deviceSIM]
			// text,[devicePassword] text)

			String buildSQL = "CREATE TABLE " + TABLE_NAME + "( "
					+ DEVICE_TABLE_COLUMN_ID + " INTEGER PRIMARY KEY, "
					+ DEVICE_TABLE_COLUMN_NAME + " TEXT, "
					+ DEVICE_TABLE_COLUMN_IMEI + " TEXT, "
					+ DEVICE_TABLE_COLUMN_SIM + " TEXT, "
					+ DEVICE_TABLE_COLUMN_PASSWORD + " TEXT, "
					+ DEVICE_TABLE_LOCATION_LAST_UPDATED + " TEXT, "
					+ DEVICE_TABLE_BATTERY_LAST_UPDATED + " TEXT  )";

			Log.d(TAG, "onCreate SQL: " + buildSQL);

			sqLiteDatabase.execSQL(buildSQL);
		}

		@Override
		public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion,
				int newVersion) {
			// Database schema upgrade code goes here

			String buildSQL = "DROP TABLE IF EXISTS " + TABLE_NAME;

			Log.d(TAG, "onUpgrade SQL: " + buildSQL);

			sqLiteDatabase.execSQL(buildSQL); // drop previous table

			onCreate(sqLiteDatabase); // create the table from the beginning
		}
	}
}
