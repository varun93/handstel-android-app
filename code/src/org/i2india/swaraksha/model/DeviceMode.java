package org.i2india.swaraksha.model;

public class DeviceMode
{
  private String deviceIMEI;//no need to check for this
  private String deviceName;
  private String devicePassword;
  private String deviceSIM;

  public String getDeviceIMEI()
  {
    return this.deviceIMEI;
  }

  public String getDeviceName()
  {
    return this.deviceName;
  }

  public String getDevicePassword()
  {
    return this.devicePassword;
  }

  public String getDeviceSIM()
  {
    return this.deviceSIM;
  }

  public void setDeviceIMEI(String paramString)
  {
    this.deviceIMEI = paramString;
  }

  public void setDeviceName(String paramString)
  {
    this.deviceName = paramString;
  }

  public void setDevicePassword(String paramString)
  {
    this.devicePassword = paramString;
  }

  public void setDeviceSIM(String paramString)
  {
    this.deviceSIM = paramString;
  }
}