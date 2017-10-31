package org.i2india.swaraksha;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.i2india.swaraksha.model.DeviceMode;
import org.i2india.swaraksha.util.AppData;

import android.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.i2india.swaraksha.persistence.DeviceDatabaseHelper;


public class MainActivity extends ActionBarActivity {

	private GoogleMap map;
	private String[] menu;
	private DrawerLayout dLayout;
	private ListView dList;

	private ActionBarDrawerToggle mDrawerToggle;
	private CustomCursorAdapter cursorAdapter;
	private String title;

	private DeviceDatabaseHelper databaseHelper;
	private DeviceMode deviceMode;//password,name,iemi,sim
	private AppData appData;
	private ArrayList<DeviceMode> devices = new ArrayList<DeviceMode>();

	//Map Testing
	private Marker marker;
	private static final LatLng MADRID = new LatLng(40.417325, -3.683081);
	private static final LatLng LONDON = new LatLng(51.511214, -0.119824);
	private static final LatLng STOCKHOLM = new LatLng(59.32893, 18.06491);
	protected static final int ENTER_DATA_REQUEST_CODE = 1;
	private String selectedLocAddress;

	//Testing
	private Context context;

	private FragmentManager manager; 
	private LocationFragment location_fragment; 
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		this.appData = (AppData) (getApplication());
		databaseHelper = new DeviceDatabaseHelper(context);
		title = "SWARAKSHA";

		map = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

		manager = getSupportFragmentManager();
		location_fragment = (LocationFragment) manager.findFragmentById(R.id.location_status);
		
		getSupportActionBar().setTitle(title);

		getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent))); 

		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.header));

		//drawer layout
		dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		dLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		//drawer list
		dList = (ListView) findViewById(R.id.left_drawer);


		View footerView =  ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.add_device, null);
		dList.addFooterView(footerView);



		new Handler().post(new Runnable() {
			@Override
			public void run() {
				cursorAdapter = new CustomCursorAdapter(MainActivity.this, databaseHelper.getAllDevices(),true);
				dList.setAdapter(cursorAdapter);
			}
		});


		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);


		mDrawerToggle = new ActionBarDrawerToggle(this, dLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			/** Called when drawer is closed */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
			}


			/** Called when a drawer is opened */
			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(title);
				super.onDrawerOpened(drawerView);
			}

		};

		dLayout.setDrawerListener(mDrawerToggle);

		dList.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
				//put in the logic here

				if (position == cursorAdapter.getCount())
				{
					startActivityForResult(new Intent(MainActivity.this, AddDeviceActivity.class), ENTER_DATA_REQUEST_CODE);
				}
				else 
				{

					ImageView checked = (ImageView) v.findViewById(R.id.checked);
					DeviceMode deviceMode = getDevices().get(position);
					int deviceID = databaseHelper.getDeviceId(deviceMode.getDeviceName());
					appData.setDeviceMod(deviceMode);
					appData.setDeviceID(deviceID);

				}

			}
		});

		dList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
		{
			public boolean onItemLongClick(AdapterView<?> parentView, View paramAnonymousView, final int position, long paramAnonymousLong)
			{
				if (position != cursorAdapter.getCount())
				{
					DeviceMode deviceMode = getDevices().get(position);
					int deviceID = databaseHelper.getDeviceId(deviceMode.getDeviceName());
					if(databaseHelper.deleteDeviceByID(deviceID))
					{
						cursorAdapter.changeCursor(databaseHelper.getAllDevices());
					}
				}

				return true;
			}
		});


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			if (dLayout.isDrawerOpen(dList)) {
				dLayout.closeDrawer(dList);
			} else {
				dLayout.openDrawer(dList);
			}
		}

		if (item.getItemId() == R.id.action_watch_settings)
		{
			Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
			startActivity(intent);
		}


		return super.onOptionsItemSelected(item);


	}

	public ArrayList<DeviceMode> getDevices()
	{
		return databaseHelper.selectAllDevices();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == ENTER_DATA_REQUEST_CODE && resultCode == RESULT_OK) {

			cursorAdapter.changeCursor(databaseHelper.getAllDevices());
		}
	}


	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState); 
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void setTitle(CharSequence title) {

		getSupportActionBar().setTitle(title);
	}


	@Override
	public void onResume(){
		Log.i("cs.fsu", "smsActivity : onResume");
		super.onResume();
		

		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("location");

		if (bundle != null) {
			Object[] pdus = (Object[])bundle.get("pdus");
			SmsMessage sms = SmsMessage.createFromPdu((byte[])pdus[0]);
			Log.i("cs.fsu", "smsActivity : SMS is <" +  sms.getMessageBody() +">");

			String message = sms.getMessageBody();
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();


			String[] lat_long = message.substring(message.indexOf('=')+1, message.indexOf('&')).split(",");
			Float latitude = Float.parseFloat(lat_long[0].trim());
			Float longitude = Float.parseFloat(lat_long[1].trim());

			LatLng currentLocation = new LatLng(latitude,longitude);


			if (marker != null) {
				marker.remove();
			}
			marker = map.addMarker(
					new MarkerOptions()
					.position(currentLocation)
					);
			marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker));
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10));

			new ReverseGeocodingTask(this).execute(currentLocation);
			
			
		    
		    
		} else
			Log.i("cs.fsu", "smsActivity : NULL SMS bundle");
	}


	private class ReverseGeocodingTask extends AsyncTask<LatLng, Void, String>{

		Context mContext;

		public ReverseGeocodingTask(Context context){

			super();

			mContext = context;

		}

		// Finding address using reverse geocoding

		@Override

		protected String doInBackground(LatLng... params) {

			String locality=null;
			Geocoder geocoder = new Geocoder(mContext);

			double latitude = params[0].latitude;

			double longitude = params[0].longitude;

			List<Address> addresses = null;

			String addressText="";

			try {

				addresses = geocoder.getFromLocation(latitude, longitude,1);

				Thread.sleep(500);

				if(addresses != null && addresses.size() > 0 ){

					Address address = addresses.get(0);

					addressText = String.format("%s", address.getAddressLine(0));
					
			        locality = addressText.substring(addressText.lastIndexOf(',') + 2);

				}

			}

			catch (IOException e) {

				e.printStackTrace();

			} catch (InterruptedException e) {

				e.printStackTrace();

			}

		

			return locality;

		}

		@Override

		protected void onPostExecute(String addressText) {
			
			location_fragment.updateLocationStatus(addressText);
			 
		}

	}



}
