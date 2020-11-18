package fi.donhut.taskyourandroid;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import fi.donhut.common.MyActivity;
import fi.donhut.common.dialog.MultipleChoiceDialog;
import fi.donhut.common.util.SchedulerUtil;
import fi.donhut.common.util.TimeUtil;
import fi.donhut.taskyourandroid.db.TaskOpenHelper;
import fi.donhut.taskyourandroid.enumeration.REPEAT_TYPE;
import fi.donhut.taskyourandroid.enumeration.TASK;
import fi.donhut.taskyourandroid.object.KeyValueData;
import fi.donhut.taskyourandroid.object.MyTask;
import fi.donhut.taskyourandroid.util.Constants;

/**
 * 
 * @author Nhut Do @ 11.2012
 *
 */
public class AddEditTaskActivity extends MyActivity {
	
	private MultipleChoceDialog2<REPEAT_TYPE> multipleChoiceDialog = null;
	
	private Spinner spinnerTask = null;
	private TextView datePicker = null;
	private TimePicker timePicker = null;
	private MyTask timeTask = null;
	
    @SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);
        
//        Button buttonTask = (Button) findViewById(R.id.buttonTask);
//        buttonTask.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				
//			}
//		});
        
        spinnerTask = (Spinner) findViewById(R.id.spinnerTask);
        final ArrayList<KeyValueData<TASK>> taskList = new ArrayList<KeyValueData<TASK>>();
        for(TASK enumObj : TASK.values()) {
        	taskList.add(new KeyValueData<TASK>(getString(enumObj.key), enumObj));
        }
        spinnerTask.setAdapter(new ArrayAdapter<KeyValueData<TASK>>(this, android.R.layout.simple_spinner_item, taskList));
        spinnerTask.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, 
                    View view, 
                    int position, 
                    long id) {
				KeyValueData<TASK> keyValueData = taskList.get(position);
				TASK selectedItem = keyValueData.getValue();
				if(Constants.DEBUG) {
					Log.d(TAG, "Selected: "+selectedItem.name());
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
        	
		});
        
        datePicker = (TextView) findViewById(R.id.textViewDatePicker);
        
        timePicker = (TimePicker) findViewById(R.id.timePickerTime);
        timePicker.setIs24HourView(true);
