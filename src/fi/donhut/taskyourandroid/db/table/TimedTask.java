package fi.donhut.taskyourandroid.db.table;

public class TimedTask {
	
	public final static String FIELD_ID = "id";
	public final static String FIELD_NAME = "name";
	public final static String FIELD_TASK = "task";
	public final static String FIELD_TIME = "time";
	public final static String FIELD_REPEAT = "repeat";
	public final static String FIELD_CREATED = "created";
	public final static String FIELD_ACTIVE = "active";
	public final static String FIELD_REMOVE_AFTER_EXECUTION = "removeAfterExecution";
	public final static String FIELD_LAST_EXECUTION = "lastExecution";
	
	public final static String[] ALL_FIELDS = new String[] {TimedTask.FIELD_ID, TimedTask.FIELD_NAME, TimedTask.FIELD_TASK, TimedTask.FIELD_TIME, TimedTask.FIELD_REPEAT, TimedTask.FIELD_CREATED, TimedTask.FIELD_ACTIVE, TimedTask.FIELD_REMOVE_AFTER_EXECUTION, TimedTask.FIELD_LAST_EXECUTION};
}
