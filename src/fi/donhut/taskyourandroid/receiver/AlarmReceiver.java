package fi.donhut.taskyourandroid.receiver;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import fi.donhut.common.MyActivity;
import fi.donhut.common.util.ConnectivityUtil;
import fi.donhut.common.util.PhoneUtil;
import fi.donhut.common.util.PreferenceUtil;
import fi.donhut.taskyourandroid.R;
import fi.donhut.taskyourandroid.db.TaskOpenHelper;
import fi.donhut.taskyourandroid.enumeration.REPEAT_TYPE;
import fi.donhut.taskyourandroid.enumeration.SCHEDULE_CODE;
import fi.donhut.taskyourandroid.enumeration.TASK;
import fi.donhut.taskyourandroid.object.MyTask;
import fi.donhut.taskyourandroid.util.Constants;
import fi.donhut.taskyourandroid.util.UserPreference;

/**
 * 
 * @author Nhut Do @ 2012
 *
 */
public class AlarmReceiver extends BroadcastReceiver {
	
	final private static String TAG = MyActivity.TAG;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			Bundle bundle = intent.getExtras();
			String message = bundle.getString(SCHEDULE_CODE.ACTION.name());
			if(message == null) {
				if(Log.isLoggable(TAG, Log.INFO)) {
					Log.i(TAG, "Nothing to handle!");
				}
				return ;
			}
			if(Constants.DEBUG) {
				Log.d(TAG, "alarm received: "+message+", "+TASK.class.getSimpleName());
			}
			if(message.equalsIgnoreCase(TASK.class.getSimpleName())) {
				Integer taskId = bundle.getInt(SCHEDULE_CODE.TASK_ID.name());
				TaskOpenHelper taskOpenHelper = new TaskOpenHelper(context);
				
				MyTask myTask = taskOpenHelper.getTask(taskId);
				if(Constants.DEBUG) {
					Log.d(TAG, "Task found? taskId="+taskId+", "+(myTask == null ? "null" : myTask.getId().toString()+", active="+myTask.isActive()));
				}
				if(myTask != null && myTask.isActive()) {
					boolean haveRepeats = (myTask.getRepeatTypes() != null && myTask.getRepeatTypes().size() > 0);
					if(haveRepeats) {
						Calendar currentCal = GregorianCalendar.getInstance();
						int dayOfWeek = currentCal.get(Calendar.DAY_OF_WEEK);
						boolean isFound = false;
						for(REPEAT_TYPE repeatType : myTask.getRepeatTypes()) {
							if(dayOfWeek == repeatType.dayOfWeek) {
								isFound = true;
							}
						}
						if(isFound) {
							//check if it already executed for today
							Calendar lastExecutionCal = myTask.getLastExecution();
							if(lastExecutionCal != null) {
								if(currentCal.get(Calendar.DAY_OF_YEAR) == lastExecutionCal.get(Calendar.DAY_OF_YEAR)) {
									//NOT SUPPOSE TO TRIGGER AGAIN FOR TODAY
									return ;
								}
							}
							
						} else {
							//NOT SUPPOSE TO TRIGGER FOR TODAY
							return ;
						}
					}
					try {
					switch (myTask.getTask()) {
					case CHANGE_PROFILE_ACTIVE:
						PhoneUtil.setProfile(context, AudioManager.RINGER_MODE_NORMAL);
						break;
					case CHANGE_PROFILE_SILENT:
						PhoneUtil.setProfile(context, AudioManager.RINGER_MODE_SILENT);
						break;
					case CHANGE_PROFILE_VIBRATE:
						PhoneUtil.setProfile(context, AudioManager.RINGER_MODE_VIBRATE);
						break;
					case CHANGE_PROFILE_PLANE:
						break;
						
					case ENABLE_MOBILE_DATA:
						ConnectivityUtil.setMobileDataEnabled(context, true);
						break;
					case DISABLE_MOBILE_DATA:
						ConnectivityUtil.setMobileDataEnabled(context, false);
						break;
						
					case ENABLE_WIFI:
						ConnectivityUtil.switchWifi(context, true);
						break;
					case DISABLE_WIFI:
						ConnectivityUtil.switchWifi(context, false);
						break;
						
					case ENABLE_BLUETOOTH:
						BluetoothAdapter bluetoothAdapter = ConnectivityUtil.getBluetoothAdapter(context);
						if(bluetoothAdapter == null) {
							return ;
						}
						bluetoothAdapter.enable();
						break;
					case DISABLE_BLUETOOTH:
						BluetoothAdapter bluetoothAdapter2 = ConnectivityUtil.getBluetoothAdapter(context);
						if(bluetoothAdapter2 == null) {
							return ;
						}
						bluetoothAdapter2.disable();
						break;
	
					default:
						break;
					}
					} catch (Exception e) {
						if(Log.isLoggable(TAG, Log.ERROR)) {
							Log.e(TAG, "Failed to execute task id="+myTask.getId()+", name="+myTask.getTask().name(), e);
						}
					}
					if(Constants.DEBUG) {
						Toast.makeText(context, "Task run done! have repeats: "+haveRepeats, Toast.LENGTH_LONG).show();
					}
					SharedPreferences preferences = PreferenceUtil.getPreference(context);
					if(haveRepeats) {
						myTask.setLastExecution(GregorianCalendar.getInstance());
						taskOpenHelper.updateTaskActive(myTask);
						
					} else {
						boolean removeAfterTaskExecution = preferences.getBoolean(UserPreference.PREF_KEY_REMOVE_TASK_AFTER_EXECUTION, Boolean.FALSE);
						if(Constants.DEBUG) {
							Toast.makeText(context, "alarm removeAfterTaskExecution:"+removeAfterTaskExecution, Toast.LENGTH_LONG).show();
						}
						if(removeAfterTaskExecution) {
							taskOpenHelper.deleteRow(myTask);
						} else {
							//disable task when there is no repeat options set
							myTask.setActive(false);
							myTask.setLastExecution(GregorianCalendar.getInstance());
							taskOpenHelper.updateTaskActive(myTask);
						}
					}
					boolean notifyAfterTaskExecuted = preferences.getBoolean(UserPreference.PREF_KEY_NOTIFY_TASK_EXECUTED, Boolean.FALSE);
					if(Constants.DEBUG) {
						Toast.makeText(context, "alarm notify:"+notifyAfterTaskExecuted, Toast.LENGTH_LONG).show();
					}
					if(notifyAfterTaskExecuted) {
						final String NOTIFICATION_TEXT = context.getString(R.string.notification_task_done, context.getString(myTask.getTask().key));
						PhoneUtil.createNotification(context, NOTIFICATION_TEXT, myTask.getId());
					}
					
				}
				
			}
			
		} catch (Exception e) {
			final String ERROR_TEXT = "There was an error somewhere, but we still received an alarm";
			Toast.makeText(context, ERROR_TEXT, Toast.LENGTH_SHORT).show();
			Log.e(TAG, ERROR_TEXT, e);
		}
	}

}
