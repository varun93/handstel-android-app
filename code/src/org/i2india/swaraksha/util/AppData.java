package org.i2india.swaraksha.util;

import org.i2india.swaraksha.model.DeviceMode;

import android.app.Application;

public class AppData extends Application
{
	private int deviceID;
	DeviceMode deviceMode = new DeviceMode();

	public int getDeviceID()
	{
		return this.deviceID;
	}

	public DeviceMode getDeviceMod()
	{
		return this.deviceMode;
	}

	public void setDeviceID(int paramInt)
	{
		this.deviceID = paramInt;
	}

	public void setDeviceMod(DeviceMode paramDeviceMode)
	{
		this.deviceMode = paramDeviceMode;
	}
}