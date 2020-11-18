package fi.donhut.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.util.Log;

import fi.donhut.common.MyActivity;
import fi.donhut.taskyourandroid.util.Constants;

/**
 * 
 * @author Nhut Do @ 2012
 *
 */
public class TimeUtil {
	
	private final static String TAG = MyActivity.TAG; 

	public static Calendar epocCalendarTime(int hours, int minutes, int seconds) {
		Calendar newCal = GregorianCalendar.getInstance();
		newCal.clear();
		newCal.set(Calendar.HOUR_OF_DAY, hours);
		newCal.set(Calendar.MINUTE, minutes);
		newCal.set(Calendar.SECOND, seconds);
		return newCal;
	}
	
	public static String toHHmm(Calendar cal) {
		return new SimpleDateFormat("HH:mm").format(cal.getTime());
	}
	
	public static String toDisplayDDMMYYYY(Calendar cal) {
		return new SimpleDateFormat("dd.MM.yyyy").format(cal.getTime());
	}
	
	public static String toDisplayDDMMYYYY_HHmm(Calendar cal ) {
		return new SimpleDateFormat("dd.MM.yyyy HH:mm").format(cal.getTime());
	}
	
	public static String toDisplayHHmm_DDMMYYYY(Calendar cal ) {
		return new SimpleDateFormat("HH:mm (dd.MM.yyyy)").format(cal.getTime());
	}
	
	public static Calendar timePickerToCalendar(int hourOfDay, int minute) {
		Calendar currCal = GregorianCalendar.getInstance();
		currCal.set(Calendar.SECOND, 0);
		currCal.set(Calendar.MILLISECOND, 0);
		
		Calendar compareCalendar = GregorianCalendar.getInstance();
		compareCalendar.setTimeInMillis(currCal.getTimeInMillis());
		compareCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		compareCalendar.set(Calendar.MINUTE, minute);
		
		if(Constants.DEBUG) {
			Log.d(TAG, "curr: "+TimeUtil.toDisplayDDMMYYYY_HHmm(currCal)+", compare: "+TimeUtil.toDisplayDDMMYYYY_HHmm(compareCalendar));
		}
		
		if(compareCalendar.compareTo(currCal) <= 0) {
			compareCalendar.add(Calendar.DAY_OF_YEAR, 1);
		}
		return compareCalendar;
	}
}
