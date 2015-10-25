package com.xwk.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DAOBase {
	protected final static int VERSION = 1;
	// The name of the SQLite database file
	protected final static String NAME = "database.db";
	protected SQLiteDatabase mDb = null;
	protected DatabaseHandler mHandler = null;


	public DAOBase(Context pContext) {
		this.mHandler = new DatabaseHandler(pContext, NAME, null,
				VERSION);
	}

	public SQLiteDatabase open() {
		// No need to close the last database since getWritableDatabase does it.
		mDb = mHandler.getWritableDatabase();
		return mDb;
	}

	public void close() {
		mDb.close();
	}

	public SQLiteDatabase getDb() {
		return mDb;
	}

}

