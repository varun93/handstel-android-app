package org.i2india.swaraksha.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

public class DialogHelper
{
  private Context context = null;
  private SMSManagerHelper smsManagerHelper;

  public DialogHelper(Context paramContext)
  {
    this.context = paramContext;
    this.smsManagerHelper = new SMSManagerHelper(this.context);
  }

  public void confirm(Exception paramException)
  {
    String str = paramException.getMessage();
    if ((str == null) || (str.length() == 0))
      str = this.context.getString(2131034203);
    confirm(str);
  }

  public void confirm(String paramString)
  {
    confirm("OK", paramString);
  }

  public void confirm(String paramString1, String paramString2)
  {
    confirm(paramString1, paramString2, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
      }
    });
  }

  public void confirm(String paramString1, String paramString2, DialogInterface.OnClickListener paramOnClickListener)
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this.context);
    localBuilder.setMessage(paramString2);
    localBuilder.setTitle(paramString1);
    localBuilder.setCancelable(false);
    localBuilder.setPositiveButton(2131034198, paramOnClickListener);
    localBuilder.create().show();
  }

  public void send(String paramString1, String paramString2, DialogInterface.OnClickListener paramOnClickListener)
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this.context);
    localBuilder.setTitle(paramString1);
    localBuilder.setPositiveButton(2131034198, paramOnClickListener);
    localBuilder.setNegativeButton(2131034199, null);
    localBuilder.create().show();
  }

  public void send(String paramString1, final String paramString2, final String paramString3)
  {
    confirm(paramString1, paramString2, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        DialogHelper.this.smsManagerHelper.send(paramString2, paramString3);
      }
    });
  }

  public void sendSMS(String paramString1, String paramString2)
  {
    send("Send", paramString1, paramString2);
  }

  public void toast(String paramString)
  {
    Toast.makeText(this.context, paramString, 5000).show();
  }
}