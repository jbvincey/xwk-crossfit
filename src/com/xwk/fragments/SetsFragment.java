package com.xwk.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.NumberPicker;

import com.jbvincey.xwk.R;
import com.xwk.model.Ids;

public class SetsFragment extends DialogFragment {

	private DialogActivity listenerActivity;
	private NumberPicker np;
	private SetsFragment dialog;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the NoticeDialogListener so we can send events to the host
			listenerActivity = (DialogActivity) activity;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement RestActivity");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){

		Bundle args = getArguments();
		int sets = -1;
		dialog = this;

		if(args!=null && args.containsKey(Ids.CROSSFIT_WORKOUT_SETS)) {
			sets = args.getInt(Ids.CROSSFIT_WORKOUT_SETS);
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(listenerActivity);

		LayoutInflater inflater = listenerActivity.getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout

		View dialogView = inflater.inflate(R.layout.sets_fragment, null);

		np = (NumberPicker)dialogView.findViewById(R.id.sets_fragment_number_picker);
		np.setMinValue(1);
		np.setMaxValue(40);
		if(sets!=-1) {
			np.setValue(sets);
		}
		else {
			np.setValue(1);
		}
		np.setWrapSelectorWheel(false);
		np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		
		Button button1 = (Button) dialogView.findViewById(R.id.sets_fragment_button);
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				listenerActivity.onSetsChosen(np.getValue());
				dialog.dismiss();
			}
		});
		builder.setView(dialogView);
		return builder.create();
	}
}
