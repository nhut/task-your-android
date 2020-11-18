package fi.donhut.common;

import fi.donhut.taskyourandroid.R;
import android.app.TabActivity;
import android.view.MenuItem;

public class MyTabActivity extends TabActivity {

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		if(super.onOptionsItemSelected(item)) {
    		return true;
    	}
//    	SharedPreferences privatePreferences = PreferenceManager.getDefaultSharedPreferences(this);
    	
    	switch (item.getItemId()) {
		case R.id.menu_about:
			MyActivity.handleMenuAbout(this);
			return true;
			
		case R.id.menu_settings:
			MyActivity.handleMenuSettings(this);
			return true;
			
		default:
			break;
		}
		return false;
    }
	
}
