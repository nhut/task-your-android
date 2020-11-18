package fi.donhut.common.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import fi.donhut.common.MyActivity;
import fi.donhut.taskyourandroid.MainActivity;
import fi.donhut.taskyourandroid.R;

/**
 * 
 * @author Nhut Do @ 2012
 *
 */
public class PhoneUtil {
	
	private final static String TAG = MyActivity.TAG;
	
	/**
	 * Change phone profile.
	 * @param context
	 * @param ringerMode
	 */
	public static void setProfile(Context context, int ringerMode) {
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setRingerMode(ringerMode);
	}
	
	/**
	 * Create a notification to display.
	 * @param context
	 * @param title
	 * @param text
	 * @param notificationId
	 */
	public static void createNotification(Context context, String text, int notificationId) {
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher, text, System.currentTimeMillis());
		notification.defaults = notification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		Intent notifyIntent = new Intent(context, MainActivity.class);
		
		PendingIntent intent = PendingIntent.getActivity(context, 0, notifyIntent, android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
		notification.setLatestEventInfo(context, context.getString(R.string.notification_task_done_title, context.getText(R.string.app_name), notificationId), text, intent);
		
		notificationManager.notify(notificationId, notification);
	}
}