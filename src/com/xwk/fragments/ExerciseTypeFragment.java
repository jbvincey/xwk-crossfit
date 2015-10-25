package com.xwk.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.jbvincey.xwk.R;
import com.xwk.controller.AddExercise;
import com.xwk.model.Exercise.ExerciseType;

public class ExerciseTypeFragment extends DialogFragment {

	private AddExercise listenerActivity;
	private DialogFragment dialogFragment;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the NoticeDialogListener so we can send events to the host
			listenerActivity = (AddExercise) activity;
			dialogFragment = this;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement RestActivity");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)  {

		AlertDialog.Builder builder = new AlertDialog.Builder(listenerActivity);

		LayoutInflater inflater = listenerActivity.getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout

		View dialogView = inflater.inflate(R.layout.exercise_type_fragment, null);
		
		LinearLayout legs = (LinearLayout) dialogView.findViewById(R.id.exercise_type_legs);
		legs.setClickable(true);
		legs.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listenerActivity.onExerciseTypeChosen(ExerciseType.legs);
				dialogFragment.dismiss();
			}
		});

		LinearLayout chest = (LinearLayout) dialogView.findViewById(R.id.exercise_type_chest);
		chest.setClickable(true);
		chest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listenerActivity.onExerciseTypeChosen(ExerciseType.chest);
				dialogFragment.dismiss();
			}
		});

		LinearLayout arms = (LinearLayout) dialogView.findViewById(R.id.exercise_type_arms);
		arms.setClickable(true);
		arms.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listenerActivity.onExerciseTypeChosen(ExerciseType.arms);
				dialogFragment.dismiss();
			}
		});

		LinearLayout back = (LinearLayout) dialogView.findViewById(R.id.exercise_type_back);
		back.setClickable(true);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listenerActivity.onExerciseTypeChosen(ExerciseType.back);
				dialogFragment.dismiss();
			}
		});

		LinearLayout abs = (LinearLayout) dialogView.findViewById(R.id.exercise_type_abs);
		abs.setClickable(true);
		abs.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listenerActivity.onExerciseTypeChosen(ExerciseType.abs);
				dialogFragment.dismiss();
			}
		});

		builder.setView(dialogView);
		return builder.create();
	}

}