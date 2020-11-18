package fi.donhut.common.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import fi.donhut.taskyourandroid.object.KeyValueData;

/**
 * 
 * @author Nhut Do @ 2012
 *
 * @param <T>
 */
public abstract class KeyValueChoiceDialog<T> implements OnClickListener {
	
	protected Context context;
	
	protected String title = null;
	
	protected KeyValueData<T>[] keyValueDatas = null;
	protected String[] texts = null;
	protected boolean[] selected = null;
	protected AlertDialog dialog = null;
	
	protected KeyValueChoiceDialog(Context context) {
		this.context = context;
	}
	
	public KeyValueChoiceDialog(Context context, String title, KeyValueData<T>[] pKeyValueDatas) {
		this(context);
		this.title = title;
		this.keyValueDatas = pKeyValueDatas;
		initialize();
		createDialog();
	}
	
	public abstract void initialize();
	public abstract AlertDialog.Builder createAlertDialog();
	
	protected void createDialog() {
		AlertDialog.Builder builder = createAlertDialog();
		
		dialog = builder.create();
		dialog.setTitle(title);
	}
	
	
	public void updateListenerValues() {
		for(int i=0; i<keyValueDatas.length; i++) {
			keyValueDatas[i].setChecked(selected[i]);
		}
	}
	
	public void updateSelectFromKeyValueDatas() {
		for(int i=0; i<keyValueDatas.length; i++) {
			selected[i] = keyValueDatas[i].isChecked();
		}
	}
	
	public boolean isAnyChecked() {
		for(int i=0; i<keyValueDatas.length; i++) {
			if(selected[i]) {
				return true;
			}
		}
		return false;
	}
	
	public KeyValueData<T>[] getKeyValueDatas() {
		return keyValueDatas;
	}
	
	public String selectedOptionsText() {
		StringBuilder sb = new StringBuilder();
		boolean once = false;
		for(int i=0; i<keyValueDatas.length; i++) {
			KeyValueData<T> keyValueData = keyValueDatas[i];
			if(!keyValueData.isChecked()) {
				continue;
			}
			if(once) {
				sb.append(", ");
			}
			sb.append(keyValueDatas[i].getText());
			once = true;
		}
		return sb.toString();
	}

	public AlertDialog getDialog() {
		return dialog;
	}
	
	public void show() {
		dialog.show();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		//OK pressed
		updateListenerValues();
		dialog.dismiss();
	}
	
}
