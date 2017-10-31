package org.i2india.swaraksha.util;


public class DateUtil {
	
	
	public static String timeDifference(Long current_time,Long old_time)
	{
		String difference = "";
		long diff = current_time - old_time;
		int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
		if(diffDays > 0)
		{
			difference = diffDays + "days";
			return difference;

		}

		int diffhours = (int) (diff / (60 * 60 * 1000));
		if(diffhours > 0)
		{
			difference = diffhours +" " +  "hours";
			return difference;

		}
		int diffmin = (int) (diff / (60 * 1000));
		if(diffmin > 0 )
		{
			difference = diffmin  +" " +"minutes";
		
			return difference;
		}
		int diffsec = (int) (diff / (1000));
		if(diffsec > 0 )
		{
			difference = diffsec + " " +"seconds";
		
			return difference;
		}


		return null;

	}
	

}
