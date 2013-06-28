package com.easycts.task;
import java.net.URI;

import org.json.JSONArray;
import org.json.JSONObject;

import com.codebutler.android_websockets.SocketIOClient;
import com.easycts.R;
import com.easycts.Database.StationDBAdapter;
import com.easycts.task.LoadingTask.LoadingTaskFinishedListener;
import com.easycts.ui.PagerStationActivity;
import com.easycts.ui.ViewPagerAdapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

public class StationsTask extends AsyncTask<String, Integer, Cursor> 
{
	StationsTaskFinishedListener stationsTaskFinishedListener;
	SocketIOClient socketIOClient;
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
