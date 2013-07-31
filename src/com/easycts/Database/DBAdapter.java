package com.easycts.Database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {
	
	public static final String DATABASE_NAME = "points.sqlite";
    public static final String DATABASE_FAV_NAME = "favs.sqlite";
	public static final String DATABASE_PATH = "/data/data/com.easycts/files/";
    public static final int DATABASE_VERSION = 1;

}
