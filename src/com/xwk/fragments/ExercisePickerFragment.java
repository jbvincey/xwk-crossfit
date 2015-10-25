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
import android.widget.TextView;

import com.jbvincey.xwk.R;
import com.xwk.model.Ids;

public class ExercisePickerFragment extends DialogFragment {

	private DialogActivity listenerActivity;
	private NumberPicker np;
	private long id = 0;
	private int position = -1;
	private ExercisePickerFragment dialog;
	
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
		String name = "name";
		int reps = -1;
		dialog = this;
		if(args!=null && args.containsKey(Ids.EXERCISE_NAME)) {
			name = args.getString(Ids.EXERCISE_NAME);
		}

		if(args!=null && args.containsKey(Ids.EXERCISE_ID)) {
			id = args.getLong(Ids.EXERCISE_ID);
		}

		if(args!=null && args.containsKey(Ids.EXERCISE_POSITION)) {
			position = args.getInt(Ids.EXERCISE_POSITION);
		}

		if(args!=null && args.containsKey(Ids.EXERCISE_REPS)) {
			reps = args.getInt(Ids.EXERCISE_REPS);
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(listenerActivity);

		LayoutInflater inflater = listenerActivity.getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout

		View dialogView = inflater.inflate(R.layout.exercise_picker_fragment, null);

		np = (NumberPicker)dialogView.findViewById(R.id.exercise_picker_number_picker);
		np.setMinValue(1);
		np.setMaxValue(1000);
		if(reps!=-1) {
			np.setValue(reps);
		}
		else {
			np.setValue(10);
		}
		np.setWrapSelectorWheel(false);
		np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

		
		TextView titleView = (TextView) dialogView.findViewById(R.id.exercise_picker_title);
		titleView.setText(name + ": reps");
		
		Button button1 = (Button) dialogView.findViewById(R.id.exercise_picker_doublebutton_button1);
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				listenerActivity.onExerciseRepsChosen(id, np.getValue(), position);
				dialog.dismiss();
			}
		});
		
		builder.setView(dialogView);
		return builder.create();
	}
}
