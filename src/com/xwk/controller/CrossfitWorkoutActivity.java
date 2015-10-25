package com.xwk.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jbvincey.xwk.R;
import com.xwk.database.CrossfitPerformanceDAO;
import com.xwk.database.CrossfitWorkoutDAO;
import com.xwk.database.ExerciseDAO;
import com.xwk.fragments.DialogActivity;
import com.xwk.fragments.ExerciseDetailsFragment;
import com.xwk.misc.ExerciseRawAdapter;
import com.xwk.model.CrossfitWorkout;
import com.xwk.model.Exercise;
import com.xwk.model.Ids;

public class CrossfitWorkoutActivity extends DialogActivity implements OnClickListener {
	
	private CrossfitWorkoutDAO crossfitWorkoutDAO;
	private CrossfitPerformanceDAO crossfitPerformanceDAO;
	private ExerciseDAO exerciseDAO;
	
	private ListView exerciseListView;
	private TextView crossfitWorkoutDescription;
	private Button crossfitWorkoutGoTraining;
	
	private CrossfitWorkout crossfitWorkout;
	private Activity activity;
	
	private MenuItem performanceButton;
	//private Menu menu;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.crossfitworkout_activity);
        
        crossfitWorkoutDAO = CrossfitWorkoutDAO.getInstance(this);
        crossfitWorkoutDAO.open();
        
        crossfitPerformanceDAO = CrossfitPerformanceDAO.getInstance(this);
        crossfitPerformanceDAO.open();
        
        exerciseDAO = ExerciseDAO.getInstance(this);
        exerciseDAO.open();
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        exerciseListView = (ListView) findViewById(R.id.crossfitworkout_activity_exercise_listview);
        exerciseListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        
        crossfitWorkoutDescription = (TextView) findViewById(R.id.crossfitworkout_activity_description);
        
        crossfitWorkoutGoTraining = (Button) findViewById(R.id.crossfitworkout_activity_gotraining);
        crossfitWorkoutGoTraining.setOnClickListener(this);
        
        crossfitWorkout = CrossfitWorkout.recoverCrossfitWorkout(getIntent().getExtras(), crossfitWorkoutDAO);      
        
        activity = this;
        
		setTitle("  " + crossfitWorkout.getName());
        
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
        crossfitWorkout = CrossfitWorkout.recoverCrossfitWorkout(getIntent().getExtras(), crossfitWorkoutDAO);      
        setupView();

        /*if(menu!=null) {
            crossfitWorkout = crossfitWorkoutDAO.getCrossfitWorkout(crossfitWorkout.getId());
        	performanceButton = menu.findItem(R.id.see_performance);
            if(crossfitWorkout.getPerformances().size()==0) {
            	performanceButton.setVisible(false);
            }
        }*/
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	crossfitWorkoutDAO.close();
        crossfitPerformanceDAO.close();
        exerciseDAO.close();
    }
    
    private void setupView() {
    	invalidateOptionsMenu();
    	crossfitWorkoutDescription.setText(crossfitWorkout.getDescription() + "\n\nSets: " + crossfitWorkout.getSets());
    	
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
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	getMenuInflater().inflate(R.menu.crossfitworkout_activity, menu);
        //this.menu = menu;
    	performanceButton = menu.findItem(R.id.see_performance);
        if(crossfitWorkout.getPerformances().size()==0) {
        	performanceButton.setVisible(false);
        }
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
		} else if(id == R.id.see_performance) {
			Intent intent = new Intent(activity, CrossfitPerformanceActivity.class);
			intent.putExtra(Ids.CROSSFIT_WORKOUT_ID, crossfitWorkout.getId());
			activity.startActivity(intent);
		} else if(id == R.id.edit_workout) {
			Intent intent = new Intent(this, AddCrossfitWorkout.class);
			intent.putExtra(Ids.CROSSFIT_WORKOUT_ID, crossfitWorkout.getId());
			intent.putExtra(Ids.FORM_MODE_MODIFY, true);
			startActivity(intent);
		}
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		if(viewId == R.id.crossfitworkout_activity_gotraining && crossfitWorkout.getExercises().size() > 0) {
			Intent intent = new Intent(this, CrossfitWorkoutTraining.class);
			intent.putExtra(Ids.CROSSFIT_WORKOUT_ID, crossfitWorkout.getId());
			startActivity(intent);
		}
	}

}
