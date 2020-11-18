package fi.donhut.taskyourandroid.util;

import fi.donhut.taskyourandroid.db.table.TimedTask;
import fi.donhut.taskyourandroid.enumeration.TAB;

/**
 * 
 * @author Nhut Do @ 2012
 *
 */
public class UserPreference {
	
	public static TAB userSelectedTab = TAB.ACTIONS;
	public static String scheduleTaskSortOrder = TimedTask.FIELD_ID;
	
	final public static String PREF_KEY_NOTIFY_TASK_EXECUTED = "notifyTaskExecuted";
	final public static String PREF_KEY_REMOVE_TASK_AFTER_EXECUTION = "removeTaskAfterExecution";
}
