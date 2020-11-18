package fi.donhut.common.util;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import fi.donhut.common.MyActivity;
import fi.donhut.taskyourandroid.enumeration.SCHEDULE_CODE;
import fi.donhut.taskyourandroid.enumeration.TASK;
import fi.donhut.taskyourandroid.object.MyTask;
import fi.donhut.taskyourandroid.receiver.AlarmReceiver;
import fi.donhut.taskyourandroid.util.Constants;

/**
 * 
 * @author Nhut Do @ 2012
 *
 */
public class SchedulerUtil {
	
	private final static String TAG = MyActivity.TAG;
	
	public static void scheduleTask(Context context, MyTask myTask) {
    	// get a Calendar object with current time
    	Calendar scheduledTime = myTask.getTime();
        Log.i(TAG, "Schedule task for: "+TimeUtil.toDisplayDDMMYYYY_HHmm(scheduledTime));
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(SCHEDULE_CODE.ACTION.name(), TASK.class.getSimpleName());
        intent.putExtra(SCHEDULE_CODE.TASK_ID.name(), myTask.getId());
        
        Integer requestCode = myTask.getId();
        PendingIntent sender = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        // Get the AlarmManager service
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(myTask.getRepeatTypes() != null && myTask.getRepeatTypes().size() > 0) {
        	alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, scheduledTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, sender);
        } else {
        	alarmManager.set(AlarmManager.RTC_WAKEUP, scheduledTime.getTimeInMillis(), sender);
        }
    }
	
	/**
	 * 
	 * @param context
	 * @param myTask
	 */
	public static void cancelScheduledTask(Context context, MyTask myTask) {
		if(Constants.DEBUG) {
			Log.d(TAG, "Cancel alarm, task id="+myTask.getId());
		}
		
		Intent intent = new Intent(context, AlarmReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, myTask.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
		
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
		
		if(Log.isLoggable(TAG, Log.INFO)) {
			Log.i(TAG, "Cancelled alarm, task id="+myTask.getId());
		}
	}
	
}
