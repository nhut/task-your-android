package fi.donhut.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;

/**
 * 
 * @author Nhut Do @ 2012
 *
 */
public class ConnectivityUtil {

	/**
	 * Check if device have internet connection turned on
	 * @return
	 */
	public static boolean isInternetConnection(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
	
	private static NetworkInfo getMobileInternetNetworkInfo(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = null;
		if (connectivityManager != null) {
			networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		}
		return networkInfo;
	}
	
	public static boolean isMobileInternetAvailable(Context context) {
		NetworkInfo networkInfo = getMobileInternetNetworkInfo(context);
		if(networkInfo == null) {
			return false;
		}
		return networkInfo.isAvailable();
	}
	
	public static boolean isMobileInternetEnable(Context context) {
		NetworkInfo networkInfo = getMobileInternetNetworkInfo(context);
		if(networkInfo == null) {
			return false;
		}
		if(networkInfo.getState().compareTo(State.CONNECTED) == 0) {
			return true;
		}
		return false;
	}
	
	public static boolean isMobileInternetConnected(Context context) {
		NetworkInfo networkInfo = getMobileInternetNetworkInfo(context);
		if(networkInfo == null) {
			return false;
		}
		return networkInfo.isConnected();
	}
	
	public static void setMobileDataEnabled(Context context, boolean enabled) throws Exception {
	    final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    final Class<?> conmanClass = Class.forName(conman.getClass().getName());
	    final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
	    iConnectivityManagerField.setAccessible(true);
	    final Object iConnectivityManager = iConnectivityManagerField.get(conman);
	    final Class<?> iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
	    final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
	    setMobileDataEnabledMethod.setAccessible(true);

	    setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
	}
	
	private static NetworkInfo getWifiNetworkInfo(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = null;
		if (connectivityManager != null) {
			networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		}
		return networkInfo;
	}
	
	public static boolean isWifiEnabled(Context context) {
		NetworkInfo networkInfo = getWifiNetworkInfo(context);
		if(networkInfo == null) {
			return false;
		}
		return networkInfo.isAvailable();
	}
	
	public static boolean isWifiConnected(Context context) {
		NetworkInfo networkInfo = getWifiNetworkInfo(context);
		if(networkInfo == null) {
			return false;
		}
		return networkInfo.isConnected();
	}
	
	public static boolean switchWifi(Context context, boolean state) {
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		return wifi.setWifiEnabled(state);
	}
	
	public static boolean isDeviceHaveBluetooth(Context context) {
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(bluetoothAdapter == null) {
			return false;
		}
		return true;
	}
	
	public static boolean isBluetoothEnabled(Context context) {
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(bluetoothAdapter == null) {
			return false;
		}
		return bluetoothAdapter.isEnabled();
	}
	
	public static BluetoothAdapter getBluetoothAdapter(Context context) {
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		return bluetoothAdapter;
	}
	
}
