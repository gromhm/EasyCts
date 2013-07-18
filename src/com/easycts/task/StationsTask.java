package com.easycts.Task;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;


import com.easycts.Database.StationDBAdapter;

public class StationsTask extends AsyncTask<String, Integer, Cursor> 
{
	StationsTaskFinishedListener stationsTaskFinishedListener;

	StationDBAdapter stationDBAdapter;
	Context mContext;
	public Boolean Connected = false;
	
	public interface StationsTaskFinishedListener 
	{
		void onTaskFinished(Cursor result);
	}

	public StationsTask(StationsTaskFinishedListener context) 
	{
		stationsTaskFinishedListener = context;
		mContext = (Context)context;
	}

	@Override
	protected Cursor doInBackground(String... params)
	{
			String ligneId = params[0];
			
	        //Get the Cursor results
			stationDBAdapter = new StationDBAdapter(mContext);
		    stationDBAdapter.open();
		    
		    Cursor cursor = stationDBAdapter.getAllLigne(String.valueOf(ligneId));
		    
		    return cursor;
	}
	
	
	@Override
	protected void onProgressUpdate(Integer... values) 
	{
		super.onProgressUpdate(values);
	}
	
	@Override
	protected void onPostExecute(Cursor cursor)
	{
		super.onPostExecute(cursor);
		stationsTaskFinishedListener.onTaskFinished(cursor);
	}}
