package com.xwk.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jbvincey.xwk.R;
import com.xwk.database.CrossfitWorkoutDAO;
import com.xwk.database.ExerciseDAO;
import com.xwk.fragments.CustomAlertDialog;
import com.xwk.fragments.DialogActivity;
import com.xwk.fragments.ExerciseDetailsFragment;
import com.xwk.fragments.ExercisePickerFragment;
import com.xwk.fragments.LongClickDialog;
import com.xwk.fragments.LongClickDialog.decision;
import com.xwk.fragments.SetsFragment;
import com.xwk.misc.ExerciseRawAdapter;
import com.xwk.model.CrossfitWorkout;
import com.xwk.model.Exercise;
import com.xwk.model.Ids;

public class AddCrossfitWorkout extends DialogActivity implements OnClickListener {

	public final static int CHOOSE_EXERCISE_REQUESTCODE = 1;
	private final static String ON_SAVE_EXERCISE_LIST_ID = "onSaveExerciseListId";
	private final static String ON_SAVE_EXERCISE_LIST_REPS = "onSaveExerciseListReps";
	private final static String ON_SAVE_REMOVED_EXERCISES_POSITION = "onSaveRemovedExercisesPosition";

	private CrossfitWorkoutDAO crossfitWorkoutDAO;
	private ExerciseDAO exerciseDAO;

	private EditText crossfitWorkoutName;
	private EditText crossfitWorkoutDescription;
	private TextView crossfitWorkoutSets;
	private Button addExercise;
	private ListView exerciseListView;

	private CrossfitWorkout crossfitWorkout;
	private Activity activity;

	//private List<Exercise> newExercises = new ArrayList<Exercise>();
	//private List<Integer> removedExercises = new ArrayList<Integer>();
	private Exercise selectedExercise;
	private int exercisePosition = -1;

