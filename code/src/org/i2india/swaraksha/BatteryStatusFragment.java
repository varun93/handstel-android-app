package org.i2india.swaraksha;

import java.util.ArrayList;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class BatteryStatusFragment extends Fragment{

	private View view;
	private ListView listView;
	private View headerView;
	private TextView title_head;
	private TextView last_updated;
	ArrayList<String> batteryList = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view =  inflater.inflate(R.layout.battery_fragment, container, false);
		listView = (ListView) view.findViewById(R.id.battery_statusF);
		headerView =  ((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.header_view, null);
		listView.addHeaderView(headerView);
		populateAdapter();
		addListeners();
		return view;
	}


	private void addListeners() {


	}


	private void populateAdapter() {

		batteryList.add("UPDATE BATTERY STATUS NOW");
		listView.setAdapter(new BatteryAdapter(getActivity(),batteryList));
		title_head = (TextView) headerView.findViewById(R.id.location);
		last_updated = (TextView) headerView.findViewById(R.id.time);
		last_updated.setText("Last updated : 21 hours ago");

	}

	public void updateBatteryStatus(String batteryStatus)
	{
		
	}
	
	private class BatteryAdapter extends BaseAdapter
	{
		private Activity cntx;
		private ArrayList<String> data;

		public BatteryAdapter(Activity context,ArrayList<String> data)
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
			row=inflater.inflate(R.layout.battery_update_item, null);
			TextView tv = (TextView) row.findViewById(R.id.battery_title);
			tv.setTypeface(null, Typeface.BOLD);
			tv.setText(data.get(position));
			return row;
		}


	}

}
