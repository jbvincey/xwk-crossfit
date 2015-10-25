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
import android.widget.EditText;
import android.widget.TextView;

import com.jbvincey.xwk.R;

public class WorkoutEndFragment extends DialogFragment {
	
	public final static String WORKOUT_END_FRAGMENT_MESSAGE = "workoutEndFragmentMessage";
	public final static String WORKOUT_END_FRAGMENT_TITLE = "workoutEndFragmentTitle";
	
	private DialogActivity listenerActivity;
	private EditText editView;
	private WorkoutEndFragment dialog;

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
		String description = "description";
		dialog = this;

		if(args!=null && args.containsKey(WORKOUT_END_FRAGMENT_TITLE)) {
			name = args.getString(WORKOUT_END_FRAGMENT_TITLE);
		}
		
		if(args!=null && args.containsKey(WORKOUT_END_FRAGMENT_MESSAGE)) {
			description = args.getString(WORKOUT_END_FRAGMENT_MESSAGE);
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(listenerActivity);

		LayoutInflater inflater = listenerActivity.getLayoutInflater();

		View dialogView = inflater.inflate(R.layout.workoutend_fragment, null);

		TextView titleView = (TextView) dialogView.findViewById(R.id.workoutend_fragment_title);
		titleView.setText(name);
		TextView descriptionView = (TextView) dialogView.findViewById(R.id.workoutend_fragment_message);
		descriptionView.setText(description);
		editView = (EditText) dialogView.findViewById(R.id.workoutend_fragment_comment);
		Button button = (Button) dialogView.findViewById(R.id.workoutend_fragment_button);

		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				listenerActivity.onPerformanceCommentAdded(editView.getText().toString());
				dialog.dismiss();
			}
		});
		
		
		builder.setView(dialogView);
		return builder.create();
	}
	
}
