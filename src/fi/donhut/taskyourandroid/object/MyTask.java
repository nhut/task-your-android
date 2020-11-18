package fi.donhut.taskyourandroid.object;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import fi.donhut.taskyourandroid.db.table.TimedTask;
import fi.donhut.taskyourandroid.enumeration.REPEAT_TYPE;
import fi.donhut.taskyourandroid.enumeration.TASK;

/**
 * 
 * @author Nhut Do @ 2012
 *
 */
public class MyTask {
	
	private Integer id = null;
	private TASK task = null;
	private Calendar time = null;
	public final static String DELIMETER = ","; 
	private List<REPEAT_TYPE> repeatTypes = new ArrayList<REPEAT_TYPE>();
	private Calendar created = null;
	private boolean active = true;
	private boolean removeAfterExecution = true;
	private Calendar lastExecution = null;
	
	public MyTask() {
	}
	
	public MyTask(Cursor cursor) {
		setId(cursor.getInt(cursor.getColumnIndex(TimedTask.FIELD_ID)));
		
		setTask(TASK.valueOf(cursor.getString(cursor.getColumnIndex(TimedTask.FIELD_TASK))));
		
		long timeMilliseconds = cursor.getInt(cursor.getColumnIndex(TimedTask.FIELD_TIME));
		Calendar timeCal = GregorianCalendar.getInstance();
		timeCal.setTimeInMillis(timeMilliseconds*1000);
		setTime(timeCal);
		
		String repeatTypeS = cursor.getString(cursor.getColumnIndex(TimedTask.FIELD_REPEAT));
		if(repeatTypeS != null && !repeatTypeS.equals("")) {
			this.repeatTypes.clear();
			String[] repeatTypeArr = repeatTypeS.split(DELIMETER);
			for(String repeatType : repeatTypeArr) {
				REPEAT_TYPE repeatTypeEnum = REPEAT_TYPE.valueOf(repeatType);
				this.repeatTypes.add(repeatTypeEnum);
			}
		}
		
		long createdMilliseconds = cursor.getInt(cursor.getColumnIndex(TimedTask.FIELD_CREATED));
		Calendar createdCal = GregorianCalendar.getInstance();
		createdCal.setTimeInMillis(createdMilliseconds*1000);
		setCreated(createdCal);
		
		Integer active = cursor.getInt(cursor.getColumnIndex(TimedTask.FIELD_ACTIVE));
		setActiveToBoolean(active);
		
		Integer removeAfterExecution = cursor.getInt(cursor.getColumnIndex(TimedTask.FIELD_REMOVE_AFTER_EXECUTION));
		setRemoveAfterExecution(removeAfterExecution);
		
		long lastExecution = cursor.getInt(cursor.getColumnIndex(TimedTask.FIELD_LAST_EXECUTION));
		Calendar lastExecutionCal = GregorianCalendar.getInstance();
		lastExecutionCal.setTimeInMillis(lastExecution*1000);
		setLastExecution(lastExecutionCal);
	}
	
	public TASK getTask() {
		return task;
	}
	
	public String getTaskSql() {
		return task.name();
	}
	
	public void setTask(TASK task) {
		this.task = task;
	}
	
	public Calendar getTime() {
		return time;
	}
	
	public Integer getTimeSql() {
		return (int) (time.getTimeInMillis()/1000);
	}
	
	public void setTime(Calendar time) {
		this.time = time;
	}
	
//	public void setTime(TimePicker timePicker) {
//		TimeUtil.epocCalendarTime(timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);
//		Calendar selectedTime = GregorianCalendar.getInstance();
//		selectedTime.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
//		selectedTime.set(Calendar.MINUTE, timePicker.getCurrentMinute());
//		selectedTime.set(Calendar.SECOND, 0);
//		selectedTime.set(Calendar.MILLISECOND, 0);
//    	this.time = selectedTime;
//	}
	
	public List<REPEAT_TYPE> getRepeatTypes() {
		return repeatTypes;
	}
	
	public String getRepeatTypesSql() {
		StringBuilder sb = new StringBuilder();
		int loop = 0;
		if(repeatTypes != null) {
			for(REPEAT_TYPE repeatType : repeatTypes) {
				if(loop > 0) {
					sb.append(DELIMETER);
				}
				sb.append(repeatType.name());
				loop++;
			}
		}
		if(sb.length() == 0) {
			return null;
		}
		return sb.toString();
	}
	
	public String getRepeatTypeForDisplay(Context context, boolean showShortText) {
		StringBuilder sb = new StringBuilder();
		int loop = 0;
		if(repeatTypes != null) {
			for(REPEAT_TYPE repeatType : repeatTypes) {
				if(loop > 0) {
					sb.append(DELIMETER).append(" ");
				}
				if(showShortText) {
					sb.append(context.getText(repeatType.keyShort));
				} else {
					sb.append(context.getText(repeatType.key));
				}
				loop++;
			}
		}
		if(sb.length() == 0) {
			return null;
		}
		return sb.toString();
	}
	
	public void setRepeatTypes(List<REPEAT_TYPE> repeatTypes) {
		this.repeatTypes = repeatTypes;
	}
	
	public void setRepeatTypes(KeyValueData<REPEAT_TYPE>[] keyValueDatas) {
		this.repeatTypes.clear();
		if(keyValueDatas != null) {
			for(int i=0; i<keyValueDatas.length; i++) {
				KeyValueData<REPEAT_TYPE> keyValueData = keyValueDatas[i];
				if(keyValueData.isChecked()) {
					this.repeatTypes.add(keyValueData.getValue());
				}
			}
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Calendar getCreated() {
		return created;
	}
	
	public Integer getCreatedSql() {
		Calendar cal = GregorianCalendar.getInstance();
		setCreated(cal);
		return (int) (cal.getTimeInMillis()/1000);
	}

	public void setCreated(Calendar created) {
		this.created = created;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public Integer getActiveSql() {
		return (this.active ? 1 : 0);
	}
	
	public void setActiveToBoolean(Integer value) {
		this.active = (value == 1 ? true : false);
	}
	
	public boolean isRemoveAfterExecution() {
		return removeAfterExecution;
	}
	
	public Integer getRemoveAfterExecutionSql() {
		return (removeAfterExecution ? 1 : 0);
	}

	public void setRemoveAfterExecution(boolean removeAfterExecution) {
		this.removeAfterExecution = removeAfterExecution;
	}
	
	public void setRemoveAfterExecution(Integer value) {
		this.removeAfterExecution = (value == 1 ? true : false);
	}

	public Calendar getLastExecution() {
		return lastExecution;
	}
	
	public Integer getLastExecutionSql() {
		if(lastExecution == null) {
			return null;
		}
		return (int) (lastExecution.getTimeInMillis()/1000);
	}

	public void setLastExecution(Calendar lastExecution) {
		this.lastExecution = lastExecution;
	}
	
}
