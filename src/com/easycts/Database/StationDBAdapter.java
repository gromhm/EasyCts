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

public class StationDBAdapter {

	public static final String ARRET_KEY = "_id";
	public static final String ARRET_LIGNE_KEY = "ligne_id";
	public static final String ARRET_CTSID = "ctsid";
	public static final String ARRET_TITLE = "nom";
	public static final String ARRET_TABLE_NAME = "Arret";
	
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
	public StationDBAdapter(Context ctx) {
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
	public StationDBAdapter open() throws SQLException {
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

		return this.mDb.query(ARRET_TABLE_NAME, new String[] {ARRET_KEY, ARRET_CTSID,
				ARRET_TITLE}, ARRET_LIGNE_KEY + "=" + constraint, null, null, null, null);
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

		Cursor mCursor = this.mDb.query(true, ARRET_TABLE_NAME, new String[] { ARRET_KEY, ARRET_CTSID, ARRET_KEY, ARRET_CTSID,
				ARRET_TITLE}, ARRET_KEY + "=" + rowId, null, null, null, null,
				null);
		
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getStationsById(String rowsId) {
		Cursor mCursor = this.mDb.query(true, ARRET_TABLE_NAME, new String[] { ARRET_KEY, ARRET_CTSID, ARRET_LIGNE_KEY, ARRET_CTSID,
				ARRET_TITLE}, ARRET_KEY + " IN(" + rowsId +")", null, null, null, null,
				null);
		
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

}