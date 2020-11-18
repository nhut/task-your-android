package fi.donhut.taskyourandroid.receiver;

import java.util.Calendar;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import fi.donhut.common.util.SchedulerUtil;
import fi.donhut.common.util.TimeUtil;
import fi.donhut.taskyourandroid.R;
import fi.donhut.taskyourandroid.db.TaskOpenHelper;
import fi.donhut.taskyourandroid.object.MyTask;
import fi.donhut.taskyourandroid.util.Constants;

/**
 * This is triggered after phone reboot
 * @author Nhut Do @ 2012
 *
 */
public class PhoneStartUpReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		TaskOpenHelper taskOpenHelper = new TaskOpenHelper(context);
		List<MyTask> taskList = taskOpenHelper.getAllTask();
		for(MyTask myTask : taskList) {
			if(myTask.isActive()) {
				if(myTask.getRepeatTypes() != null && !myTask.getRepeatTypes().isEmpty()) {
					Calendar taskTime = myTask.getTime();
					taskTime = TimeUtil.timePickerToCalendar(taskTime.get(Calendar.HOUR_OF_DAY), Calendar.MINUTE);
					myTask.setTime(taskTime);
				}
				SchedulerUtil.scheduleTask(context, myTask);
				if(Constants.DEBUG) {
					Toast.makeText(context, context.getString(R.string.task_create_success)+", id="+myTask.getId()+", task="+context.getString(myTask.getTask().key)+", repeat="+myTask.getRepeatTypeForDisplay(context, true), Toast.LENGTH_LONG).show();
				}
			}
		}
		
	}

}
