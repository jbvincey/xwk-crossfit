package com.xwk.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.xwk.model.CrossfitPerformance;
import com.xwk.model.CrossfitWorkout;
import com.xwk.model.Exercise;

public class CrossfitWorkoutDAO extends DAOBase {

	private static CrossfitWorkoutDAO instance = null;
	private CrossfitPerformanceDAO crossfitPerformanceDAO = null;
	private ExerciseDAO exerciseDAO = null;
	
	public static final String CROSSFIT_WORKOUT_ID = "id";
	public static final String CROSSFIT_WORKOUT_NAME = "name";
	public static final String CROSSFIT_WORKOUT_DESCRIPTION = "description";
	public static final String CROSSFIT_WORKOUT_TYPES = "types";
	public static final String CROSSFIT_WORKOUT_SETS = "sets";
	private String[] allColumns = { CROSSFIT_WORKOUT_ID,
			CROSSFIT_WORKOUT_NAME,
			CROSSFIT_WORKOUT_DESCRIPTION,
			CROSSFIT_WORKOUT_TYPES,
			CROSSFIT_WORKOUT_SETS
	};
	
	public static final String CROSSFIT_WORKOUT_TABLE_NAME = "CROSSFIT_WORKOUT_TABLE";

	public static final String CROSSFIT_WORKOUT_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + CROSSFIT_WORKOUT_TABLE_NAME 
			+ " (" + CROSSFIT_WORKOUT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ CROSSFIT_WORKOUT_NAME + " TEXT NOT NULL, " 
			+ CROSSFIT_WORKOUT_DESCRIPTION + " TEXT NOT NULL,"
			+ CROSSFIT_WORKOUT_TYPES + " TEXT,"
			+ CROSSFIT_WORKOUT_SETS + " INTEGER NOT NULL);";

	
	public static final String CROSSFIT_WORKOUT_TO_EXERCISE_ID = "crossfit_workout_to_exercise_id";
	public static final String CROSSFIT_WORKOUT_REF = "crossfit_workout_ref";
	public static final String EXERCISE_REF = "exercise_ref";
	public static final String CROSSFIT_WORKOUT_TO_EXERCISE_NAME = "CROSSFIT_WORKOUT_TO_EXERCISE_NAME";
	public static final String EXERCISE_REP = "exercise_rep";
	
	public static final String CROSSFIT_WORKOUT_TO_EXERCISE_CREATE = "CREATE TABLE IF NOT EXISTS " + CROSSFIT_WORKOUT_TO_EXERCISE_NAME
			+ " (" 
			+ CROSSFIT_WORKOUT_TO_EXERCISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ CROSSFIT_WORKOUT_REF + " INTEGER NOT NULL,"
			+ EXERCISE_REF + " INTEGER NOT NULL,"
			+ EXERCISE_REP + " INTEGER NOT NULL,"
			+ "FOREIGN KEY(" + CROSSFIT_WORKOUT_REF + ") REFERENCES " + CROSSFIT_WORKOUT_TABLE_NAME + "(" + CROSSFIT_WORKOUT_ID + ") ON DELETE CASCADE,"
			+ "FOREIGN KEY(" + EXERCISE_REF + ") REFERENCES " + ExerciseDAO.EXERCISE_TABLE_NAME + "(" + ExerciseDAO.EXERCISE_ID + ") ON DELETE CASCADE"
			+ ");";

	private CrossfitWorkoutDAO(Context pContext) {
		super(pContext);
		crossfitPerformanceDAO = CrossfitPerformanceDAO.getInstance(pContext);
		exerciseDAO = ExerciseDAO.getInstance(pContext);
	}
	
	public static CrossfitWorkoutDAO getInstance(Context pContext) {
		if(instance==null) {
			instance = new CrossfitWorkoutDAO(pContext);
		}
		return instance;
	}
	
