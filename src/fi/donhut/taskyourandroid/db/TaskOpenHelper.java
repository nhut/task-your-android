package fi.donhut.taskyourandroid.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import fi.donhut.common.MyActivity;
import fi.donhut.common.util.TimeUtil;
import fi.donhut.taskyourandroid.db.table.TimedTask;
import fi.donhut.taskyourandroid.object.MyTask;

/**
 * http://www.sqlite.org/datatype3.html
 * @author Nhut Do @ 2012
 *
 */
public class TaskOpenHelper extends SQLiteOpenHelper {
	
	private static final String TAG = MyActivity.TAG;
    private static final int DATABASE_VERSION = 2;
    private static final String DB_NAME = "Task";
    private static final String TABLE_NAME = TimedTask.class.getSimpleName();
    private static final String TABLE_CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                TimedTask.FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TimedTask.FIELD_NAME + " TEXT, " +
                TimedTask.FIELD_TASK + " TEXT NOT NULL, " +
                TimedTask.FIELD_TIME + " INTEGER NOT NULL, " +
                TimedTask.FIELD_REPEAT + " TEXT," +
                TimedTask.FIELD_CREATED + " INTEGER NOT NULL," +
                TimedTask.FIELD_ACTIVE+" INTEGER NOT NULL," +
                TimedTask.FIELD_REMOVE_AFTER_EXECUTION+" INTEGER NOT NULL," +
                TimedTask.FIELD_LAST_EXECUTION+" INTEGER);";

    public TaskOpenHelper(Context context) {
        super(context, TaskOpenHelper.DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    	if(Log.isLoggable(TAG, Log.INFO)) {
    		Log.i(TAG, "Create table ("+TABLE_NAME+")... "+TABLE_CREATE);
    	}
        db.execSQL(TABLE_CREATE);
        if(Log.isLoggable(TAG, Log.INFO)) {
    		Log.i(TAG, "Table ("+TABLE_NAME+") created!");
    	}
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(Log.isLoggable(TAG, Log.WARN)) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
	                + newVersion + ", which will destroy all old data");
		}
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
	}
	
	/**
	 * Insert a new task into database.
	 * @param task
	 * @param time
	 * @param repeat
	 * @return
	 */
	public long addNewRow(MyTask myTask) {
		SQLiteDatabase db = this.getWritableDatabase();
    	ContentValues insertValues = new ContentValues();
    	insertValues.put(TimedTask.FIELD_TASK, myTask.getTaskSql());
    	insertValues.put(TimedTask.FIELD_TIME, myTask.getTimeSql());
    	insertValues.put(TimedTask.FIELD_REPEAT, myTask.getRepeatTypesSql());
    	insertValues.put(TimedTask.FIELD_CREATED, myTask.getCreatedSql());
    	insertValues.put(TimedTask.FIELD_ACTIVE, myTask.getActiveSql());
    	insertValues.put(TimedTask.FIELD_REMOVE_AFTER_EXECUTION, myTask.getRemoveAfterExecutionSql());
    	insertValues.put(TimedTask.FIELD_LAST_EXECUTION, myTask.getLastExecutionSql());
    	
    	long result = db.insert(TABLE_NAME, null, insertValues);
    	if(Log.isLoggable(TAG, Log.INFO)) {
    		Log.i(TAG, "SQL addNewRow() result = "+result+", created orig="+TimeUtil.toDisplayDDMMYYYY_HHmm(myTask.getCreated())+" ("+myTask.getCreated().getTimeInMillis()+")");
    	}
    	db.close();
    	return result;
	}
	
	public int updateRow(MyTask myTask) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues insertValues = new ContentValues();
    	insertValues.put(TimedTask.FIELD_TASK, myTask.getTaskSql());
    	insertValues.put(TimedTask.FIELD_TIME, myTask.getTimeSql());
    	insertValues.put(TimedTask.FIELD_REPEAT, myTask.getRepeatTypesSql());
    	insertValues.put(TimedTask.FIELD_ACTIVE, myTask.getActiveSql());
    	insertValues.put(TimedTask.FIELD_LAST_EXECUTION, myTask.getLastExecutionSql());
    	
		int result = db.update(TABLE_NAME, insertValues, TimedTask.FIELD_ID+"="+myTask.getId(), null);
		if(Log.isLoggable(TAG, Log.INFO)) {
    		Log.i(TAG, "SQL updateRow() result = "+result);
    	}
		db.close();
		return result;
	}
	
	public int updateTaskActive(MyTask myTask) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues insertValues = new ContentValues();
		insertValues.put(TimedTask.FIELD_TIME, myTask.getTimeSql());
    	insertValues.put(TimedTask.FIELD_ACTIVE, myTask.getActiveSql());
    	insertValues.put(TimedTask.FIELD_LAST_EXECUTION, myTask.getLastExecutionSql());
    	
		int result = db.update(TABLE_NAME, insertValues, TimedTask.FIELD_ID+"="+myTask.getId(), null);
		if(Log.isLoggable(TAG, Log.INFO)) {
    		Log.i(TAG, "SQL updateTaskActive() result = "+result);
    	}
		db.close();
		return result;
	}
	
	public int deleteRow(MyTask myTask) {
		SQLiteDatabase db = this.getWritableDatabase();
        int linesDeleted = db.delete(TABLE_NAME, TimedTask.FIELD_ID + " = ?",
                new String[] { String.valueOf(myTask.getId()) });
        db.close();
        return linesDeleted;
	}
	
	/**
	 * Get all the task.
	 * @return
	 */
	public List<MyTask> getAllTask() {
		List<MyTask> myTasks = new ArrayList<MyTask>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, TimedTask.ALL_FIELDS, null, null, null, null, TimedTask.FIELD_ID);
		if(cursor.moveToFirst()) {
			MyTask myTask = new MyTask(cursor);
			myTasks.add(myTask);
			while(cursor.moveToNext()) {
				myTask = new MyTask(cursor);
				myTasks.add(myTask);
			}
		}
		cursor.close();
		db.close();
		return myTasks;
	}
	
	public MyTask getTask(Integer id) {
		MyTask myTask = null;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, TimedTask.ALL_FIELDS, TimedTask.FIELD_ID+"= "+id, null, null, null, null);
		if(cursor.moveToFirst()) {
			myTask = new MyTask(cursor);
		}
		cursor.close();
		db.close();
		return myTask;
	}

}
