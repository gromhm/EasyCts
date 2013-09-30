package com.easycts.Task;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.easycts.Database.FavAdapter;
import com.easycts.Database.StationDBAdapter;
import com.easycts.Models.Fav;

import java.util.ArrayList;

public class CheckStationFavTask extends AsyncTask<Object, Integer, Boolean>
{
    CheckStationFavTaskFinishedListener stationsTaskFinishedListener;

	StationDBAdapter stationDBAdapter;
	Context mContext;
	public Boolean Connected = false;

	public interface CheckStationFavTaskFinishedListener
	{
		void onTaskFinished(Boolean result);
	}

	public CheckStationFavTask(CheckStationFavTaskFinishedListener context)
	{
		stationsTaskFinishedListener = context;
		mContext = ((SherlockFragmentActivity)context);
	}

	@Override
	protected Boolean doInBackground(Object... params)
	{
        String ligneId = (String)params[0];
        String arretId = (String)params[1];

        //Favs
        FavAdapter favAdapter = new FavAdapter(mContext);
        favAdapter.open();
        Boolean favExist = favAdapter.Check(ligneId, arretId);
        favAdapter.close();

        return favExist;
	}
	
	
	@Override
	protected void onProgressUpdate(Integer... values) 
	{
		super.onProgressUpdate(values);
	}
	
	@Override
	protected void onPostExecute(Boolean result)
	{
		super.onPostExecute(result);
		stationsTaskFinishedListener.onTaskFinished(result);
	}
}
