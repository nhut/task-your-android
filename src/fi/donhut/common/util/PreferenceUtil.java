package fi.donhut.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 
 * @author Nhut Do @ 2012
 *
 */
public class PreferenceUtil {

	public static SharedPreferences getPreference(Context context) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPreferences;
	}
	
}