	private CrossfitWorkout cursorToCrossfitWorkout(Cursor cursor) {
		CrossfitWorkout crossfitWorkout = new CrossfitWorkout();
		crossfitWorkout.setId(cursor.getLong(0));
		crossfitWorkout.setName(cursor.getString(1));
		crossfitWorkout.setDescription(cursor.getString(2));
		crossfitWorkout.setTypes(cursor.getString(3));
		crossfitWorkout.setSets(cursor.getInt(4));
		
		//retrieve all exercises for this workout
		String queryForExercises = "SELECT * FROM " + CROSSFIT_WORKOUT_TO_EXERCISE_NAME + " WHERE " + CROSSFIT_WORKOUT_REF + "=" + crossfitWorkout.getId() + ";";
		Cursor exerciseCursor = mDb.rawQuery(queryForExercises, null);
		if(exerciseCursor.moveToFirst()) {
	    	 do {
	    		 Exercise exercise = exerciseDAO.getExercise(exerciseCursor.getLong(2));
	    		 exercise.setReps(exerciseCursor.getInt(3));
	    		 crossfitWorkout.addExercise(exercise);
	    	 } while (exerciseCursor.moveToNext());
	     }
		exerciseCursor.close();
		
		//retrieve all performances for this workout
		String queryForPerformance = "SELECT * FROM " + CrossfitPerformanceDAO.CROSSFIT_PERFORMANCE_TABLE_NAME + " WHERE " + CrossfitPerformanceDAO.CROSSFIT_WORKOUT_REF + "=" + crossfitWorkout.getId() + ";";
		Cursor performanceCursor = mDb.rawQuery(queryForPerformance, null);
		if(performanceCursor.moveToFirst()) {
	    	 do {
	    		 CrossfitPerformance crossfitPerformance = crossfitPerformanceDAO.cursorToCrossfitPerformance(performanceCursor);
	    		 crossfitWorkout.addCrossfitPerformance(crossfitPerformance);
	    	 } while (performanceCursor.moveToNext());
	     }
		performanceCursor.close();
		
		return crossfitWorkout;
	}
	
	public CrossfitWorkout getCrossfitWorkout(long id) {
		CrossfitWorkout crossfitWorkout = null;
		Cursor cursor = mDb.query(CROSSFIT_WORKOUT_TABLE_NAME, allColumns, CROSSFIT_WORKOUT_ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null) {
	        cursor.moveToFirst();
	        crossfitWorkout = cursorToCrossfitWorkout(cursor);
		}
		// make sure to close the cursor
		cursor.close();
		return crossfitWorkout;
	}
	
	public long addCrossfitWorkout(CrossfitWorkout crossfitWorkout) {
		List<Exercise.ExerciseType> types = new ArrayList<Exercise.ExerciseType>();
		ContentValues value = new ContentValues();
		value.put(CROSSFIT_WORKOUT_NAME, crossfitWorkout.getName());
		value.put(CROSSFIT_WORKOUT_DESCRIPTION, crossfitWorkout.getDescription());
		value.put(CROSSFIT_WORKOUT_SETS, crossfitWorkout.getSets());

		long crossfitWorkoutId = mDb.insert(CROSSFIT_WORKOUT_TABLE_NAME, null, value);
		for(Exercise exercise: crossfitWorkout.getExercises()) {
			Exercise.ExerciseType type = exercise.getType();
			if(!types.contains(type)) {
				types.add(type);
			}
			ContentValues  exerciseValue = new ContentValues();
			exerciseValue.put(CROSSFIT_WORKOUT_REF, crossfitWorkoutId);
			exerciseValue.put(EXERCISE_REF, exercise.getId());
			exerciseValue.put(EXERCISE_REP, exercise.getReps());
			mDb.insert(CROSSFIT_WORKOUT_TO_EXERCISE_NAME, null, exerciseValue);
		}
		value.put(CROSSFIT_WORKOUT_TYPES, buildTypesString(types));
		mDb.update(CROSSFIT_WORKOUT_TABLE_NAME, value, CROSSFIT_WORKOUT_ID + "=?",
				new String[] { String.valueOf(crossfitWorkoutId) });
		return crossfitWorkoutId;
	}
	
