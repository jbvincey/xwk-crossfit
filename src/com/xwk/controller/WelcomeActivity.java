package com.xwk.controller;

import com.jbvincey.xwk.R;
import com.xwk.database.CrossfitWorkoutDAO;
import com.xwk.database.ExerciseDAO;
import com.xwk.model.CrossfitWorkout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class WelcomeActivity extends Activity {

	private Handler handler = new Handler();
	private final int welcomeScreenDisplay = 1500;
	private final int noDelay = 1000;
	private Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_activity);
		activity = this;

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(isFirstLaunch()) {
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					CrossfitWorkoutDAO crossfitWorkoutDAO = CrossfitWorkoutDAO.getInstance(activity);
					crossfitWorkoutDAO.open();

					ExerciseDAO exerciseDAO = ExerciseDAO.getInstance(activity);
					exerciseDAO.open();

					CrossfitWorkout.loadDefaultWorkouts(crossfitWorkoutDAO, exerciseDAO, activity);
					
					Intent i = new Intent(getBaseContext(), CrossfitWorkoutList.class);
					startActivity(i);
					finish();
				}
			}, noDelay);
			
		} 
		else {
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					Intent i = new Intent(getBaseContext(), CrossfitWorkoutList.class);
					startActivity(i);
					finish();
				}
			}, welcomeScreenDisplay);
		}
	}

	private boolean isFirstLaunch() {
		SharedPreferences firstLaunch = getSharedPreferences(CrossfitWorkoutList.MyPref, MODE_PRIVATE);
		boolean isFirstLaunch = firstLaunch.getBoolean(CrossfitWorkoutList.Pref_first_launch, true);
		return isFirstLaunch;
	}

}
