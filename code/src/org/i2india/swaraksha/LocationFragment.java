package org.i2india.swaraksha;

import java.util.ArrayList;
import java.util.Date;

import org.i2india.swaraksha.util.AppData;
import org.i2india.swaraksha.util.DateUtil;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.i2india.swaraksha.persistence.DeviceDatabaseHelper;

public class LocationFragment extends Fragment{

	private View view;
	private ListView listView;
	private View headerView;
	private TextView title_head;
	private TextView last_updated;
	private AppData appData;
	private DeviceDatabaseHelper databaseHelper; 

	ArrayList<String> adapterList = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view =  inflater.inflate(R.layout.location_fragment, container, false);
		listView = (ListView) view.findViewById(R.id.location_statusF);

		headerView =  ((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.header_view, null);
		populateAdapter();
		appData = (AppData) getActivity().getApplicationContext();
		addListeners();

		return view;
	}

	private void addListeners() {

		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {

				if(position != 0)
				{
					if(appData.getDeviceMod().getDeviceName() != null)
					{
						

						SmsManager smsManager = SmsManager.getDefault(); 
						PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent(), 0);
						smsManager.sendTextMessage(appData.getDeviceMod().getDeviceSIM(), null,"http://www.gps166.com/showMap.aspx?n=12.914155,77.631848&lang=en", pendingIntent, null);
						Toast.makeText(getActivity(),"Successfully set", 3000).show();
					}
					else
						Toast.makeText(getActivity(),"Please select a device", 3000).show();
				}

			}
		});

	}

	private void populateAdapter() {

		adapterList.add("GET CURRENT LOCATION");
		listView.addHeaderView(headerView);
		listView.setAdapter(new LocationAdapter(getActivity(),adapterList));
		title_head = (TextView) headerView.findViewById(R.id.location);
		last_updated = (TextView) headerView.findViewById(R.id.time);


	}

	public void updateLocationStatus(String location)
	{
		title_head.setText(location);
		databaseHelper = new DeviceDatabaseHelper(getActivity());
		Long lastUpdated = Long.parseLong(databaseHelper.getLastLocationUpdateTime(appData.getDeviceMod().getDeviceName()));
		String difference = DateUtil.timeDifference(new Date().getTime(),lastUpdated);
		last_updated.setText("Location last updated : " + difference + " ago");
	}

	private class LocationAdapter extends BaseAdapter
	{
		private Activity cntx;
		private ArrayList<String> data;

		public LocationAdapter(Activity context,ArrayList<String> data)
		{
			// TODO Auto-generated constructor stub
			this.cntx=context;
			this.data = data;
		}

		public int getCount()
		{
			// TODO Auto-generated method stub
			return data.size();
		}

		public Object getItem(int position)
		{
			// TODO Auto-generated method stub
			return data.get(position);
		}

		public long getItemId(int position)
		{
			// TODO Auto-generated method stub
			return data.size();
		}

		public View getView(final int position, View convertView, ViewGroup parent)
		{
			View row=null;
			LayoutInflater inflater=cntx.getLayoutInflater();
			row=inflater.inflate(R.layout.location_update_item, null);
			TextView tv = (TextView) row.findViewById(R.id.location_title);
			tv.setTypeface(null, Typeface.BOLD);
			tv.setText(data.get(position));
			return row;
		}


	}


}
