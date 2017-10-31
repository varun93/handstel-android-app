package org.i2india.swaraksha;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.i2india.swaraksha.persistence.DeviceDatabaseHelper;

public class SmsReceiver extends BroadcastReceiver
{

	private DeviceDatabaseHelper databaseHelper;

	@Override
	public void onReceive(Context context, Intent intent) {

		Log.i("cs.fsu", "smsReceiver: SMS Received");

		databaseHelper = new DeviceDatabaseHelper(context);
		
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			
			Log.i("cs.fsu", "smsReceiver : Reading Bundle");

			Object[] pdus = (Object[])bundle.get("pdus");
			SmsMessage sms = SmsMessage.createFromPdu((byte[])pdus[0]);

			String phoneNumber = sms.getDisplayOriginatingAddress();
			String message = sms.getDisplayMessageBody();

			int duration = Toast.LENGTH_LONG;

			try
			{
				if(databaseHelper.checkDeviceNumber(phoneNumber))
				{
					Intent myIntent = new Intent(context,MainActivity.class);
					myIntent.putExtra("location", bundle);
					myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(myIntent);
				}
				else
					Toast.makeText(context, "false", duration).show();
					
			
			}
			catch(Exception e)
			{
				
				Toast.makeText(context, "something wrong", duration).show();
			}
			
	



		}
	}


}