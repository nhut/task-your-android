package fi.donhut.common.dialog;

import android.app.AlertDialog.Builder;
import android.content.Context;

public class SingleChoiceDialog<T> extends KeyValueChoiceDialog<T> {
	
	public SingleChoiceDialog(Context context) {
		super(context);
	}

	@Override
	public void initialize() {
		
	}

	@Override
	public Builder createAlertDialog() {
		return null;
	}

}
