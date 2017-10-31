package org.i2india.swaraksha.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

public class SMSManagerHelper
{
	private Context context = null;

	public SMSManagerHelper(Context paramContext)
	{
		this.context = paramContext;
	}

	public void send(String paramString1, String paramString2)
	{
		SmsManager.getDefault().sendTextMessage(paramString2, null, paramString1, PendingIntent.getBroadcast(this.context, 0, new Intent(), 0), null);
	}
}