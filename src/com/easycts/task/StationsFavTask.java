package com.easycts.Task;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.actionbarsherlock.app.SherlockFragment;
import com.easycts.Database.FavAdapter;
import com.easycts.Database.StationDBAdapter;
import com.easycts.Models.Fav;

import java.util.ArrayList;

public class StationsFavTask extends AsyncTask<Object, Integer, Cursor>
{
    StationsFavTaskFinishedListener stationsTaskFinishedListener;

	StationDBAdapter stationDBAdapter;
	Context mContext;
	public Boolean Connected = false;

	public interface StationsFavTaskFinishedListener
	{
		void onTaskFinished(Cursor result);
	}

	public StationsFavTask(StationsFavTaskFinishedListener context)
	{
		stationsTaskFinishedListener = context;
		mContext = ((SherlockFragment)context).getActivity();
	}

	@Override
	protected Cursor doInBackground(Object... params)
	{
        //Favs
        FavAdapter favAdapter = new FavAdapter(mContext);
        favAdapter.open();
        ArrayList<Fav> favs = favAdapter.getAll();
        favAdapter.close();

        if(favs.isEmpty())
            return null;

        //Get the Cursor results
        stationDBAdapter = new StationDBAdapter(mContext);
        stationDBAdapter.open();
        Cursor curs = stationDBAdapter.getLignesAndStationsFavs(favs);

        return curs;
	}
	
	
	@Override
	protected void onProgressUpdate(Integer... values) 
	{
		super.onProgressUpdate(values);
	}
	
	@Override
	protected void onPostExecute(Cursor results)
	{
		super.onPostExecute(results);
		stationsTaskFinishedListener.onTaskFinished(results);
	}
}
