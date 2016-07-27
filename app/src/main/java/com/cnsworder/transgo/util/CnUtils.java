/**
 * @author 伊冲
 * @version 1.0
 * May 17, 2011
 */
package com.cnsworder.transgo.util;

import java.util.Calendar;

/**
 * @author 伊冲
 * @version 1.0 May 17, 2011
 */
public class CnUtils {

	private static int day = 0;
	private static int month = 0;
	private static int year = 0;
	
	private static Calendar canlendar = Calendar.getInstance();
	private static String monthstr = null;
	private static String daystr = null;
		
    public static void monthAndDay() {
		month = canlendar.get(java.util.Calendar.MONTH) + 1;
		day = canlendar.get(java.util.Calendar.DAY_OF_MONTH);
		year = canlendar.getTime().getYear() + 1900;
		
		monthstr = String.valueOf(month);
		daystr = String.valueOf(day);
		
		if(month < 10)
		{
			monthstr = "0" + monthstr;	
		}
		if(day < 10)
		{
			daystr = "0" + daystr;	
		}

    }
	
	public static String getTime() {

		String time = year + "-" +
		monthstr + "-" +
		daystr + " " +
		canlendar.getTime().getHours()+ ":" + 
		canlendar.getTime().getMinutes() + ":" +
		canlendar.getTime().getSeconds();
		
		return time;
	}

	public static boolean isOneDay(String time) {

		int m = Integer.valueOf(time.substring(5, 7));
		int d = Integer.valueOf(time.substring(8, 10));
		
		if(month - m == 0 && day - d == 0){
			return true;
		}

		return false;
	}

}
