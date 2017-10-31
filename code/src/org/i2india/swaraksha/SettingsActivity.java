package org.i2india.swaraksha;

import java.util.ArrayList;

import org.i2india.swaraksha.util.AppData;
import org.i2india.swaraksha.util.EntryItem;
import org.i2india.swaraksha.util.Item;
import org.i2india.swaraksha.util.SectionItem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class SettingsActivity extends ActionBarActivity {

	private  static final int SET_SOS_NUMBER = 1;
	private static final int SET_TRACKING_INTERVAL = 2;
	private static final int SET_LOW_BATTERY_ALARM = 3;
	private static final int VIEW_DEVICE_INFORMATION = 4;
	private static final int CHANGE_DEVICE_PASSWORD = 5;
	private static final int SET_APN = 7;
	private static final int RESTORE_FACTORY_SETTINGS = 8;

	private static final String SET_ALARM_VALUE = "04";
	private static String SET_FACTORY_RESET = "";

	private String title = "Settings";
	private ListView list;
	private View infoView;
	private ArrayList<Item> items = new ArrayList<Item>();
	private Context context;
	private AppData appData;

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		context = this;
		this.appData = ((AppData)getApplication());
		list = (ListView) findViewById(R.id.settings);

		getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent))); 

		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.header));

		getSupportActionBar().setTitle(title);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);


		items.add(new SectionItem("General Settings"));
		items.add(new EntryItem("Set SOS number"));
		items.add(new EntryItem("Set tracking interval"));
		items.add(new EntryItem("Set low battery alarm"));
		items.add(new EntryItem("View device information"));
		items.add(new EntryItem("Change device password"));

		items.add(new SectionItem("Advanced Settings"));
		items.add(new EntryItem("Set APN"));
		items.add(new EntryItem("Restore factory settings"));


		EntryAdapter adapter = new EntryAdapter(this, items);
		list.setAdapter(adapter);



		list.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				if(appData.getDeviceMod().getDeviceName() != null)
				{
					switch(position)
					{
					default : return;

					case SET_SOS_NUMBER:
						Intent localIntent = new Intent();
						localIntent.setClass(SettingsActivity.this, SOSActivity.class);
						startActivity(localIntent);
						break;

					case SET_TRACKING_INTERVAL:
						AlertDialog.Builder alert = new AlertDialog.Builder(context);
						alert.setTitle("Set Tracking Interval"); //Set Alert dialog title here
						alert.setMessage("Time Interval"); //Message here
						final EditText input = new EditText(context);
						alert.setView(input);

						alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								// Set an EditText view to get user input

								String interval = input.getEditableText().toString().trim();

								if ((interval.length() == 0) || (Integer.parseInt(input.getText().toString().trim()) < 0) || (Integer.parseInt(input.getText().toString().trim()) > 99999))
								{
									Toast.makeText(context,"Enter a valid interval", 3000).show();
									return;
								}
								SmsManager localSmsManager = SmsManager.getDefault();
								PendingIntent localPendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(), 0);
								String str = "08#" + interval;
								localSmsManager.sendTextMessage(appData.getDeviceMod().getDeviceSIM(), null, str, localPendingIntent, null);
								dialog.cancel();
								Toast.makeText(context,"Interval successfully set", 3000).show();

							}
						});

						alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								// Canceled.
								dialog.cancel();
							}
						});
						AlertDialog alertDialog = alert.create();
						alertDialog.show();

						break;

					case SET_LOW_BATTERY_ALARM:
						SmsManager smsManager = SmsManager.getDefault(); 
						PendingIntent pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this.context, 0, new Intent(), 0);
						smsManager.sendTextMessage(appData.getDeviceMod().getDeviceSIM(), null,SET_ALARM_VALUE, pendingIntent, null);
						Toast.makeText(SettingsActivity.this.context,"Successfully set", 3000).show();
						break;

					case VIEW_DEVICE_INFORMATION:
						//just put a dialog box displaying the information

						AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
						LayoutInflater inflater = LayoutInflater.from(SettingsActivity.this);
						infoView = inflater.inflate(R.layout.devicen_info, null);
						builder.setView(infoView);

						builder.setTitle("Device Information");
						TextView deviceName = (TextView) infoView.findViewById(R.id.device_name);
						TextView deviceNumber = (TextView) infoView.findViewById(R.id.device_number);
						deviceName.setText(appData.getDeviceMod().getDeviceName());
						deviceNumber.setText(appData.getDeviceMod().getDeviceSIM());

						builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {

							}
						});

						builder.setCancelable(false);
						AlertDialog dialog = builder.create();
						dialog.show();

						break;
					case CHANGE_DEVICE_PASSWORD:
						//return a dialog box and change the password
						Toast.makeText(SettingsActivity.this.context,"Clicked on change device password", 3000).show();
						break;
					case SET_APN:
						//wont do anything as of now
						Toast.makeText(SettingsActivity.this.context,"Clicked on set apn", 3000).show();
						break;
					case RESTORE_FACTORY_SETTINGS:
						smsManager = SmsManager.getDefault();
						pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(), 0);
						SET_FACTORY_RESET = "77#99#" + appData.getDeviceMod().getDevicePassword();
						smsManager.sendTextMessage(appData.getDeviceMod().getDeviceSIM(), null, SET_FACTORY_RESET, pendingIntent, null);
						Toast.makeText(SettingsActivity.this, "Factory settings done", 1).show();
						break;
					}



				}
				else
					Toast.makeText(context,"Please select a device", 3000).show();


			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		menu.findItem(R.id.action_watch_settings).setVisible(false);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		return super.onOptionsItemSelected(item);
	}


	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState); 

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void setTitle(CharSequence title) {

		getSupportActionBar().setTitle(title);
	}



}
