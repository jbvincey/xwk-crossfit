package com.xwk.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.jbvincey.xwk.R;
import com.xwk.database.CrossfitPerformanceDAO;
import com.xwk.database.CrossfitWorkoutDAO;
import com.xwk.database.ExerciseDAO;
import com.xwk.fragments.CustomAlertDialogDoubleButton;
import com.xwk.fragments.DialogActivity;
import com.xwk.fragments.ExerciseDetailsFragment;
import com.xwk.fragments.WorkoutEndFragment;
import com.xwk.misc.BlurableImageView;
import com.xwk.misc.RoundedDrawable;
import com.xwk.model.CrossfitPerformance;
import com.xwk.model.CrossfitWorkout;
import com.xwk.model.Exercise;
import com.xwk.model.Ids;

public class CrossfitWorkoutTraining extends DialogActivity implements OnClickListener {

	private static final String ON_SAVE_TIME = "on_save_time";
	private static final String ON_SAVE_STARTED = "on_save_started";
	private static final String CURRENT_SET = "current_set";

	private CrossfitWorkoutDAO crossfitWorkoutDAO;
	private CrossfitPerformanceDAO crossfitPerformanceDAO;
	private ExerciseDAO exerciseDAO;

	private ListView exerciseListView;
	private TextView timerValue;
	private TextView exerciseName;
	private Button nextExercise;
	private BlurableImageView exerciseImage;
	//private ImageView percentage1;
	//private ImageView percentage2;
	//private LinearLayout.LayoutParams layoutParams1;
	//private LinearLayout.LayoutParams layoutParams2;
	private TextView headerListTextProgress;
	private TextView headerListTextSet;


	private long startTime = 0L;
	private Handler customHandler = new Handler();
	private long timeInMilliseconds = 0L;
	private long timeSwapBuff = 0L;
	private long updatedTime = 0L;
	private boolean exerciseStarted = false;

	private int exercisePosition = -1;
	private int currentSet = 0;
	private Exercise selectedExercise = null;

