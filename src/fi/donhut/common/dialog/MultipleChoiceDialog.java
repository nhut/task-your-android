package fi.donhut.common.dialog;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import fi.donhut.taskyourandroid.R;
import fi.donhut.taskyourandroid.object.KeyValueData;

/**
 * 
 * @author Nhut Do @ 2012
 *
 * @param <T>
 */
public class MultipleChoiceDialog<T> extends KeyValueChoiceDialog<T> implements DialogInterface.OnMultiChoiceClickListener {
	
	public MultipleChoiceDialog(Context context, String title, KeyValueData<T>[] pKeyValueDatas) {
		super(context, title, pKeyValueDatas);
	}
	
	@Override
	public void initialize() {
		this.texts = new String[this.keyValueDatas.length];
		this.selected = new boolean[this.keyValueDatas.length];
		for(int i=0; i<keyValueDatas.length; i++) {
			KeyValueData<T> obj = keyValueDatas[i];
			this.texts[i] = obj.getText();
			this.selected[i] = obj.isChecked();
		}
	}

	@Override
	public Builder createAlertDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMultiChoiceItems(texts, selected, this);
		builder.setPositiveButton(context.getText(R.string.button_ok), this);
		return builder;
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which, boolean isChecked) {
		selected[which] = isChecked;
	}
	
}
