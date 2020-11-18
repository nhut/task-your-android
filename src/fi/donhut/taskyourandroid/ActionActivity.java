package fi.donhut.taskyourandroid;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import fi.donhut.common.MyActivity;
import fi.donhut.common.util.ConnectivityUtil;
import fi.donhut.taskyourandroid.util.Constants;

/**
 * Action tab screen actions.
 * @author Nhut Do @ 11.2012
 *
 */
public class ActionActivity extends MyActivity {
	
	private TextView textStatus;
	private AudioManager audioManager;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);
        
        // Look up the AdView as a resource and load a request.
	    AdView adView = (AdView)this.findViewById(R.id.adView);
	    adView.loadAd(new AdRequest());
	    
	    //for testing ads, activity_action.xml, ads:testDevices="TEST_EMULATOR,355558041940881"
	    
        //GET COMPONENTS
        textStatus = (TextView) findViewById(R.id.textViewStatus);
		audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		
		//UPDATE CURRENT MODE TEXT
		changeSilentMode(audioManager.getRingerMode(), false);
		//UPDATE INTERNET STATUS TEXT
		updateInternetStatus();
		
//	    if(Constants.DEBUG) {
//		    TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//		    String uid = tManager.getDeviceId();
//		    Log.d(TAG, uid);
//	    }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	changeSilentMode(audioManager.getRingerMode(), false);
    	updateInternetStatus();
    	super.onRestoreInstanceState(savedInstanceState);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    }
    
    /**
     * Get profile mode text.
     * @return profile mode clear text
     */
    private CharSequence getProfileModeText() {
		switch (audioManager.getRingerMode()) {
		case AudioManager.RINGER_MODE_NORMAL:
			updateProfileStatus(true, false, false);
			return getText(R.string.button_mode_normal);
			
		case AudioManager.RINGER_MODE_SILENT:
			updateProfileStatus(false, true, false);
			return getText(R.string.button_mode_silent);
			
		case AudioManager.RINGER_MODE_VIBRATE:
			updateProfileStatus(false, false, true);
			return getText(R.string.button_mode_vibrate);

		default:
			updateProfileStatus(false, false, false);
			return "?";
		}
	}
	
    /**
     * Change to silent mode
     * @param ringerMode
     * @param showToast
     */
	private void changeSilentMode(int ringerMode, boolean showToast) {
		audioManager.setRingerMode(ringerMode);
		CharSequence profileText = getProfileModeText();
		textStatus.setText(getString(R.string.switch_profile, profileText));
		if(showToast) {
			Toast.makeText(this, getString(R.string.toast_profile_activated, profileText), Toast.LENGTH_SHORT).show();
		}
	}
	
	private void updateProfileStatus(boolean normal, boolean silent, boolean vibrate) {
		ToggleButton normalButton = (ToggleButton) findViewById(R.id.ToggleButtonNormal);
		normalButton.setChecked(normal);
		
		ToggleButton silentButton = (ToggleButton) findViewById(R.id.ToggleButtonSilent);
		silentButton.setChecked(silent);
		
		ToggleButton vibrateButton = (ToggleButton) findViewById(R.id.ToggleButtonVibrate);
		vibrateButton.setChecked(vibrate);
	}
	
	/**
	 * Update internet status text
	 */
	private void updateInternetStatus() {
		ToggleButton mobileInternetButton = (ToggleButton) findViewById(R.id.toggleButtonMobileData);
		if(ConnectivityUtil.isMobileInternetAvailable(this)) {
			mobileInternetButton.setEnabled(true);
			mobileInternetButton.setChecked(ConnectivityUtil.isMobileInternetEnable(this));
		} else {
			mobileInternetButton.setEnabled(false);
		}
		
		ToggleButton wifiToggleButton = (ToggleButton) findViewById(R.id.toggleButtonWiFi);
		wifiToggleButton.setChecked(ConnectivityUtil.isWifiEnabled(this));
		
		ToggleButton bluetoothToggleButton = (ToggleButton) findViewById(R.id.toggleButtonBT);
		if(ConnectivityUtil.isDeviceHaveBluetooth(this)) {
			bluetoothToggleButton.setEnabled(true);
			bluetoothToggleButton.setChecked(ConnectivityUtil.isBluetoothEnabled(this));
		} else {
			bluetoothToggleButton.setEnabled(false);
		}
		
		TextView textView = (TextView) findViewById(R.id.textViewConnectivity);
		textView.setText(getString(R.string.connectivity, (ConnectivityUtil.isInternetConnection(this) ? getString(R.string.yes) : getString(R.string.no))));
	}
	
	/**
	 * Button action
	 * @param view
	 */
	public void doChangeSilentMode(View view) {
		changeSilentMode(AudioManager.RINGER_MODE_SILENT, true);
	}
	
	/**
	 * Button action
	 * @param view
	 */
	public void doChangeNormalMode(View view) {
		changeSilentMode(AudioManager.RINGER_MODE_NORMAL, true);
	}
	
	/**
	 * Button action
	 * @param view
	 */
	public void doChangeVibrateMode(View view) {
		changeSilentMode(AudioManager.RINGER_MODE_VIBRATE, true);
	}
	
	/**
	 * Button action
	 * @param view
	 */
	public void doSwitchMobileData(View view) {
		ToggleButton toggleButton = (ToggleButton)view;
		boolean newState = toggleButton.isChecked();
		try {
			ConnectivityUtil.setMobileDataEnabled(this, newState);
			toggleButton.setChecked(newState);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Toast.makeText(this, getString(R.string.toast_mobile_data_switched)+" "+(newState ? getString(R.string.on) : getString(R.string.off)), Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Button action
	 * @param view
	 */
	public void doSwitchWifi(View view) {
		ToggleButton toggleButton = (ToggleButton)view;
		boolean newState = toggleButton.isChecked();
		if(ConnectivityUtil.switchWifi(this, newState)) {
			toggleButton.setChecked(newState);
			Toast.makeText(this, getString(R.string.toast_wifi_switched)+" "+(newState ? getString(R.string.on) : getString(R.string.off)), Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, getString(R.string.toast_wifi_switched_fail)+" "+newState, Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * Button action
	 * @param view
	 */
	public void doSwitchBluetooth(View view) {
		ToggleButton toggleButton = (ToggleButton)view;
		boolean newState = toggleButton.isChecked();
		
		BluetoothAdapter bluetoothAdapter = ConnectivityUtil.getBluetoothAdapter(this);
		if(bluetoothAdapter == null) {
			return ;
		}
		if(newState) {
			if(!bluetoothAdapter.isEnabled()) {
				bluetoothAdapter.enable();
			}
		} else {
			if(bluetoothAdapter.isEnabled()) {
				bluetoothAdapter.disable();
			}
		}
		toggleButton.setChecked(newState);
		Toast.makeText(this, getString(R.string.toast_bluetooth_switched)+" "+(newState ? getString(R.string.on) : getString(R.string.off)), Toast.LENGTH_SHORT).show();
	}
	
}
