package org.i2india.swaraksha;

import org.i2india.swaraksha.model.DeviceMode;
import org.i2india.swaraksha.util.DialogHelper;

import android.R;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.i2india.swaraksha.persistence.DeviceDatabaseHelper;

public class AddDeviceActivity extends ActionBarActivity {

	private DeviceMode deviceMod;
	private DeviceDatabaseHelper databaseHelper;
	private EditText name;
	private EditText sim;
	private EditText password;
	private Button cont;
	private Button cancel;
	private DialogHelper dialogHelper;
	private Context context;

	public void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		setContentView(R.layout.add_user);
		getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent))); 

		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.header));

		getSupportActionBar().setTitle("Add Device");
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		init();
		setListeners();

	}

	private void setListeners()
	{
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				name.setText("");
				sim.setText("");
				password.setText("");

			}
		});


		cont.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String imeiStr = "123456789";
				String nameStr = name.getText().toString().trim();
				if(databaseHelper.isDevicePresent(nameStr))
				{
					dialogHelper.toast("Name already used");
					return;
				}
				String simStr = sim.getText().toString().trim();
				String passwordStr = password.getText().toString().trim();
				if (nameStr.length() == 0 || simStr.length() == 0 || passwordStr.length() == 0 )
				{
					dialogHelper.toast("Incomplete Information");
					return;
				}

				deviceMod = new DeviceMode();

				deviceMod.setDeviceName(nameStr);
				deviceMod.setDeviceIMEI(imeiStr);
				deviceMod.setDeviceSIM(simStr);
				deviceMod.setDevicePassword(passwordStr);
				databaseHelper.addDevice(deviceMod);
//				databaseHelper.updateLocationStatus(new Long(new Date().getTime()).toString(),nameStr);
				Intent newIntent = getIntent();
				AddDeviceActivity.this.setResult(RESULT_OK, newIntent);
				AddDeviceActivity.this.finish();

			}
		});

	}
	private void init()
	{

		this.context = this;
		databaseHelper = new DeviceDatabaseHelper(context);
		this.dialogHelper = new DialogHelper(this.context);

		name = (EditText) findViewById(R.id.name);
		sim = (EditText) findViewById(R.id.sim);
		password = (EditText) findViewById(R.id.device);
		cont = (Button) findViewById(R.id.contine);
		cancel = (Button) findViewById(R.id.cancel);


	}
}
