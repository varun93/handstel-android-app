package org.i2india.swaraksha;

import org.i2india.swaraksha.util.AppData;

import android.R;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SOSActivity extends ActionBarActivity {

	private EditText sos_one;
	private EditText sos_two;
	private Button contnue;
	private Button cancel;
	private Context context;
	private AppData appData;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sos);
		context = this;
		appData = (AppData) getApplication();
		sos_one = (EditText) findViewById(R.id.editSos1);
		sos_two = (EditText) findViewById(R.id.editSos2);
		contnue = (Button) findViewById(R.id.contineSOS);
		cancel = (Button) findViewById(R.id.cancelSOS);
		getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent))); 

		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.header));

		getSupportActionBar().setTitle("Set SOS Numbers");
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		setListeners();
	}

	private void setListeners() {
		contnue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				SmsManager smsManager;
				PendingIntent pendingIntent;


				String sos1 = sos_one.getText().toString().trim();
				String sos2 = sos_two.getText().toString().trim();

				if(sos1.length() == 0 && sos2.length()==0)
				{
					Toast.makeText(context,"Please enter atleast one number", 3000).show();
					return;

				}
				if(sos1.matches("[0-9]{11}"))
				{
					String messageContent = ("1SOS#" + appData.getDeviceMod().getDevicePassword() + "#" + sos1);
					smsManager = SmsManager.getDefault(); 
					pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(), 0);
					smsManager.sendTextMessage(appData.getDeviceMod().getDeviceSIM(), null, messageContent, pendingIntent, null);
					Toast.makeText(context,"SOS 1 successfully set", 3000).show();
				}

				if(sos2.matches("[0-9]{11}"))
				{
					String messageContent = ("2SOS#" + appData.getDeviceMod().getDevicePassword() + "#" + sos2);
					smsManager = SmsManager.getDefault(); 
					pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(), 0);
					smsManager.sendTextMessage(appData.getDeviceMod().getDeviceSIM(), null, messageContent, pendingIntent, null);
					Toast.makeText(context,"SOS 2 successfully set", 3000).show();	
				}

				else
				{
					Toast.makeText(context,"Please enter a 11 digit number", 3000).show();	
				}

				finish();

			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	
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
