package com.xwk.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.jbvincey.xwk.R;
import com.xwk.database.ExerciseDAO;
import com.xwk.fragments.CustomAlertDialog;
import com.xwk.fragments.DialogActivity;
import com.xwk.fragments.ExerciseDetailsFragment;
import com.xwk.fragments.ExercisePickerFragment;
import com.xwk.fragments.LongClickDialog;
import com.xwk.fragments.LongClickDialog.decision;
import com.xwk.model.Exercise;
import com.xwk.model.Exercise.ExerciseType;
import com.xwk.model.Ids;

public class ChooseExercise extends DialogActivity implements OnClickListener {

	private ExerciseDAO exerciseDAO;

	private ListView exerciseListView;
	private RelativeLayout legsButton;
	private RelativeLayout chestButton;
	private RelativeLayout armsButton;
	private RelativeLayout backButton;
	private RelativeLayout absButton;
	private ImageView legsImage;
	private ImageView chestImage;
	private ImageView armsImage;
	private ImageView backImage;
	private ImageView absImage;

	private List<Exercise> typeExercises;

	private Exercise selectedExercise = null;
	private Activity activity;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.choose_exercise);

		exerciseDAO = ExerciseDAO.getInstance(this);
		exerciseDAO.open();

		exerciseListView = (ListView) findViewById(R.id.choose_exercise_listview);

		legsButton = (RelativeLayout) findViewById(R.id.button_legs);
		legsButton.setOnClickListener(this);
		legsImage = (ImageView) findViewById(R.id.button_image_legs);
		chestButton = (RelativeLayout) findViewById(R.id.button_chest);
		chestButton.setOnClickListener(this);
		chestImage = (ImageView) findViewById(R.id.button_image_chest);
		armsButton = (RelativeLayout) findViewById(R.id.button_arms);
		armsButton.setOnClickListener(this);
		armsImage = (ImageView) findViewById(R.id.button_image_arms);
		backButton = (RelativeLayout) findViewById(R.id.button_back);
		backButton.setOnClickListener(this);
		backImage = (ImageView) findViewById(R.id.button_image_back);
		absButton = (RelativeLayout) findViewById(R.id.button_abs);
		absButton.setOnClickListener(this);
		absImage = (ImageView) findViewById(R.id.button_image_abs);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		activity = this;
		this.getActionBar().setTitle("  Pick exercise");

		intent = new Intent(activity, AddCrossfitWorkout.class);
		setResult(AddCrossfitWorkout.CHOOSE_EXERCISE_REQUESTCODE, intent);

	}

	@Override
	protected void onResume() {
		super.onResume();
		exerciseDAO.open();
		typeExercises = exerciseDAO.getExerciseByType(ExerciseType.legs);
		resetButtons();
		legsButton.setBackgroundColor(Color.parseColor("#ffffff"));
		legsImage.setImageResource(R.drawable.legshdpi5);
		setupListView();
	}

	@Override
	protected void onPause() {
		super.onPause();
		exerciseDAO.close();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);

		if(selectedExercise!=null) {
			savedInstanceState.putLong(Ids.EXERCISE_ID, selectedExercise.getId());
		}
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		exerciseDAO.open();
		if(savedInstanceState!=null && savedInstanceState.containsKey(Ids.EXERCISE_ID)) {
			selectedExercise = exerciseDAO.getExercise(savedInstanceState.getLong(Ids.EXERCISE_ID));
		}
	}

	private void setupListView() {

		List<Map<String, Object>> dataExercises = new ArrayList<Map<String, Object>>();
		dataExercises.clear();

		for(Exercise exercise: typeExercises) {
			Map<String, Object> datum = new HashMap<String, Object>(2);
			datum.put(Ids.EXERCISE_NAME, exercise.getName());
			datum.put(Ids.EXERCISE_REPS, exercise.getDescription());
			dataExercises.add(datum);
		}

		SimpleAdapter adapter2 = new SimpleAdapter(this, dataExercises, R.layout.rowlist_choose_exercise, new String[] {Ids.EXERCISE_NAME, Ids.EXERCISE_REPS}, new int[] {R.id.rowlist_choose_exercise_item1, R.id.rowlist_choose_exercise_item2});
		exerciseListView.setAdapter(adapter2);

		exerciseListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectedExercise = typeExercises.get(position);
				/*ExercisePickerFragment exercisePickerFragment = new ExercisePickerFragment();
				Bundle args = new Bundle();
				args.putString(Ids.EXERCISE_NAME, selectedExercise.getName());
				args.putLong(Ids.EXERCISE_ID, selectedExercise.getId());
				exercisePickerFragment.setArguments(args);
				exercisePickerFragment.show(getFragmentManager(), "ExerciseDetails");*/
				ExerciseDetailsFragment exerciseDetailsFragment = new ExerciseDetailsFragment();
				Bundle args = new Bundle();
				args.putString(Ids.EXERCISE_NAME, selectedExercise.getName());
				args.putString(Ids.EXERCISE_DESCRIPTION, selectedExercise.getDescription());
				args.putByteArray(Ids.EXERCISE_TYPE, selectedExercise.getTypeBytearrayWhite(activity));
				args.putByteArray(Ids.EXERCISE_PIC, selectedExercise.getImage());
				args.putBoolean(Ids.ALERTDIALOG_PICKER, true);
				exerciseDetailsFragment.setArguments(args);
				exerciseDetailsFragment.show(getFragmentManager(), "ExerciseDetails");
			}
		});
		exerciseListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				selectedExercise = typeExercises.get(position);
				LongClickDialog longClickDialog = new LongClickDialog();
				Bundle args = new Bundle();
				args.putString(LongClickDialog.SET_TITLE, selectedExercise.getName());
				args.putString(LongClickDialog.SET_TITLE_CONFIRM, selectedExercise.getName());
				args.putString(LongClickDialog.SET_CONTENT_CONFIRM, "Are you sure you want to delete this exercise?");
				longClickDialog.setArguments(args);
				longClickDialog.show(getFragmentManager(), "OnItemLongClick");
				return true;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.choose_exercise_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
		} else if (id == R.id.add_exercise) {
			Intent intent = new Intent(this, AddExercise.class);
			intent.putExtra(Ids.FORM_MODE_MODIFY, false);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onExerciseRepsChosen(long id, int reps, int position) {
		intent.putExtra(Ids.EXERCISE_ID, id);
		intent.putExtra(Ids.EXERCISE_REPS, reps);
		setResult(AddCrossfitWorkout.CHOOSE_EXERCISE_REQUESTCODE, intent);
		finish();
	}

	@Override
	public void onExerciseChosen() {
		ExercisePickerFragment exercisePickerFragment = new ExercisePickerFragment();
		Bundle args = new Bundle();
		args.putString(Ids.EXERCISE_NAME, selectedExercise.getName());
		args.putLong(Ids.EXERCISE_ID, selectedExercise.getId());
		exercisePickerFragment.setArguments(args);
		exercisePickerFragment.show(getFragmentManager(), "ExerciseDetails");
	}

	@Override
	public void onConfirmOrCancel(boolean confirm) {
		if(confirm) {
			onDecisionTaken(decision.DELETE);
		}
	}

	@Override
	public void onDecisionTaken(LongClickDialog.decision decision) {
		switch(decision) {
		case DELETE:
			if(exerciseDAO.deleteExercise(selectedExercise.getId())) {
				selectedExercise = null;
				resetButtons();
				legsButton.setBackgroundColor(Color.parseColor("#ffffff"));
				legsImage.setImageResource(R.drawable.legshdpi5);
				typeExercises = exerciseDAO.getExerciseByType(ExerciseType.legs);
				setupListView();
			}
			else {
				CustomAlertDialog alertDialog = new CustomAlertDialog();
				Bundle args = new Bundle();
				args.putString(Ids.ALERTDIALOG_TITLE, "Delete exercise");
				args.putString(Ids.ALERTDIALOG_CONTENT, "This exercise is used in one or several workouts. " +
						"You have to remove this exercise from these workouts before deleting it.");
				alertDialog.setArguments(args);
				alertDialog.show(getFragmentManager(), "alertdialog");
			}
			break;

		case MODIFY:
			Intent intent = new Intent(this, AddExercise.class);
			intent.putExtra(Ids.EXERCISE_ID, selectedExercise.getId());
			intent.putExtra(Ids.FORM_MODE_MODIFY, true);
			startActivity(intent);
			break;
		}
	}

	@Override
	public void onClick(View v) {

		int id = v.getId();
		if(id==R.id.button_legs) {
			resetButtons();
			legsButton.setBackgroundColor(Color.parseColor("#ffffff"));
			legsImage.setImageResource(R.drawable.legshdpi5);
			typeExercises = exerciseDAO.getExerciseByType(ExerciseType.legs);
			setupListView();
		}
		else if(id==R.id.button_chest) {
			resetButtons();
			chestButton.setBackgroundColor(Color.parseColor("#ffffff"));
			chestImage.setImageResource(R.drawable.chesthdpi5);
			typeExercises = exerciseDAO.getExerciseByType(ExerciseType.chest);
			setupListView();
		}
		else if(id==R.id.button_arms) {
			resetButtons();
			armsButton.setBackgroundColor(Color.parseColor("#ffffff"));
			armsImage.setImageResource(R.drawable.armshdpi5);
			typeExercises = exerciseDAO.getExerciseByType(ExerciseType.arms);
			setupListView();
		}
		else if(id==R.id.button_back) {
			resetButtons();
			backButton.setBackgroundColor(Color.parseColor("#ffffff"));
			backImage.setImageResource(R.drawable.backhdpi5);
			typeExercises = exerciseDAO.getExerciseByType(ExerciseType.back);
			setupListView();
		}
		else if(id==R.id.button_abs) {
			resetButtons();
			absButton.setBackgroundColor(Color.parseColor("#ffffff"));
			absImage.setImageResource(R.drawable.abshdpi5);
			typeExercises = exerciseDAO.getExerciseByType(ExerciseType.abs);
			setupListView();
		}
	}

	private void resetButtons() {
		legsButton.setBackgroundColor(Color.parseColor("#444444"));
		legsImage.setImageResource(R.drawable.legshdpi5white);
		chestButton.setBackgroundColor(Color.parseColor("#444444"));
		chestImage.setImageResource(R.drawable.chesthdpi5white);
		armsButton.setBackgroundColor(Color.parseColor("#444444"));
		armsImage.setImageResource(R.drawable.armshdpi5white);
		backButton.setBackgroundColor(Color.parseColor("#444444"));
		backImage.setImageResource(R.drawable.backhdpi5white);
		absButton.setBackgroundColor(Color.parseColor("#444444"));
		absImage.setImageResource(R.drawable.abshdpi5white);
	}
}