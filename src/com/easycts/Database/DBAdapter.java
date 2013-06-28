package com.easycts.Database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {
	
	public static final String DATABASE_NAME = "points.sqlite";
	public static final String DATABASE_PATH = "/data/data/com.easycts/files/";
    public static final int DATABASE_VERSION = 1;
    
	public static final String LIGNE_KEY = "_id";
	public static final String LIGNE_CTSID = "title";
	public static final String LIGNE_DIR1 = "labeldir1";
	public static final String LIGNE_DIR2 = "labeldir2";
	public static final String LIGNE_TABLE_NAME = "Ligne";
	
	public static final String ARRET_KEY = "_id";
	public static final String ARRET_LIGNE_KEY = "ligne_id";
	public static final String ARRET_CTSID = "ctsid";
	public static final String ARRET_TITLE = "nom";
	public static final String ARRET_TABLE_NAME = "Arret";
	
	public static final String POSITION_KEY = "id";
	public static final String POSITION_ARRET_KEY = "arret_id";
	public static final String POSITION_DIRECTION = "direction";
	public static final String POSITION_LAT = "lat";
	public static final String POSITION_LNG = "lng";
	public static final String POSITION_TABLE_NAME = "Position";
	
	public static final String LIGNE_TABLE_CREATE = "CREATE TABLE " + LIGNE_TABLE_NAME + " (" 
			+ LIGNE_KEY + " INTEGER PRIMARY KEY , " 
			+ LIGNE_CTSID + " TEXT, "
			+ LIGNE_DIR1 + " TEXT, " 
			+ LIGNE_DIR2 + " TEXT);";
	public static final String LIGNE_TABLE_DROP = "DROP TABLE IF EXISTS " + LIGNE_TABLE_NAME + ";";
	
	public static final String ARRET_TABLE_CREATE = "CREATE TABLE " + ARRET_TABLE_NAME + " (" 
			+ ARRET_KEY + " INTEGER PRIMARY KEY , " 
			+ ARRET_LIGNE_KEY + " INTEGER, "
			+ ARRET_CTSID + " TEXT, "
			+ ARRET_TITLE + " TEXT, " 
			+"FOREIGN KEY("+ARRET_LIGNE_KEY+") REFERENCES "+LIGNE_TABLE_NAME+"("+LIGNE_KEY+"));";
	public static final String ARRET_TABLE_DROP = "DROP TABLE IF EXISTS " + ARRET_TABLE_NAME + ";";
	
	public static final String POSITION_TABLE_CREATE = "CREATE TABLE " + POSITION_TABLE_NAME + " (" 
			+ POSITION_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ POSITION_ARRET_KEY + " INTEGER, "
			+ POSITION_DIRECTION + " INTEGER, "
			+ POSITION_LAT + " TEXT, "
			+ POSITION_LNG + " TEXT, " 
			+"FOREIGN KEY("+POSITION_ARRET_KEY+") REFERENCES "+ARRET_TABLE_NAME+"("+ARRET_KEY+"));";
	public static final String POSITION_TABLE_DROP = "DROP TABLE IF EXISTS " + POSITION_TABLE_NAME + ";";

	private final Context context; 
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    
    /**
     * Constructor
     * @param ctx
     */
    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        this.DBHelper = new DatabaseHelper(this.context);
        //this.DBHelper.Delete();
        //this.DBHelper.close();
    }

    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {    		
        	db.execSQL(LIGNE_TABLE_CREATE);
    		db.execSQL(ARRET_TABLE_CREATE);
    		db.execSQL(POSITION_TABLE_CREATE);    
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
        {               
        	db.execSQL(POSITION_TABLE_DROP);
    		db.execSQL(ARRET_TABLE_DROP);
    		db.execSQL(LIGNE_TABLE_DROP);
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
        this.db = this.DBHelper.getWritableDatabase();
        return this.db;
    }
    
    /**
     * close the db 
     * return type: void
     */
    public void close() 
    {
        this.DBHelper.close();
    }
}
