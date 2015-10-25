package com.xwk.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.jbvincey.xwk.R;
import com.xwk.database.CrossfitPerformanceDAO;
import com.xwk.database.CrossfitWorkoutDAO;
import com.xwk.database.ExerciseDAO;
import com.xwk.fragments.CustomAlertDialog;
import com.xwk.fragments.DialogActivity;
import com.xwk.fragments.LongClickDialog;
import com.xwk.fragments.LongClickDialog.decision;
import com.xwk.misc.CrossfitRawAdapter;
import com.xwk.model.CrossfitWorkout;
import com.xwk.model.Ids;



public class CrossfitWorkoutList extends DialogActivity {

	public final static String MyPref = "SharedPreferences";
	public final static String Pref_first_launch = "firslaunch";

	private CrossfitWorkoutDAO crossfitWorkoutDAO;
	private CrossfitPerformanceDAO crossfitPerformanceDAO;
	private ExerciseDAO exerciseDAO;
	private ListView workoutListView;
	private Activity activity;

	private List<CrossfitWorkout> crossfitWorkouts;
	private CrossfitWorkout selectedWorkout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.crossfitworkout_list);

		crossfitWorkoutDAO = CrossfitWorkoutDAO.getInstance(this);
		crossfitWorkoutDAO.open();

		crossfitPerformanceDAO = CrossfitPerformanceDAO.getInstance(this);
		crossfitPerformanceDAO.open();

		exerciseDAO = ExerciseDAO.getInstance(this);
		exerciseDAO.open();

		//getActionBar().setDisplayHomeAsUpEnabled(true);

		workoutListView = (ListView) findViewById(R.id.workout_listview);
		workoutListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		activity = this;

		this.getActionBar().setTitle("");
		/*if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/

	}

	@Override
	protected void onResume() {
		super.onResume();
		crossfitWorkoutDAO.open();
		crossfitPerformanceDAO.open();
		exerciseDAO.open();
		setupListview();

		if(isFirstLaunch()) {

			CustomAlertDialog alertDialog = new CustomAlertDialog();
			Bundle args = new Bundle();
			args.putString(Ids.ALERTDIALOG_TITLE, "XWK Terms of Use");
			String content = "XWK does not take any responsibility or liability for any injuries happening during a workout while using" +
					" this application. Practicing sport is something to take seriously. Make sure to warmup before, and stretch " +
					"after each workout. When learning a new exercise, make slow movements until you are sure to make it right. ";
			args.putString(Ids.ALERTDIALOG_CONTENT, content);
			alertDialog.setArguments(args);
			alertDialog.show(getFragmentManager(), "alertdialog");

			saveFirstLaunch();
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		crossfitWorkoutDAO.close();
		crossfitPerformanceDAO.close();
		exerciseDAO.close();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);

		if(selectedWorkout!=null) {
			savedInstanceState.putLong(Ids.CROSSFIT_WORKOUT_ID, selectedWorkout.getId());
		}
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		crossfitWorkoutDAO.open();
		if(savedInstanceState!=null && savedInstanceState.containsKey(Ids.CROSSFIT_WORKOUT_ID)) {
			selectedWorkout = crossfitWorkoutDAO.getCrossfitWorkout(savedInstanceState.getLong(Ids.CROSSFIT_WORKOUT_ID));
		}
	}

	private void setupListview() {
		crossfitWorkouts = crossfitWorkoutDAO.getAllCrossfitWorkouts();

		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		data.clear();

		for(CrossfitWorkout crossfitWorkout: crossfitWorkouts) {
			Map<String, String> datum = new HashMap<String, String>(2);
			datum.put(Ids.CROSSFIT_WORKOUT_NAME, crossfitWorkout.getName());
			datum.put(Ids.CROSSFIT_WORKOUT_DESCRIPTION, crossfitWorkout.getDescription());
			datum.put(Ids.CROSSFIT_WORKOUT_TYPES, crossfitWorkout.getTypes());
			data.add(datum);
		}

		CrossfitRawAdapter adapter = new CrossfitRawAdapter(this, data, R.layout.rowlist_workout, new String[] {Ids.CROSSFIT_WORKOUT_NAME, Ids.CROSSFIT_WORKOUT_DESCRIPTION, Ids.CROSSFIT_WORKOUT_TYPES}, new int[] {R.id.item1, R.id.item2, R.id.rowlist_workout_types});
		//SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.rowlist_workout, new String[] {Ids.CROSSFIT_WORKOUT_NAME, Ids.CROSSFIT_WORKOUT_DESCRIPTION}, new int[] {R.id.item1, R.id.item2});
		workoutListView.setAdapter(adapter);

		workoutListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				CrossfitWorkout crossfitWorkout = crossfitWorkouts.get(position);
				Intent intent = new Intent(activity, CrossfitWorkoutActivity.class);
				intent.putExtra(Ids.CROSSFIT_WORKOUT_ID, crossfitWorkout.getId());
				startActivity(intent);
			}
		});
		workoutListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				selectedWorkout = crossfitWorkouts.get(position);
				LongClickDialog longClickDialog = new LongClickDialog();
				Bundle args = new Bundle();
				args.putString(LongClickDialog.SET_TITLE, selectedWorkout.getName());
				args.putString(LongClickDialog.SET_TITLE_CONFIRM, selectedWorkout.getName());
				args.putString(LongClickDialog.SET_CONTENT_CONFIRM, "Are you sure you want to delete this workout?");
				longClickDialog.setArguments(args);
				longClickDialog.show(getFragmentManager(), "OnItemLongClick");
				return true;
			}
		});
	}

	private void saveFirstLaunch() {
		SharedPreferences firstLaunch = getSharedPreferences(MyPref, MODE_PRIVATE);
		SharedPreferences.Editor editor = firstLaunch.edit();
		editor.putBoolean(Pref_first_launch, false);
		editor.commit();
	}

	private boolean isFirstLaunch() {
		SharedPreferences firstLaunch = getSharedPreferences(MyPref, MODE_PRIVATE);
		boolean isFirstLaunch = firstLaunch.getBoolean(Pref_first_launch, true);
		return isFirstLaunch;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.crossfitworkout_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			//finish();
		} else if (id == R.id.add_crossfitworkout) {
			Intent intent = new Intent(this, AddCrossfitWorkout.class);
			intent.putExtra(Ids.FORM_MODE_MODIFY, false);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
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
			crossfitWorkoutDAO.deleteCrossfitWorkout(selectedWorkout.getId());
			selectedWorkout = null;
			setupListview();
			break;

		case MODIFY:
			Intent intent = new Intent(this, AddCrossfitWorkout.class);
			intent.putExtra(Ids.CROSSFIT_WORKOUT_ID, selectedWorkout.getId());
			intent.putExtra(Ids.FORM_MODE_MODIFY, true);
			startActivity(intent);
			break;
		}
	}
}