	private boolean modifyMode = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.add_crossfitworkout);

		crossfitWorkoutDAO = CrossfitWorkoutDAO.getInstance(this);
		crossfitWorkoutDAO.open();

		exerciseDAO = ExerciseDAO.getInstance(this);
		exerciseDAO.open();

		getActionBar().setDisplayHomeAsUpEnabled(true);

		crossfitWorkoutName = (EditText) findViewById(R.id.add_crossfitworkout_name);
		crossfitWorkoutDescription = (EditText) findViewById(R.id.add_crossfitworkout_description);
		crossfitWorkoutSets = (TextView) findViewById(R.id.add_crossfitworkout_sets);
		crossfitWorkoutSets.setOnClickListener(this);
		addExercise = (Button) findViewById(R.id.addworkout_addexercise_button);
		addExercise.setOnClickListener(this);
		exerciseListView = (ListView) findViewById(R.id.addworkout_exercise_listview);

		activity = this;
		this.getActionBar().setTitle("  Add new workout");

		crossfitWorkout = new CrossfitWorkout();

		Bundle bundle = getIntent().getExtras();
		if(bundle != null && 
				bundle.containsKey(Ids.FORM_MODE_MODIFY) && 
				bundle.getBoolean(Ids.FORM_MODE_MODIFY) &&
				bundle.containsKey(Ids.CROSSFIT_WORKOUT_ID)) {
			crossfitWorkout = crossfitWorkoutDAO.getCrossfitWorkout(bundle.getLong(Ids.CROSSFIT_WORKOUT_ID));
			crossfitWorkoutName.setText(crossfitWorkout.getName());
			crossfitWorkoutDescription.setText(crossfitWorkout.getDescription());
			crossfitWorkoutSets.setText("Number of sets: " + crossfitWorkout.getSets());
			modifyMode = true;
			this.getActionBar().setTitle("  Edit workout");

		}



	}

	@Override
	protected void onResume() {
		super.onResume();
		crossfitWorkoutDAO.open();
		exerciseDAO.open();
		setupListView();
	}

	@Override
	protected void onPause() {
		super.onPause();
		crossfitWorkoutDAO.close();
		exerciseDAO.close();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		if(selectedExercise!=null && exercisePosition!=-1) {
			savedInstanceState.putInt(Ids.EXERCISE_POSITION, exercisePosition);
		}
		ArrayList<String> exercisesId = new ArrayList<String>();
		ArrayList<String> exercisesReps = new ArrayList<String>();
		ArrayList<String> removedExercisesString = new ArrayList<String>();
		for(Exercise exercise: crossfitWorkout.getExercises()) {
			exercisesId.add(String.valueOf(exercise.getId()));
			exercisesReps.add(String.valueOf(exercise.getReps()));
		}
		//for(Integer removedPosition: removedExercises) {
		//	removedExercisesString.add(String.valueOf(removedPosition));
		//}
		savedInstanceState.putStringArrayList(ON_SAVE_EXERCISE_LIST_ID, exercisesId);
		savedInstanceState.putStringArrayList(ON_SAVE_EXERCISE_LIST_REPS, exercisesReps);
		savedInstanceState.putStringArrayList(ON_SAVE_REMOVED_EXERCISES_POSITION, removedExercisesString);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		exerciseDAO.open();
		if(savedInstanceState!=null && savedInstanceState.containsKey(ON_SAVE_EXERCISE_LIST_ID) && savedInstanceState.containsKey(ON_SAVE_EXERCISE_LIST_REPS)) {
			ArrayList<String> exerciseId = savedInstanceState.getStringArrayList(ON_SAVE_EXERCISE_LIST_ID);
			ArrayList<String> exerciseReps = savedInstanceState.getStringArrayList(ON_SAVE_EXERCISE_LIST_REPS);
			if(exerciseId.size() > 0) {
				crossfitWorkout.getExercises().clear();
				int i = 0;
				for(String exerciseIdString: exerciseId) {
					Exercise exercise = exerciseDAO.getExercise(Long.parseLong(exerciseIdString));
					exercise.setReps(Integer.parseInt(exerciseReps.get(i)));
					crossfitWorkout.getExercises().add(exercise);
					i++;
				}
			}
		}
		/*if(savedInstanceState!=null && savedInstanceState.containsKey(ON_SAVE_REMOVED_EXERCISES_POSITION)) {
			ArrayList<String> removedExercisesString = savedInstanceState.getStringArrayList(ON_SAVE_REMOVED_EXERCISES_POSITION);
			for(String removedExercisePosition: removedExercisesString) {
				removedExercises.add(Integer.parseInt(removedExercisePosition));
				crossfitWorkout.getExercises().remove(Integer.parseInt(removedExercisePosition));
			}
		}*/
		if(savedInstanceState!=null && savedInstanceState.containsKey(Ids.EXERCISE_POSITION)) {
			int position = savedInstanceState.getInt(Ids.EXERCISE_POSITION);
			if(crossfitWorkout.getExercises().size() > position) {
				exercisePosition = position;
				selectedExercise = crossfitWorkout.getExercises().get(exercisePosition);
			}
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.addworkout_addexercise_button) {
			Intent intent = new Intent(this, ChooseExercise.class);
			startActivityForResult(intent, CHOOSE_EXERCISE_REQUESTCODE);
		}
		else if(id == R.id.add_crossfitworkout_sets) {
			SetsFragment setsFragment = new SetsFragment();
			if(crossfitWorkout.getSets()!=0) {
				Bundle args = new Bundle();
				args.putInt(Ids.CROSSFIT_WORKOUT_SETS, crossfitWorkout.getSets());
				setsFragment.setArguments(args);
			}
			setsFragment.show(getFragmentManager(), "setsFragment");
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CHOOSE_EXERCISE_REQUESTCODE) {
			crossfitWorkoutDAO.open();
			exerciseDAO.open();
			Bundle bundle = data.getExtras();
			if (null!=bundle && bundle.containsKey(Ids.EXERCISE_ID) && bundle.containsKey(Ids.EXERCISE_REPS)) {
				long id = bundle.getLong(Ids.EXERCISE_ID);
				if(id!=0) {
					Exercise exercise = exerciseDAO.getExercise(id);
					exercise.setReps(bundle.getInt(Ids.EXERCISE_REPS));
					//newExercises.add(exercise);
					crossfitWorkout.getExercises().add(exercise);
					setupListView();
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.addcrossfitworkout, menu);
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
		} else if (id == R.id.check_add_crossfitworkout) {
			String name = crossfitWorkoutName.getText().toString();
			String description = crossfitWorkoutDescription.getText().toString();
			Iterator<Exercise> iter = crossfitWorkout.getExercises().iterator();

			while(iter.hasNext()) {				
				Exercise exercise = iter.next();
				try {
					exerciseDAO.getExercise(exercise.getId());
				}
				catch (CursorIndexOutOfBoundsException e) {
					iter.remove();
					setupListView();
				}
			}
			/*for(Exercise exercise: crossfitWorkout.getExercises()) {
				try {
					exerciseDAO.getExercise(exercise.getId());
				}
				catch (CursorIndexOutOfBoundsException e) {
					crossfitWorkout.getExercises().remove(exercise);
					setupListView();
				}
			}*/
			if(!name.isEmpty() && !description.isEmpty() && crossfitWorkout.getExercises().size() > 0) {
				crossfitWorkout.setName(name);
				crossfitWorkout.setDescription(description);
				
				if(modifyMode) {
					crossfitWorkoutDAO.updateCrossfitWorkout(crossfitWorkout);
				}
				else {
					crossfitWorkoutDAO.addCrossfitWorkout(crossfitWorkout);
				}
				finish();
			}
			else {
				CustomAlertDialog alertDialog = new CustomAlertDialog();
				Bundle args = new Bundle();
				args.putString(Ids.ALERTDIALOG_TITLE, "New workout");
				args.putString(Ids.ALERTDIALOG_CONTENT, "You have to fill in all fields and to add at least one exercise.");
				alertDialog.setArguments(args);
				alertDialog.show(getFragmentManager(), "alertdialog");
			}
		}
		return super.onOptionsItemSelected(item);
	}

	private void setupListView() {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		data.clear();

		for(Exercise exercise: crossfitWorkout.getExercises()) {
			Map<String, Object> datum = new HashMap<String, Object>(3);
			datum.put(Ids.EXERCISE_NAME, exercise.getName());
			datum.put(Ids.EXERCISE_REPS, exercise.getReps() + " reps");

			Bitmap bmp = exercise.getTypeImage(this);

			datum.put(Ids.EXERCISE_PIC, bmp);

			data.add(datum);
		}

		ExerciseRawAdapter adapter = new ExerciseRawAdapter(this, data, R.layout.rowlist_exercise, new String[] {Ids.EXERCISE_NAME, Ids.EXERCISE_REPS, Ids.EXERCISE_PIC}, new int[] {R.id.item1, R.id.item2, R.id.exercise_row_image});
		exerciseListView.setAdapter(adapter);

		exerciseListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Exercise exercise = crossfitWorkout.getExercises().get(position);
				ExerciseDetailsFragment exerciseDetailsFragment = new ExerciseDetailsFragment();
				Bundle args = new Bundle();
				args.putString(Ids.EXERCISE_NAME, exercise.getName());
				args.putString(Ids.EXERCISE_DESCRIPTION, exercise.getDescription());
				args.putByteArray(Ids.EXERCISE_TYPE, exercise.getTypeBytearrayWhite(activity));
				args.putByteArray(Ids.EXERCISE_PIC, exercise.getImage());
				exerciseDetailsFragment.setArguments(args);
				exerciseDetailsFragment.show(getFragmentManager(), "ExerciseDetails");
			}
		});
		exerciseListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				selectedExercise = crossfitWorkout.getExercises().get(position);
				exercisePosition = position;
				LongClickDialog longClickDialog = new LongClickDialog();
				Bundle args = new Bundle();
				args.putString(LongClickDialog.SET_TITLE, selectedExercise.getName());
				args.putString(LongClickDialog.SET_TITLE_CONFIRM, selectedExercise.getName());
				args.putString(LongClickDialog.SET_CONTENT_CONFIRM, "Are you sure you want to remove this exercise from the workout?");
				longClickDialog.setArguments(args);
				longClickDialog.show(getFragmentManager(), "OnItemLongClick");
				return true;
			}
		});
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
			int crossfitExercisesSize = crossfitWorkout.getExercises().size();
			if(exercisePosition!=-1 && selectedExercise!=null && crossfitExercisesSize > exercisePosition) {
				crossfitWorkout.getExercises().remove(exercisePosition);
				//if(exercisePosition > (crossfitExercisesSize-1)) {
				//	newExercises.remove(exercisePosition - crossfitExercisesSize);
				//}
				//else {
				//	removedExercises.add(exercisePosition);
				//}
				setupListView();
			}
			break;
		case MODIFY:
			if(exercisePosition!=-1 && selectedExercise!=null) {
				ExercisePickerFragment exercisePickerFragment = new ExercisePickerFragment();
				Bundle args = new Bundle();
				args.putString(Ids.EXERCISE_NAME, selectedExercise.getName());
				args.putLong(Ids.EXERCISE_ID, selectedExercise.getId());args.putInt(Ids.EXERCISE_POSITION, exercisePosition);
				args.putInt(Ids.EXERCISE_REPS, selectedExercise.getReps());
				exercisePickerFragment.setArguments(args);
				exercisePickerFragment.show(getFragmentManager(), "ModifyRepsExercise");
			}
			break;
		}
	}

	@Override
	public void onExerciseRepsChosen(long id, int reps, int position) {
		if(crossfitWorkout.getExercises().size() > position) {
			Exercise exercise = crossfitWorkout.getExercises().get(position);
			exercise.setReps(reps);
			setupListView();
		}
	}

	@Override
	public void onSetsChosen(int sets) {
		crossfitWorkout.setSets(sets);
		crossfitWorkoutSets.setText("Number of sets: " + String.valueOf(sets));
	}

}
