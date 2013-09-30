package com.easycts.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.easycts.Models.Fav;
import com.easycts.Models.Ligne;

import java.util.ArrayList;

/**
 * Created by thomas on 23/07/13.
 */
public class FavAdapter
{
    public static final String FAV_KEY = "id";
    public static final String FAV_ARRET_CTS_KEY = "arret_id";
    public static final String FAV_LIGNE_CTS_KEY = "ligne_id";
    public static final String FAV_TABLE_NAME = "Favoris";

    public static final String FAV_TABLE_CREATE = "CREATE TABLE " + FAV_TABLE_NAME + " ("
            + FAV_KEY + " INTEGER PRIMARY KEY , "
            + FAV_LIGNE_CTS_KEY + " TEXT, "
            + FAV_ARRET_CTS_KEY + " TEXT);";
    public static final String FAV_TABLE_DROP = "DROP TABLE IF EXISTS " + FAV_TABLE_NAME + ";";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private final Context mCtx;

    public FavAdapter(Context ctx) {
        this.mCtx = ctx;
    }


    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DBAdapter.DATABASE_PATH + DBAdapter.DATABASE_FAV_NAME, null, DBAdapter.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(FAV_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            db.execSQL(FAV_TABLE_DROP);
            onCreate(db);
        }

        public void Delete()
        {
            this.Delete();
        }
    }

    /**
     * open the db
     * @return this
     * @throws SQLException
     * return type: DBAdapter
     */
    public SQLiteDatabase open() throws SQLException
    {
        this.mDbHelper = new DatabaseHelper(this.mCtx);
        this.mDb = this.mDbHelper.getWritableDatabase();
        return this.mDb;
    }

    /**
     * close the db
     * return type: void
     */
    public void close()
    {
        this.mDbHelper.close();
    }

    public long insert(Fav fav)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(FAV_ARRET_CTS_KEY, fav.getArretCtsKey());
        initialValues.put(FAV_LIGNE_CTS_KEY, fav.getLigneCtsKey());
        return this.mDb.insert(FAV_TABLE_NAME, null, initialValues);
    }

    public ArrayList<Fav> getAll()
    {
        ArrayList<Fav> favs = new ArrayList<Fav>();
        Cursor curs = this.mDb.rawQuery("select * from " + FAV_TABLE_NAME,null);

        if (curs != null)
        {
           while (curs.moveToNext())
            {
                favs.add(Fav.fromCursor(curs));
            }
        }

        return favs;
    }

    public Boolean Check(String ligneId, String arretId)
    {
        Cursor curs = this.mDb.rawQuery("select * from " + FAV_TABLE_NAME + " Where " + FAV_LIGNE_CTS_KEY + "='" + ligneId + "' and " + FAV_ARRET_CTS_KEY  + "='" + arretId + "'",null);
        return curs.getCount()>0;
    }


    public ArrayList<Fav> getAllByLigne(Ligne ligne)
    {
        String filter= String.format("%s = '%s'",
                FAV_LIGNE_CTS_KEY,
                ligne.getTitle());

        ArrayList<Fav> favs = new ArrayList<Fav>();
        Cursor curs = this.mDb.query(FAV_TABLE_NAME, new String[] {FAV_KEY, FAV_LIGNE_CTS_KEY,
                FAV_ARRET_CTS_KEY}, filter, null, null, null, null);

        if (curs != null)
        {
            while (curs.moveToNext())
            {
                Fav fav = Fav.fromCursor(curs);
                favs.add(fav);
            }
        }

        return favs;
    }


    public void setLigneFavs(ArrayList<Fav> favs, String ligneCtsKey)
    {
        long deletedrows = this.mDb.delete(FAV_TABLE_NAME, FAV_LIGNE_CTS_KEY + "='" + ligneCtsKey+"'", null);
        Log.d("easycts", "rows deleted:"+deletedrows);

        if(!favs.isEmpty())
        {
            StringBuilder builder = new StringBuilder();
            builder.append(String.format("INSERT INTO '%s' ('%s', '%s', '%s') VALUES", FAV_TABLE_NAME , FAV_KEY, FAV_LIGNE_CTS_KEY, FAV_ARRET_CTS_KEY));

            for(int i=0; i<favs.size(); i++)
            {
                Fav fav = favs.get(i);
                if(i>0)
                {
                    builder.append(",");
                }
                builder.append(String.format(" ( NULL, '%s','%s')", ligneCtsKey, fav.getArretCtsKey()));
            }

           this.mDb.execSQL(builder.toString());
        }
    }

    public long delete(long id)
    {
        return this.mDb.delete(FAV_TABLE_NAME, FAV_KEY + "=" + id, null);
    }

    public long delete(String ligneCtsKey, String arretCtsKey)
    {
        String query= String.format("%s = '%s' AND %s = '%s'",
                FAV_LIGNE_CTS_KEY,
                ligneCtsKey,
                FAV_ARRET_CTS_KEY,
                arretCtsKey);
        return this.mDb.delete(FAV_TABLE_NAME, query, null);
    }

}