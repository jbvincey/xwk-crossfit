package com.xwk.fragments;

import android.app.Activity;

public abstract class DialogActivity extends Activity {

	public void onDecisionTaken(LongClickDialog.decision decision) {}

	public void onExerciseRepsChosen(long id, int reps, int position) {}
	
	public void onPerformanceCommentAdded(String comment) {}
	
	public void onConfirmOrCancel(boolean confirmOrCancel) {}
	
	public void onSetsChosen(int sets) {}
	
	public void onExerciseChosen() {}
}