	private String buildTypesString(List<Exercise.ExerciseType> types) {
		String typesString = "";
		for(Exercise.ExerciseType type: types) {
			typesString += type.toString() + ", ";
		}
		typesString = typesString.substring(0, typesString.length() - 2);
		return typesString;
	}
	
	public void deleteCrossfitWorkout(long id) {
		mDb.delete(CROSSFIT_WORKOUT_TABLE_NAME, CROSSFIT_WORKOUT_ID + "=?",
				new String[] { String.valueOf(id) });
		mDb.delete(CrossfitPerformanceDAO.CROSSFIT_PERFORMANCE_TABLE_NAME, CrossfitPerformanceDAO.CROSSFIT_WORKOUT_REF + "=?", 
				new String[] { String.valueOf(id) });
		mDb.delete(CROSSFIT_WORKOUT_TO_EXERCISE_NAME, CROSSFIT_WORKOUT_REF + "=?",
				new String[] { String.valueOf(id) });
	}
	
	public void updateCrossfitWorkout(CrossfitWorkout crossfitWorkout) {
		mDb.delete(CROSSFIT_WORKOUT_TO_EXERCISE_NAME, CROSSFIT_WORKOUT_REF + "=?",
				new String[] { String.valueOf(crossfitWorkout.getId()) });
		
		List<Exercise.ExerciseType> types = new ArrayList<Exercise.ExerciseType>();
		ContentValues value = new ContentValues();
		value.put(CROSSFIT_WORKOUT_NAME, crossfitWorkout.getName());
		value.put(CROSSFIT_WORKOUT_DESCRIPTION, crossfitWorkout.getDescription());
		value.put(CROSSFIT_WORKOUT_SETS, crossfitWorkout.getSets());
		
		for(Exercise exercise: crossfitWorkout.getExercises()) {
			Exercise.ExerciseType type = exercise.getType();
			if(!types.contains(type)) {
				types.add(type);
			}
			ContentValues  exerciseValue = new ContentValues();
			exerciseValue.put(CROSSFIT_WORKOUT_REF, crossfitWorkout.getId());
			exerciseValue.put(EXERCISE_REF, exercise.getId());
			exerciseValue.put(EXERCISE_REP, exercise.getReps());
			mDb.insert(CROSSFIT_WORKOUT_TO_EXERCISE_NAME, null, exerciseValue);
		}
		value.put(CROSSFIT_WORKOUT_TYPES, buildTypesString(types));

		mDb.update(CROSSFIT_WORKOUT_TABLE_NAME, value, CROSSFIT_WORKOUT_ID + "=?",
				new String[] { String.valueOf(crossfitWorkout.getId()) });
	}
	
	public List<CrossfitWorkout> getAllCrossfitWorkouts() {
		List<CrossfitWorkout> crossfitWorkouts = new ArrayList<CrossfitWorkout>();
		String selectQuery = "SELECT  * FROM " + CROSSFIT_WORKOUT_TABLE_NAME;
	    Cursor cursor = mDb.rawQuery(selectQuery, null);
	     if(cursor.moveToFirst()) {
	    	 do {
	    		 CrossfitWorkout crossfitWorkout = cursorToCrossfitWorkout(cursor);
	    		 crossfitWorkouts.add(crossfitWorkout);
	    	 } while (cursor.moveToNext());
	     }
	    
		cursor.close();
		return crossfitWorkouts;
	}
	
	public int getExercisesCount() {
		String countQuery = "SELECT  * FROM " + CROSSFIT_WORKOUT_TABLE_NAME;
        Cursor cursor = mDb.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
	}
}
