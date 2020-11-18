package fi.donhut.taskyourandroid.enumeration;

import java.util.Calendar;

import fi.donhut.taskyourandroid.R;

/**
 * Repeat types
 * @author Nhut Do @ 2012
 *
 */
public enum REPEAT_TYPE {
	MONDAY(R.string.REPEAT_TYPE_MONDAY, R.string.REPEAT_TYPE_MONDAY_S, Calendar.MONDAY), TUESDAY(R.string.REPEAT_TYPE_TUESDAY, R.string.REPEAT_TYPE_TUESDAY_S, Calendar.TUESDAY), 
	WEDNESDAY(R.string.REPEAT_TYPE_WEDNESDAY, R.string.REPEAT_TYPE_WEDNESDAY_S, Calendar.WEDNESDAY), THURSDAY(R.string.REPEAT_TYPE_THURSDAY, R.string.REPEAT_TYPE_THURSDAY_S, Calendar.THURSDAY), FRIDAY(R.string.REPEAT_TYPE_FRIDAY, R.string.REPEAT_TYPE_FRIDAY_S, Calendar.FRIDAY), 
	SATURDAY(R.string.REPEAT_TYPE_SATURDAY, R.string.REPEAT_TYPE_SATURDAY_S, Calendar.SATURDAY), SUNDAY(R.string.REPEAT_TYPE_SUNDAY, R.string.REPEAT_TYPE_SUNDAY_S, Calendar.SUNDAY);
	
	public int key;
	public int keyShort;
	public int dayOfWeek;
	
	private REPEAT_TYPE(int key, int keyShort, int dayOfWeek) {
		this.key = key;
		this.keyShort = keyShort;
		this.dayOfWeek = dayOfWeek;
	}
}
