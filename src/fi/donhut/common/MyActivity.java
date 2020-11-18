package fi.donhut.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.MenuItem;
import fi.donhut.taskyourandroid.R;
import fi.donhut.taskyourandroid.SettingsActivity;
import fi.donhut.taskyourandroid.util.Constants;

/**
 * 
 * @author Nhut Do @ 2012
 *
 */
public class MyActivity extends Activity {
	
	final public static String TAG = Constants.TAG; 
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(super.onOptionsItemSelected(item)) {
    		return true;
    	}
    	switch (item.getItemId()) {
		case R.id.menu_about:
			handleMenuAbout(this);
			return true;
			
		case R.id.menu_settings:
			handleMenuSettings(this);
			return true;
			
		default:
			break;
		}
		return false;
	}
	
	public static void handleMenuAbout(Context context) {
		PackageInfo packageInfo = null;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {}
		final String APPNAME = context.getText(R.string.app_name)+" "+(packageInfo == null ? "" : "v"+packageInfo.versionName);
		String message = context.getString(R.string.menu_about_message, APPNAME, context.getText(R.string.author), context.getText(R.string.app_created));
		new AlertDialog.Builder(context)
		.setTitle(context.getText(R.string.menu_about))
		.setMessage(message)
		.setPositiveButton(context.getText(R.string.button_ok), null)
		.show();
	}
	
	public static void handleMenuSettings(Context context) {
		Intent settingsIntent = new Intent(context, SettingsActivity.class);
		context.startActivity(settingsIntent);
	}
	
}