//        timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
//			
//			@Override
//			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//				Calendar calToShow = TimeUtil.timePickerToCalendar(hourOfDay, minute);
//				datePicker.setText(TimeUtil.toDisplayDDMMYYYY(calToShow));
//			}
//		});
        Calendar currCal = GregorianCalendar.getInstance();
        currCal.add(Calendar.MINUTE, 1);
        timePicker.setCurrentHour(currCal.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(currCal.get(Calendar.MINUTE));
        currCal = TimeUtil.timePickerToCalendar(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
        datePicker.setText(TimeUtil.toDisplayDDMMYYYY(currCal));
        
        final ArrayList<KeyValueData<REPEAT_TYPE>> repeatTypeList = new ArrayList<KeyValueData<REPEAT_TYPE>>();
        for(REPEAT_TYPE enumObj : REPEAT_TYPE.values()) {
        	repeatTypeList.add(new KeyValueData<REPEAT_TYPE>(getString(enumObj.key), enumObj));
        }
        
        KeyValueData<REPEAT_TYPE>[] chooseRepeatTypeArray = repeatTypeList.toArray(new KeyValueData[0]);
        multipleChoiceDialog = new MultipleChoceDialog2<REPEAT_TYPE>(this, getString(R.string.select_repeat_days), chooseRepeatTypeArray);
        
        Bundle extraBundle = getIntent().getExtras();
        if(extraBundle != null) {
	        int passedMyTaskId = extraBundle.getInt("id", -1);
	        if(passedMyTaskId > 0) {
	        	TaskOpenHelper taskOpenHelper = new TaskOpenHelper(this);
	        	timeTask = taskOpenHelper.getTask(passedMyTaskId);
	        	if(timeTask != null) {
	        		setValuesFromExistsObject(timeTask);
	        	}
	        }
        }
    }
    
    @SuppressWarnings("unchecked")
	private void setValuesFromExistsObject(MyTask task) {
    	//SET TASK
    	for(int i=0; i<spinnerTask.getAdapter().getCount(); i++) {
    		KeyValueData<TASK> keyValueDataTask = (KeyValueData<TASK>)spinnerTask.getAdapter().getItem(i);
    		if(keyValueDataTask.getValue().compareTo(task.getTask()) == 0) {
    			spinnerTask.setSelection(i);
    			break;
    		}
    	}
    	//SET TIME
    	Calendar time = task.getTime();
    	timePicker.setCurrentHour(time.get(Calendar.HOUR_OF_DAY));
    	timePicker.setCurrentMinute(time.get(Calendar.MINUTE));
    	
    	for(KeyValueData<REPEAT_TYPE> keyValueDataRepeatType : multipleChoiceDialog.getKeyValueDatas()) {
    		if(task.getRepeatTypes().contains(keyValueDataRepeatType.getValue())) {
    			keyValueDataRepeatType.setChecked(true);
    		} else {
    			keyValueDataRepeatType.setChecked(false);
    		}
    	}
    	multipleChoiceDialog.updateSelectFromKeyValueDatas();
    	multipleChoiceDialog.updateRepeatTypeButtonText();
    	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	return super.onOptionsItemSelected(item);
    }
    
    public void doSelectTask(View view) {
        multipleChoiceDialog.show();
    }
    
    public class MultipleChoceDialog2<T> extends MultipleChoiceDialog<T> {

		public MultipleChoceDialog2(Context context, String title,
				KeyValueData<T>[] pKeyValueDatas) {
			super(context, title, pKeyValueDatas);
		}
    	
		@Override
		public void onClick(DialogInterface dialog, int which) {
			super.onClick(dialog, which);
			updateRepeatTypeButtonText();
		}
		
		public void updateRepeatTypeButtonText() {
			Button repeatTypeButton = (Button) findViewById(R.id.buttonRepeatType);
			boolean isAnySelected = super.isAnyChecked();
			if(isAnySelected) {
				repeatTypeButton.setText(multipleChoiceDialog.selectedOptionsText());
			} else {
				repeatTypeButton.setText(context.getText(R.string.select_repeat_none));
			}
		}
    }
    
    private void moveToScheduleView() {
//    	Intent intent = new Intent(this, MainActivity.class);
//    	startActivity(intent);
//    	finish();
    	
    	onBackPressed();
    }
    
    public void doCancelAdd(View view) {
    	setResult(RESULT_CANCELED);
    	moveToScheduleView();
    }
    
    @SuppressWarnings("unchecked")
	public void doSaveNewOrOld(View view) {
    	if(timeTask == null) {
    		timeTask = new MyTask();
    	}
    	
    	TASK selectedTask = (TASK)((KeyValueData<TASK>)spinnerTask.getSelectedItem()).getValue(); 
    	timeTask.setTask(selectedTask);
    	Calendar startTimeCal = TimeUtil.timePickerToCalendar(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
    	timeTask.setTime(startTimeCal);
    	datePicker.setText(TimeUtil.toDisplayDDMMYYYY(startTimeCal));
    	Calendar currCal = GregorianCalendar.getInstance();
    	timeTask.setRepeatTypes(multipleChoiceDialog.getKeyValueDatas());
    	
    	TaskOpenHelper taskOpenHelper = new TaskOpenHelper(this);
    	long rowId = 0;
    	if(timeTask.getId() == null || timeTask.getId() == 0) {
    		rowId = taskOpenHelper.addNewRow(timeTask);
    		timeTask.setId((int)rowId);
    		if(rowId != -1) {
    			Toast.makeText(this, getText(R.string.task_create_success), Toast.LENGTH_LONG).show();
    		} else {
    			Toast.makeText(this, getText(R.string.task_create_fail), Toast.LENGTH_LONG).show();
    		}
    	} else {
    		rowId = taskOpenHelper.updateRow(timeTask);
    		if(rowId != -1) {
    			Toast.makeText(this, getText(R.string.task_edit_success), Toast.LENGTH_LONG).show();
    		} else {
    			Toast.makeText(this, getText(R.string.task_edit_fail), Toast.LENGTH_LONG).show();
    		}
    	}
    	
    	SchedulerUtil.scheduleTask(this, timeTask);
    	
    	setResult(RESULT_OK);
    	
    	currCal.set(Calendar.MILLISECOND, 0);
    	startTimeCal.set(Calendar.MILLISECOND, 0);
    	if(currCal.compareTo(startTimeCal) == 0) {
    		try {
    			//sleep for a second, so layout triggers correctly
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
    	}
    	
    	moveToScheduleView();
    }
    
    public void doDelete(View view) {
    	setResult(RESULT_OK);
    	moveToScheduleView();
    }
}
