package fi.donhut.taskyourandroid.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import fi.donhut.common.MyActivity;
import fi.donhut.common.util.TimeUtil;
import fi.donhut.taskyourandroid.R;
import fi.donhut.taskyourandroid.object.MyTask;
import fi.donhut.taskyourandroid.util.Constants;

/**
 * 
 * @author Nhut Do @ 2012
 *
 */
public class ScheduledTaskAdapter extends BaseAdapter {
	
	private final static String TAG = MyActivity.TAG;
	
	private List<MyTask> items = null;
	private static LayoutInflater inflater = null;
	
	public ScheduledTaskAdapter(Context context, List<MyTask> items) {
		if(items == null) {
			items = new ArrayList<MyTask>();
		}
		this.items = items;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		sortData();
	}
	
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public MyTask getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return items.get(position).hashCode();
	}
	
	@Override
	public void notifyDataSetChanged() {
		sortData();
		super.notifyDataSetChanged();
	}
	
	public void removeItem(MyTask myTask) {
		items.remove(myTask);
	}
	
	private void sortData() {
		if(Constants.DEBUG) {
			Log.d(TAG, "Sorting...");
		}
		Collections.sort(items, new Comparator<MyTask>() {

			@Override
			public int compare(MyTask lhs, MyTask rhs) {
				return lhs.getId().compareTo(rhs.getId());
			}
		});
		if(Constants.DEBUG) {
			Log.d(TAG, "Sort done!");
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi=convertView;
        if(vi==null) {
            vi = inflater.inflate(R.layout.listview_scheduled_task_row, null);
        }
        Context context = vi.getContext();
        final MyTask myTask = getItem(position);
        TextView title = (TextView) vi.findViewById(R.id.textViewTask);
        title.setText(context.getString(myTask.getTask().key));
        TextView content = (TextView) vi.findViewById(R.id.textViewContent);
        final String REPEAT_TYPE_STRING = myTask.getRepeatTypeForDisplay(context, true);
        content.setText(/*context.getText(R.string.scheduled)+" "+*/(myTask.getTime() == null ? "?" : (REPEAT_TYPE_STRING == null ? TimeUtil.toDisplayHHmm_DDMMYYYY(myTask.getTime()) : TimeUtil.toHHmm(myTask.getTime()))));
		TextView repeat = (TextView) vi.findViewById(R.id.TextViewRepeat);
        repeat.setText(/*context.getText(R.string.repeat)+" "+*/(REPEAT_TYPE_STRING == null ? context.getText(R.string.select_repeat_none) : REPEAT_TYPE_STRING));
        TextView created = (TextView) vi.findViewById(R.id.textViewCreated);
        String createdText = context.getText(R.string.created)+" "+TimeUtil.toDisplayDDMMYYYY_HHmm(myTask.getCreated());
        if(Constants.DEBUG) {
        	createdText+=", last="+(myTask.getLastExecution() == null ? "null" : TimeUtil.toDisplayDDMMYYYY_HHmm(myTask.getLastExecution()));
        }
        created.setText(createdText);
        TextView id = (TextView) vi.findViewById(R.id.textViewId);
        id.setText(context.getText(R.string.id)+": "+myTask.getId());
        
        CheckBox checkBox = (CheckBox) vi.findViewById(R.id.checkBoxEnabled);
    	checkBox.setChecked(myTask.isActive());
    	vi.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				((Activity)v.getContext()).openContextMenu(v);
				return true;
			}
		});
        return vi;
	}

}
