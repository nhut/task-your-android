package fi.donhut.taskyourandroid;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * 
 * @author Nhut Do @ 2012
 *
 */
public class SettingsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//add the prefernces.xml layout
        addPreferencesFromResource(R.xml.preferences);
        
	}
	
}
