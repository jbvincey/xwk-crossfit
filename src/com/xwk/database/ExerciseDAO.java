package com.xwk.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.xwk.model.Exercise;
import com.xwk.model.Exercise.ExerciseType;

public class ExerciseDAO extends DAOBase {
	private static ExerciseDAO instance = null;

	public static final String EXERCISE_ID = "id";
	public static final String EXERCISE_NAME = "name";
	public static final String EXERCISE_DESCRIPTION = "description";
	public static final String EXERCISE_TYPE = "type";
	public static final String EXERCISE_IMAGE = "image";
	private String[] allColumns = { EXERCISE_ID,
			EXERCISE_NAME,
			EXERCISE_DESCRIPTION,
			EXERCISE_TYPE,
			EXERCISE_IMAGE
	};


	public static final String EXERCISE_TABLE_NAME = "EXERCISE_TABLE";

	public static final String EXERCISE_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + EXERCISE_TABLE_NAME 
			+ " (" + EXERCISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ EXERCISE_NAME + " TEXT NOT NULL, " 
			+ EXERCISE_DESCRIPTION + " TEXT NOT NULL, "
			+ EXERCISE_TYPE + " TEXT NOT NULL, "
			+ EXERCISE_IMAGE + " BLOB" + ");";

	private ExerciseDAO(Context pContext) {
		super(pContext);
	}

	public static ExerciseDAO getInstance(Context pContext) {
		if(instance==null) {
			instance = new ExerciseDAO(pContext);
		}
		return instance;
	}

	protected Exercise cursorToExercise(Cursor cursor) {
		Exercise exercise = new Exercise();
		exercise.setId(cursor.getLong(0));
		exercise.setName(cursor.getString(1));
		exercise.setDescription(cursor.getString(2));
		exercise.setType(ExerciseType.valueOf(cursor.getString(3)));
		exercise.setImage(cursor.getBlob(4));
		return exercise;
	}
	
	public Exercise getExercise(long id) {
		Exercise exercise = null;
		Cursor cursor = mDb.query(EXERCISE_TABLE_NAME, allColumns, EXERCISE_ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null) {
	        cursor.moveToFirst();
	        exercise = cursorToExercise(cursor);
		}
		// make sure to close the cursor
		cursor.close();
		return exercise;
	}
	
	public long addExercise(Exercise exercise) {
		ContentValues value = new ContentValues();
		value.put(EXERCISE_NAME, exercise.getName());
		value.put(EXERCISE_DESCRIPTION, exercise.getDescription());
		value.put(EXERCISE_TYPE, exercise.getType().toString());
		value.put(EXERCISE_IMAGE, exercise.getImage());
		return mDb.insert(EXERCISE_TABLE_NAME, null, value);
	}
	
	public boolean deleteExercise(long id) {
		Cursor cursor = mDb.query(
				CrossfitWorkoutDAO.CROSSFIT_WORKOUT_TO_EXERCISE_NAME,
				new String[] {CrossfitWorkoutDAO.CROSSFIT_WORKOUT_TO_EXERCISE_ID,
					CrossfitWorkoutDAO.CROSSFIT_WORKOUT_REF,
					CrossfitWorkoutDAO.EXERCISE_REF, 
					CrossfitWorkoutDAO.EXERCISE_REP}, 
				CrossfitWorkoutDAO.EXERCISE_REF + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			return false;
		}
		else {
		mDb.delete(EXERCISE_TABLE_NAME, EXERCISE_ID + "=?",
			new String[] { String.valueOf(id) });
			return true;
		}
		//mDb.delete(CrossfitWorkoutDAO.CROSSFIT_WORKOUT_TO_EXERCISE_NAME, CrossfitWorkoutDAO.EXERCISE_REF + "=?",
		//		new String[] { String.valueOf(id) });
	}
	
	public void updateExercise(Exercise exercise) {
		ContentValues value = new ContentValues();
		value.put(EXERCISE_NAME, exercise.getName());
		value.put(EXERCISE_DESCRIPTION, exercise.getDescription());
		value.put(EXERCISE_TYPE, exercise.getType().toString());
		value.put(EXERCISE_IMAGE, exercise.getImage());
		mDb.update(EXERCISE_TABLE_NAME, value, EXERCISE_ID + "=?",
				new String[] { String.valueOf(exercise.getId()) });
	}
	
	public List<Exercise> getAllExercises() {
		List<Exercise> exercises = new ArrayList<Exercise>();
		String selectQuery = "SELECT  * FROM " + EXERCISE_TABLE_NAME;
	    Cursor cursor = mDb.rawQuery(selectQuery, null);
	     if(cursor.moveToFirst()) {
	    	 do {
	    		 Exercise exercise = cursorToExercise(cursor);
	    		 exercises.add(exercise);
	    	 } while (cursor.moveToNext());
	     }
	    
		cursor.close();
		return exercises;
	}
	
	public List<Exercise> getExerciseByType(ExerciseType type) {
		List<Exercise> exercises = new ArrayList<Exercise>();
		//String selectQuery = "SELECT  * FROM " + EXERCISE_TABLE_NAME + " WHERE " + EXERCISE_TYPE  + "=" +  type.toString()+ ";";
	    //Cursor cursor = mDb.rawQuery(selectQuery, null);
	    Cursor cursor = mDb.query(EXERCISE_TABLE_NAME, allColumns, EXERCISE_TYPE + "=?",
	            new String[] { type.toString() }, null, null, null, null);
	     if(cursor.moveToFirst()) {
	    	 do {
	    		 Exercise exercise = cursorToExercise(cursor);
	    		 exercises.add(exercise);
	    	 } while (cursor.moveToNext());
	     }
	    
		cursor.close();
		return exercises;
	}
	
	public int getExercisesCount() {
		String countQuery = "SELECT  * FROM " + EXERCISE_TABLE_NAME;
        Cursor cursor = mDb.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
	}
}
