package fi.donhut.taskyourandroid;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import fi.donhut.common.MyActivity;
import fi.donhut.common.MyTabActivity;
import fi.donhut.taskyourandroid.enumeration.TAB;
import fi.donhut.taskyourandroid.util.Constants;
import fi.donhut.taskyourandroid.util.UserPreference;

/**
 * 
 * @author Nhut Do @ 2012
 *
 */
public class MainActivity extends MyTabActivity implements OnTabChangeListener {
	
	private static final String TAG = MyActivity.TAG;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		Resources res = getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost(); // The activity TabHost
		tabHost.setOnTabChangedListener(this);
		
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent = null; // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab (to be reused)
		// Initialize a TabSpec for each tab and add it to the TabHost
		intent = new Intent().setClass(this, ActionActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		spec = tabHost.newTabSpec(TAB.ACTIONS.name()).setIndicator(getText(R.string.tab_action), res.getDrawable(R.drawable.ic_tab_actions)).setContent(intent);
		tabHost.addTab(spec);
		
		// Initialize a TabSpec for each tab and add it to the TabHost
		intent = new Intent().setClass(this, ScheduleActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		spec = tabHost.newTabSpec(TAB.SCHEDULE_TASK.name()).setIndicator(getText(R.string.tab_schedule_task), res.getDrawable(R.drawable.ic_tab_schedules)).setContent(intent);
		tabHost.addTab(spec);
		
		tabHost.setCurrentTabByTag(UserPreference.userSelectedTab.name());
		
		LayoutInflater.from(this).inflate(R.layout.activity_main, tabHost.getTabContentView(), true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	return super.onOptionsItemSelected(item);
    }

	@Override
	public void onTabChanged(String tabId) {
		UserPreference.userSelectedTab = TAB.valueOf(tabId);
//		Activity currentActivity = getCurrentActivity();
		if(Constants.DEBUG) {
			Log.d(TAG, "selected tab: "+UserPreference.userSelectedTab.name());
		}
	}
}
