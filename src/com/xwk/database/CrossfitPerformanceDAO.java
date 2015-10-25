package com.xwk.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.xwk.model.CrossfitPerformance;

public class CrossfitPerformanceDAO extends DAOBase {
	private static CrossfitPerformanceDAO instance = null;
	
	public static final String CROSSFIT_PERFORMANCE_ID = "id";
	public static final String CROSSFIT_PERFORMANCE_DATE = "date";
	public static final String CROSSFIT_PERFORMANCE_TIME = "time";
	public static final String CROSSFIT_PERFORMANCE_COMMENT = "comment";
	public static final String CROSSFIT_WORKOUT_REF = "crossfit_workout_ref";
	private String[] allColumns = { CROSSFIT_PERFORMANCE_ID, 
			CROSSFIT_PERFORMANCE_DATE,
			CROSSFIT_PERFORMANCE_TIME,
			CROSSFIT_WORKOUT_REF
	};
	
	public static final String CROSSFIT_PERFORMANCE_TABLE_NAME = "CROSSFIT_PERFORMANCE_TABLE";

	public static final String CROSSFIT_PERFORMANCE_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + CROSSFIT_PERFORMANCE_TABLE_NAME 
			+ " (" + CROSSFIT_PERFORMANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ CROSSFIT_PERFORMANCE_DATE + " TEXT NOT NULL, "
			+ CROSSFIT_PERFORMANCE_TIME + " INTEGER NOT NULL,"
			+ CROSSFIT_PERFORMANCE_COMMENT + " TEXT, "
			+ CROSSFIT_WORKOUT_REF + " INTEGER NOT NULL,"
			+ "FOREIGN KEY(" + CROSSFIT_WORKOUT_REF + ") REFERENCES " + CrossfitWorkoutDAO.CROSSFIT_WORKOUT_TABLE_NAME + "(" + CrossfitWorkoutDAO.CROSSFIT_WORKOUT_ID + ") ON DELETE CASCADE"
			+ ");";

	private CrossfitPerformanceDAO(Context pContext) {
		super(pContext);
		// TODO Auto-generated constructor stub
	}
	
	public static CrossfitPerformanceDAO getInstance(Context context) {
		if(instance == null) {
			instance = new CrossfitPerformanceDAO(context);
		}
		return instance;
	}
	
	protected CrossfitPerformance cursorToCrossfitPerformance(Cursor cursor) {
		CrossfitPerformance crossfitPerformance = new CrossfitPerformance();
		crossfitPerformance.setId(cursor.getLong(0));
		crossfitPerformance.setDateString(cursor.getString(1));
		crossfitPerformance.setTime(cursor.getLong(2));
		crossfitPerformance.setComment(cursor.getString(3));
		return crossfitPerformance;
	}
	
	public CrossfitPerformance getCrossfitPerformance(long id) {
		CrossfitPerformance crossfitPerformance = null;
		Cursor cursor = mDb.query(CROSSFIT_PERFORMANCE_TABLE_NAME, allColumns, CROSSFIT_PERFORMANCE_ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null) {
	        cursor.moveToFirst();
	        crossfitPerformance = cursorToCrossfitPerformance(cursor);
		}
		// make sure to close the cursor
		cursor.close();
		return crossfitPerformance;
	}
	
	public long addCrossfitPerformance(CrossfitPerformance crossfitPerformance, long workoutId) {
		ContentValues value = new ContentValues();
		value.put(CROSSFIT_PERFORMANCE_DATE, crossfitPerformance.getDateString());
		value.put(CROSSFIT_PERFORMANCE_TIME, crossfitPerformance.getTime());
		value.put(CROSSFIT_PERFORMANCE_COMMENT, crossfitPerformance.getComment());
		value.put(CROSSFIT_WORKOUT_REF, workoutId);
		return mDb.insert(CROSSFIT_PERFORMANCE_TABLE_NAME, null, value);
	}
	
	public void deleteCrossfitPerformance(long id) {
		mDb.delete(CROSSFIT_PERFORMANCE_TABLE_NAME, CROSSFIT_PERFORMANCE_ID + "=?",
				new String[] { String.valueOf(id) });
	}
	
	public void updateCrossfitPerformance(CrossfitPerformance crossfitPerformance) {
		ContentValues value = new ContentValues();
		value.put(CROSSFIT_PERFORMANCE_DATE, crossfitPerformance.getDateString());
		value.put(CROSSFIT_PERFORMANCE_TIME, crossfitPerformance.getTime());
		value.put(CROSSFIT_PERFORMANCE_COMMENT, crossfitPerformance.getComment());
		mDb.update(CROSSFIT_PERFORMANCE_TABLE_NAME, value, CROSSFIT_PERFORMANCE_ID + "=?",
				new String[] { String.valueOf(crossfitPerformance.getId()) });
	}
	
	public List<CrossfitPerformance> getAllCrossfitPerformances() {
		List<CrossfitPerformance> crossfitPerformances = new ArrayList<CrossfitPerformance>();
		String selectQuery = "SELECT  * FROM " + CROSSFIT_PERFORMANCE_TABLE_NAME;
	    Cursor cursor = mDb.rawQuery(selectQuery, null);
	     if(cursor.moveToFirst()) {
	    	 do {
	    		 CrossfitPerformance crossfitPerformance = cursorToCrossfitPerformance(cursor);
	    		 crossfitPerformances.add(crossfitPerformance);
	    	 } while (cursor.moveToNext());
	     }
	    
		cursor.close();
		return crossfitPerformances;
	}
	
	public int getCrossfitPerformancesCount() {
		String countQuery = "SELECT  * FROM " + CROSSFIT_PERFORMANCE_TABLE_NAME;
        Cursor cursor = mDb.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
	}

	
}
