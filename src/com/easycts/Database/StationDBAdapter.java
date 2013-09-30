package com.easycts.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.easycts.Models.Fav;
import com.easycts.Models.Station;

import java.util.ArrayList;

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
	public  ArrayList<Station> getAllStation(String constraint)
	{

		Cursor curs = this.mDb.query(ARRET_TABLE_NAME, new String[] {ARRET_KEY, ARRET_CTSID,
				ARRET_TITLE}, ARRET_LIGNE_KEY + "=" + constraint, null, null, null, null);

        ArrayList<Station> stations = new ArrayList<Station>();

        if (curs != null)
        {
            while (curs.moveToNext())
            {
                stations.add(Station.FromCursor(curs));
            }
        }

        return stations;
    }

    public Cursor searchStation(String constraint)
    {

        /*Cursor curs = this.mDb.query(ARRET_TABLE_NAME, new String[] {ARRET_KEY, ARRET_CTSID,
                ARRET_TITLE}, ARRET_TITLE + " like '%" + constraint +"%'", null, null, null, null);*/

        String query = String.format("SELECT a.*,%s,%s,%s,%s FROM %s a INNER JOIN %s l ON a.%s = l.%s where a.%s like '%%%s%%'",
                LigneDBAdapter.LIGNE_CTSID,
                LigneDBAdapter.LIGNE_DIR1,
                LigneDBAdapter.LIGNE_DIR2,
                LigneDBAdapter.LIGNE_TYPE,
                ARRET_TABLE_NAME,
                LigneDBAdapter.LIGNE_TABLE_NAME,
                ARRET_LIGNE_KEY,
                LigneDBAdapter.LIGNE_KEY,
                ARRET_TITLE,
                constraint);
        Cursor curs = this.mDb.rawQuery(query, new String[]{});
        return curs;
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

	public Cursor getLignesAndStationsFavs(ArrayList<Fav> favs)
	{
        StringBuilder queryBuilder = new StringBuilder();
        for(int i=0;i<favs.size();i++)
        {
            Fav fav = favs.get(i);
            if(i>0)
            {
                queryBuilder.append(" UNION ");
            }
            queryBuilder.append(String.format("SELECT a.*,%s,%s,%s,%s FROM %s a INNER JOIN %s l ON a.%s = l.%s where l.%s = '%s' AND a.%s = '%s'",
                    LigneDBAdapter.LIGNE_CTSID,
                    LigneDBAdapter.LIGNE_DIR1,
                    LigneDBAdapter.LIGNE_DIR2,
                    LigneDBAdapter.LIGNE_TYPE,
                    ARRET_TABLE_NAME,
                    LigneDBAdapter.LIGNE_TABLE_NAME,
                    ARRET_LIGNE_KEY,
                    LigneDBAdapter.LIGNE_KEY,
                    LigneDBAdapter.LIGNE_CTSID,
                    fav.getLigneCtsKey(),
                    ARRET_CTSID,
                    fav.getArretCtsKey()));
        }

		Cursor mCursor = this.mDb.rawQuery(queryBuilder.toString(), new String[]{});
		
		return mCursor;

	}

}