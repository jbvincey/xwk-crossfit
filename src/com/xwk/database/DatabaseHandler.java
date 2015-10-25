package com.xwk.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	//Turn on Foreign Key for SQLite
	private static final String FKON = "PRAGMA foreign_keys = ON;";
	
	public DatabaseHandler(Context context, String name, CursorFactory
			factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(FKON);
		db.execSQL(ExerciseDAO.EXERCISE_TABLE_CREATE);
		db.execSQL(CrossfitPerformanceDAO.CROSSFIT_PERFORMANCE_TABLE_CREATE);
		db.execSQL(CrossfitWorkoutDAO.CROSSFIT_WORKOUT_TABLE_CREATE);
		db.execSQL(CrossfitWorkoutDAO.CROSSFIT_WORKOUT_TO_EXERCISE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + ExerciseDAO.EXERCISE_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + CrossfitWorkoutDAO.CROSSFIT_WORKOUT_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + CrossfitPerformanceDAO.CROSSFIT_PERFORMANCE_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + CrossfitWorkoutDAO.CROSSFIT_WORKOUT_TO_EXERCISE_CREATE);
		onCreate(db);
	}

}
