package fi.donhut.taskyourandroid;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;
import fi.donhut.common.MyActivity;
import fi.donhut.common.util.SchedulerUtil;
import fi.donhut.taskyourandroid.adapter.ScheduledTaskAdapter;
import fi.donhut.taskyourandroid.db.TaskOpenHelper;
import fi.donhut.taskyourandroid.object.MyTask;

/**
 * 
 * @author Nhut Do @ 2012
 *
 */
public class ScheduleActivity extends MyActivity {
	
	private final static int TASK_NEW = 1;
	private final static int TASK_EDIT = 2; 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        
        updateList();
    }

    private void updateList() {
    	TaskOpenHelper taskOpenHelper = new TaskOpenHelper(this);
        List<MyTask> tasks = taskOpenHelper.getAllTask();
        System.out.println(tasks);
        
        ListView listView = (ListView) findViewById(R.id.listViewTasks);
        ScheduledTaskAdapter scheduledTaskAdapter = new ScheduledTaskAdapter(this, tasks);
        listView.setAdapter(scheduledTaskAdapter);
        registerForContextMenu(listView);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, view, menuInfo);
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.schedule_task, menu);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	if(super.onContextItemSelected(item)) {
    		return true;
    	}
    	
    	ListView listView = (ListView) findViewById(R.id.listViewTasks);
    	ScheduledTaskAdapter adapter = (ScheduledTaskAdapter) listView.getAdapter();
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	MyTask myTask = adapter.getItem(info.position);
    	TaskOpenHelper taskOpenHelper = null;
    	
    	int selectedItemId = item.getItemId();
        switch(selectedItemId) {
        case R.id.menu_delete:
        case R.id.menu_deactivate:
        case R.id.menu_activate:
        	taskOpenHelper = new TaskOpenHelper(this);
        	if(selectedItemId == R.id.menu_activate) {
        		//activate task
        		myTask.setActive(true);
        		Calendar currCal = GregorianCalendar.getInstance();
        		currCal.set(Calendar.HOUR_OF_DAY, myTask.getTime().get(Calendar.HOUR_OF_DAY));
        		currCal.set(Calendar.MINUTE, myTask.getTime().get(Calendar.MINUTE));
        		currCal.set(Calendar.SECOND, myTask.getTime().get(Calendar.SECOND));
        		currCal.set(Calendar.MILLISECOND, myTask.getTime().get(Calendar.MILLISECOND));
        		myTask.setTime(currCal);
        		SchedulerUtil.scheduleTask(this, myTask);
        		int rowId = taskOpenHelper.updateTaskActive(myTask);
        		Toast.makeText(this, getString(R.string.task_activate_success, myTask.getId()), Toast.LENGTH_LONG).show();
        		
        	} else {
        		//cancel the task
        		SchedulerUtil.cancelScheduledTask(this, myTask);
        		
        		if(selectedItemId == R.id.menu_deactivate) {
        			myTask.setActive(false);
        			int rowId = taskOpenHelper.updateTaskActive(myTask);
        			Toast.makeText(this, getString(R.string.task_deactivate_success, myTask.getId()), Toast.LENGTH_LONG).show();
        			
        		} else {
        			taskOpenHelper.deleteRow(myTask);
        			Toast.makeText(this, getString(R.string.task_deleted_success, myTask.getId()), Toast.LENGTH_SHORT).show();
        			adapter.removeItem(myTask);
        		}
        	}
        	adapter.notifyDataSetChanged();
        	return true;
        	
        case R.id.menu_view:
        	Toast.makeText(this, "Clicked: "+item.getItemId()+", id="+info.id+", pos="+info.position, Toast.LENGTH_SHORT).show();
        	return true;
        	
        case R.id.menu_edit:
        	Intent intent = new Intent(this, AddEditTaskActivity.class);
        	intent.putExtra("id", myTask.getId());
        	startActivityForResult(intent, TASK_EDIT);
        	return true;
        	
        default:
        	return true;
        }
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	updateList();
    	super.onRestoreInstanceState(savedInstanceState);
    }
    
    public void doAddNewTask(View view) {
    	Intent intent = new Intent(this, AddEditTaskActivity.class);
    	startActivityForResult(intent, TASK_NEW);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if(resultCode == RESULT_OK) {
    		updateList();
    		doScrollListBottom(null);
    	}
    }
    
    public void doScrollListBottom(View view) {
    	ListView listView = (ListView) findViewById(R.id.listViewTasks);
    	if(listView.getAdapter() != null && listView.getAdapter().getCount() > 0) {
			listView.smoothScrollToPosition(listView.getAdapter().getCount()-1);
    	}
    }
    
    public void doScrollListTop(View view) {
    	ListView listView = (ListView) findViewById(R.id.listViewTasks);
    	listView.smoothScrollToPosition(0);
    }
    
    public void doTaskEnableDisable(View view) {
    	CheckBox checkBox = (CheckBox)view;
    	checkBox.setChecked(true);
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage(getText(R.string.coming_soon));
    	builder.setPositiveButton(getText(R.string.button_ok), new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
    	final AlertDialog alert = builder.create();
		alert.show();
    }
}