	private CrossfitWorkout crossfitWorkout;
	private Activity activity;

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.crossfitworkout_training);

		crossfitWorkoutDAO = CrossfitWorkoutDAO.getInstance(this);
		crossfitWorkoutDAO.open();

		crossfitPerformanceDAO = CrossfitPerformanceDAO.getInstance(this);
		crossfitPerformanceDAO.open();

		exerciseDAO = ExerciseDAO.getInstance(this);
		exerciseDAO.open();

		getActionBar().setDisplayHomeAsUpEnabled(true);

		exerciseListView = (ListView) findViewById(R.id.crossfitworkout_training_exercise_listview);

		timerValue = (TextView) findViewById(R.id.crossfitworkout_training_timer);
		exerciseName = (TextView) findViewById(R.id.crossfitworkout_training_exercisename);

		nextExercise = (Button) findViewById(R.id.crossfitworkout_training_next);
		nextExercise.setOnClickListener(this);

		exerciseImage = (BlurableImageView) findViewById(R.id.crossfitworkout_training_exerciseimage);

		//percentage1 = (ImageView) findViewById(R.id.crossfitworkout_training_percentage1);
		//layoutParams1 = (LinearLayout.LayoutParams) percentage1.getLayoutParams();
		//percentage2 = (ImageView) findViewById(R.id.crossfitworkout_training_percentage2);
		//layoutParams2 = (LinearLayout.LayoutParams) percentage2.getLayoutParams();

		crossfitWorkout = CrossfitWorkout.recoverCrossfitWorkout(getIntent().getExtras(), crossfitWorkoutDAO);      

		activity = this;

		activity.setTitle("  "+crossfitWorkout.getName());

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
				mDrawerLayout,         /* DrawerLayout object */
				R.drawable.ic_navigation_drawer,  /* nav drawer icon to replace 'Up' caret */
				R.string.drawer_open,  /* "open drawer" description */
				R.string.drawer_close  /* "close drawer" description */
				) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				//getActionBar().setTitle(crossfitWorkout.getName());
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				//getActionBar().setTitle("Exercices");
				//if(exercisePosition!=-1) {
				//exerciseListView.requestFocusFromTouch();
				//exerciseListView.setSelection(exercisePosition + 1);
				//}
				//exerciseListView.smoothScrollToPosition(exercisePosition+1);
				//exerciseListView.setItemChecked(exercisePosition+1, true);
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		setupView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		crossfitWorkoutDAO.open();
		crossfitPerformanceDAO.open();
		exerciseDAO.open();
		if(exerciseStarted || timeSwapBuff!=0) {
			customHandler.postDelayed(updateTimerThread, 0);
		}

	}


	@Override
	protected void onPause() {
		super.onPause();
		crossfitWorkoutDAO.close();
		crossfitPerformanceDAO.close();
		exerciseDAO.close();
		//customHandler.removeCallbacks(updateTimerThread);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		if(selectedExercise!=null && exercisePosition!=-1) {
			savedInstanceState.putInt(Ids.EXERCISE_POSITION, exercisePosition);
		}
		savedInstanceState.putInt(CURRENT_SET, currentSet);
		//timeSwapBuff += timeInMilliseconds;

		savedInstanceState.putLong(ON_SAVE_TIME, timeSwapBuff);
		savedInstanceState.putBoolean(ON_SAVE_STARTED, exerciseStarted);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if(savedInstanceState!=null && savedInstanceState.containsKey(Ids.EXERCISE_POSITION) && savedInstanceState.containsKey(CURRENT_SET)) {
			currentSet = savedInstanceState.getInt(CURRENT_SET);
			exercisePosition = savedInstanceState.getInt(Ids.EXERCISE_POSITION);
			selectedExercise = crossfitWorkout.getExercises().get(exercisePosition);
			if(exercisePosition == (crossfitWorkout.getExercises().size() - 1) && currentSet == crossfitWorkout.getSets()) {
				nextExercise.setText("End workout");
			}
			else {
				nextExercise.setText("Next exercise");
			}
			exerciseName.setText(selectedExercise.getReps() + " " + selectedExercise.getName());
			headerListTextProgress.setText("Progress: " + ((exercisePosition + crossfitWorkout.getExercises().size()*(currentSet-1))*100/(crossfitWorkout.getExercises().size()*crossfitWorkout.getSets())) +"%");
			headerListTextSet.setText("Set " + currentSet + " / " + crossfitWorkout.getSets());
		}
		if(savedInstanceState!=null && savedInstanceState.containsKey(ON_SAVE_TIME)
				&& savedInstanceState.containsKey(ON_SAVE_STARTED)) {
			timeSwapBuff = savedInstanceState.getLong(ON_SAVE_TIME);
			startTime = SystemClock.uptimeMillis();
			exerciseStarted = savedInstanceState.getBoolean(ON_SAVE_STARTED);

		}
	}

	private void setupView() {
		selectedExercise = crossfitWorkout.getExercises().get(0);
		exerciseName.setText(selectedExercise.getReps() + " " + selectedExercise.getName());

		byte[] byteArray = selectedExercise.getImage();
		Bitmap bmp = null;

		if(byteArray!=null && byteArray.length>0) {
			bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
		}
		else {
			BitmapFactory.Options options = new BitmapFactory.Options();
			bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.nopicture, options);
		}
		RoundedDrawable picture = new RoundedDrawable(bmp);
		exerciseImage.setImageDrawable(picture);
		//exerciseImage.fastblur(bmp, 10, bmp.getWidth(), bmp.getHeight());

		View headerListView = getLayoutInflater().inflate(R.layout.header_listview, null);
		headerListTextProgress = (TextView) headerListView.findViewById(R.id.header_listview_progress);
		headerListTextProgress.setText("Progress: 0%");
		headerListTextSet = (TextView) headerListView.findViewById(R.id.header_listview_set);
		headerListTextSet.setText("Set 1 / " + crossfitWorkout.getSets());
		exerciseListView.addHeaderView(headerListView);

		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		data.clear();

		for(Exercise exercise: crossfitWorkout.getExercises()) {
			Map<String, String> datum = new HashMap<String, String>();
			datum.put(Ids.EXERCISE_NAME, exercise.getName());
			datum.put(Ids.EXERCISE_REPS, exercise.getReps() + " reps");

			//Bitmap bmp2 = exercise.getTypeImage(this);

			//datum.put(Ids.EXERCISE_PIC, bmp2);

			data.add(datum);
		}

		//ExerciseRawAdapter adapter = new ExerciseRawAdapter(this, data, R.layout.rowlist_exercise, new String[] {Ids.EXERCISE_NAME, Ids.EXERCISE_REPS, Ids.EXERCISE_PIC}, new int[] {R.id.item1, R.id.item2, R.id.exercise_row_image});
		SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.rowlist_workout_training, new String[] {Ids.EXERCISE_NAME, Ids.EXERCISE_REPS}, new int[] {R.id.item1, R.id.item2});
		exerciseListView.setAdapter(adapter);

		exerciseListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position!=0) {
					Exercise exercise = crossfitWorkout.getExercises().get(position - 1);
					ExerciseDetailsFragment exerciseDetailsFragment = new ExerciseDetailsFragment();
					Bundle args = new Bundle();
					args.putString(Ids.EXERCISE_NAME, exercise.getName());
					args.putString(Ids.EXERCISE_DESCRIPTION, exercise.getDescription());
					args.putByteArray(Ids.EXERCISE_TYPE, exercise.getTypeBytearrayWhite(activity));
					args.putByteArray(Ids.EXERCISE_PIC, exercise.getImage());
					exerciseDetailsFragment.setArguments(args);
					exerciseDetailsFragment.show(getFragmentManager(), "ExerciseDetails");
				}
			}
		});

	}

	private Runnable updateTimerThread = new Runnable() {

		public void run() {
			timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
			updatedTime = timeSwapBuff + timeInMilliseconds;
			int secs = (int) (updatedTime / 1000);
			int mins = secs / 60;
			secs = secs % 60;
			//int milliseconds = (int) (updatedTime % 100);
			timerValue.setText("" + String.format("%02d", mins) + ":"
					+ String.format("%02d", secs));
			//+ ":"
			//+ String.format("%02d", milliseconds));
			customHandler.postDelayed(this, 1000);
		}
	};

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		if(viewId == R.id.crossfitworkout_training_next) {

			if(exercisePosition == -1 && currentSet==0) {
				exerciseStarted = true;
				startTime = SystemClock.uptimeMillis();
				customHandler.postDelayed(updateTimerThread, 0);
				nextExercise.setText("Next exercise");
				exercisePosition = 0;
				currentSet = 1;
				selectedExercise = crossfitWorkout.getExercises().get(exercisePosition);
				exerciseName.setText(selectedExercise.getReps() + " " + selectedExercise.getName());

				headerListTextProgress.setText("Progress: " + ((exercisePosition + crossfitWorkout.getExercises().size()*(currentSet-1))*100/(crossfitWorkout.getExercises().size()*crossfitWorkout.getSets())) +"%");

				byte[] byteArray = selectedExercise.getImage();
				Bitmap bmp = null;
				if(byteArray!=null && byteArray.length>0) {
					bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
				}
				else {
					BitmapFactory.Options options = new BitmapFactory.Options();
					bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.nopicture, options);
				}
				RoundedDrawable picture = new RoundedDrawable(bmp);
				exerciseImage.setImageDrawable(picture);
				if(exercisePosition == (crossfitWorkout.getExercises().size() - 1) && currentSet == crossfitWorkout.getSets()) {
					nextExercise.setText("Finish");
				}
				//View currentExercise = exerciseListView.getChildAt(exercisePosition+1);
				//currentExercise.setBackgroundColor(Color.parseColor("#99E8A7A7"));
				exerciseListView.setItemChecked(1, true);
			}
			else if(exercisePosition < (crossfitWorkout.getExercises().size()-1)) {
				exercisePosition++;
				selectedExercise = crossfitWorkout.getExercises().get(exercisePosition);
				exerciseName.setText(selectedExercise.getReps() + " " + selectedExercise.getName());

				headerListTextProgress.setText("Progress: " + ((exercisePosition + crossfitWorkout.getExercises().size()*(currentSet-1))*100/(crossfitWorkout.getExercises().size()*crossfitWorkout.getSets())) +"%");

				byte[] byteArray = selectedExercise.getImage();
				Bitmap bmp = null;
				if(byteArray!=null && byteArray.length>0) {
					bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
				}
				else {
					BitmapFactory.Options options = new BitmapFactory.Options();
					bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.nopicture, options);
				}
				RoundedDrawable picture = new RoundedDrawable(bmp);
				exerciseImage.setImageDrawable(picture);

				if(exercisePosition == (crossfitWorkout.getExercises().size() - 1) && currentSet == crossfitWorkout.getSets()) {
					nextExercise.setText("Finish");
				}
				//exerciseListView.setSelection(exercisePosition+1);
				exerciseListView.smoothScrollToPosition(exercisePosition+1);
				exerciseListView.setItemChecked(exercisePosition+1, true);
				//View currentExercise = exerciseListView.getChildAt(exercisePosition+1);
				//currentExercise.setBackgroundColor(Color.parseColor("#99E8A7A7"));
				//View previousExercise = exerciseListView.getChildAt(exercisePosition);
				//if(previousExercise!=null){
				//	previousExercise.setBackgroundColor(Color.WHITE);
				//}			
			}
			else {
				if(currentSet < crossfitWorkout.getSets()) {
					currentSet++;
					exercisePosition = 0;
					selectedExercise = crossfitWorkout.getExercises().get(exercisePosition);
					exerciseName.setText(selectedExercise.getReps() + " " + selectedExercise.getName());

					headerListTextProgress.setText("Progress: " + ((exercisePosition + crossfitWorkout.getExercises().size()*(currentSet-1))*100/(crossfitWorkout.getExercises().size()*crossfitWorkout.getSets())) +"%");
					headerListTextSet.setText("Set " + currentSet + " / " + crossfitWorkout.getSets());

					byte[] byteArray = selectedExercise.getImage();
					Bitmap bmp = null;
					if(byteArray!=null && byteArray.length>0) {
						bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
					}
					else {
						BitmapFactory.Options options = new BitmapFactory.Options();
						bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.nopicture, options);
					}
					RoundedDrawable picture = new RoundedDrawable(bmp);
					exerciseImage.setImageDrawable(picture);

					exerciseListView.smoothScrollToPosition(1);
					if(exercisePosition == (crossfitWorkout.getExercises().size() - 1) && currentSet == crossfitWorkout.getSets()) {
						nextExercise.setText("Finish");
					}
					exerciseListView.setItemChecked(1, true);
					//View currentExercise = exerciseListView.getChildAt(1);
					//currentExercise.setBackgroundColor(Color.parseColor("#99E8A7A7"));
					//View previousExercise = exerciseListView.getChildAt(crossfitWorkout.getExercises().size());
					//if(previousExercise!=null){
					//	previousExercise.setBackgroundColor(Color.WHITE);
					//}
				}
				else {

					timeSwapBuff += timeInMilliseconds;
					timeInMilliseconds = 0;
					customHandler.removeCallbacks(updateTimerThread);

					int secs = (int) (timeSwapBuff / 1000);
					int mins = secs / 60;
					secs = secs % 60;

					WorkoutEndFragment workoutEndFragment = new WorkoutEndFragment();
					Bundle args = new Bundle();
					args.putString(WorkoutEndFragment.WORKOUT_END_FRAGMENT_TITLE, crossfitWorkout.getName());
					args.putString(WorkoutEndFragment.WORKOUT_END_FRAGMENT_MESSAGE, "Time: " + String.format("%02d", mins) + ":" + String.format("%02d", secs));
					workoutEndFragment.setArguments(args);
					workoutEndFragment.show(getFragmentManager(), "WorkoutEndFragment");

				}
			}
			int weight = (exercisePosition + 1) / (crossfitWorkout.getExercises().size());
			//layoutParams1.weight = (float) weight;
			//layoutParams2.weight = (float) (1 - weight);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.crossfitworkout_training, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	private void backPressedConfirmed() {
		super.onBackPressed();
	}

	@Override       
	public void onBackPressed(){
		if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
			mDrawerLayout.closeDrawer(Gravity.LEFT);
			//exerciseImage.requestFocus();
		} else {
			CustomAlertDialogDoubleButton alertDialog = new CustomAlertDialogDoubleButton();
			Bundle args = new Bundle();
			args.putString(Ids.ALERTDIALOG_TITLE, "Stop training");
			args.putString(Ids.ALERTDIALOG_CONTENT, "Are you sure you want to quit this workout?");
			alertDialog.setArguments(args);
			alertDialog.show(getFragmentManager(), "alertdialog");

			/*AlertDialog.Builder builder = new AlertDialog.Builder(activity);

			builder.setMessage("Êtes vous sûr de vouloir quitter l'entraînement?")
			.setPositiveButton("Annuler", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			})
			.setNegativeButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					backPressedConfirmed();
				}
			});
			AlertDialog alertDialog = builder.create();

			alertDialog.show();

			Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
			positiveButton.setFocusable(false);
			positiveButton.setFocusableInTouchMode(false);
			Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
			negativeButton.setFocusable(false);
			negativeButton.setFocusableInTouchMode(false);*/
		}
	}

	@Override
	public void onPerformanceCommentAdded(String comment) {
		CrossfitPerformance performance = new CrossfitPerformance();
		performance.setTime(timeSwapBuff);
		Calendar c = Calendar.getInstance(); 
		performance.setDate(c.getTime());
		performance.setComment(comment);
		crossfitPerformanceDAO.addCrossfitPerformance(performance, crossfitWorkout.getId());

		Intent intent = new Intent(activity, CrossfitPerformanceActivity.class);
		intent.putExtra(Ids.CROSSFIT_WORKOUT_ID, crossfitWorkout.getId());
		activity.startActivity(intent);
		activity.finish();
	}

	@Override
	public void onConfirmOrCancel(boolean confirm) {
		if(confirm) {
			backPressedConfirmed();
		}
	} 


}
