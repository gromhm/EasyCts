package com.easycts.Database;

import java.util.ArrayList;

import com.easycts.Models.Ligne;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class LigneDBAdapter {

	public static final String LIGNE_KEY = "_id";
	public static final String LIGNE_CTSID = "title";
	public static final String LIGNE_TYPE = "type";
	public static final String LIGNE_DIR1 = "labeldir1";
	public static final String LIGNE_DIR2 = "labeldir2";
	public static final String LIGNE_TABLE_NAME = "Ligne";
	
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private final Context mCtx;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) 
		{
			super(context, DBAdapter.DATABASE_PATH + DBAdapter.DATABASE_NAME, null, DBAdapter.DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param ctx
	 *            the Context within which to work
	 */
	public LigneDBAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	/**
	 * Open the cars database. If it cannot be opened, try to create a new
	 * instance of the database. If it cannot be created, throw an exception to
	 * signal the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public LigneDBAdapter open() throws SQLException {
		this.mDbHelper = new DatabaseHelper(this.mCtx);
		this.mDb = this.mDbHelper.getWritableDatabase();
		return this;
	}

	/**
	 * close return type: void
	 */
	public void close() {
		this.mDbHelper.close();
	}
	
	/**
	 * Return a Cursor over the list of all cars in the database
	 * 
	 * @return Cursor over all cars
	 */
	public Cursor getAllLigne(String constraint) 
	{
		String where = null;
		
		//bus
		if(constraint.equals("1"))
			where = LIGNE_TYPE + "=1";
		
		//tram
		else if(constraint.equals("2"))
			where = LIGNE_TYPE + "=0";
		
		return this.mDb.query(LIGNE_TABLE_NAME, new String[] {LIGNE_KEY, LIGNE_CTSID,
				LIGNE_DIR1, LIGNE_DIR2, LIGNE_TYPE }, where, null, null, null, LIGNE_CTSID + " ASC");
	}

	/**
	 * Return a Cursor positioned at the car that matches the given rowId
	 * 
	 * @param rowId
	 * @return Cursor positioned to matching car, if found
	 * @throws SQLException
	 *             if car could not be found/retrieved
	 */
	public Cursor getLigne(long rowId) throws SQLException {

		Cursor mCursor =

		this.mDb.query(true, LIGNE_TABLE_NAME, new String[] { LIGNE_KEY, LIGNE_CTSID,
				LIGNE_DIR1, LIGNE_DIR2, LIGNE_TYPE }, LIGNE_KEY + "=" + rowId, null, null, null, null,
				null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

}