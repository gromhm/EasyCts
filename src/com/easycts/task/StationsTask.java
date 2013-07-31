package com.easycts.Task;

import android.content.Context;
import android.os.AsyncTask;


import com.easycts.Database.FavAdapter;
import com.easycts.Database.StationDBAdapter;
import com.easycts.Models.Fav;
import com.easycts.Models.Ligne;
import com.easycts.Models.Station;

import java.util.ArrayList;

public class StationsTask extends AsyncTask<Object, Integer, ArrayList<Station>>
{
	StationsTaskFinishedListener stationsTaskFinishedListener;

	StationDBAdapter stationDBAdapter;
	Context mContext;
	public Boolean Connected = false;
	
	public interface StationsTaskFinishedListener 
	{
		void onTaskFinished(ArrayList<Station> result);
	}

	public StationsTask(StationsTaskFinishedListener context) 
	{
		stationsTaskFinishedListener = context;
		mContext = (Context)context;
	}

	@Override
	protected ArrayList<Station> doInBackground(Object... params)
	{
			Ligne ligne = (Ligne)params[0];
			
	        //Get the Cursor results
			stationDBAdapter = new StationDBAdapter(mContext);
		    stationDBAdapter.open();
            ArrayList<Station> stations = stationDBAdapter.getAllStation(String.valueOf(ligne.getId()));
            stationDBAdapter.close();

            //Favs
            FavAdapter favAdapter = new FavAdapter(mContext);
            favAdapter.open();
            ArrayList<Fav> favs = favAdapter.getAllByLigne(ligne);
            favAdapter.close();

            //Set fav
            if(!favs.isEmpty())
            {
                int favsize = favs.size();
                int favDone = 0;

                for(int i=0; i<stations.size(); i++)
                {
                    Station station = stations.get(i);
                    boolean isfav = Fav.FavsContains(favs, station.getCtsId()) != -1;
                    if(isfav)
                    {
                        station.setFav(true);
                        favDone++;
                        if(favDone == favsize){
                            break;
                        }
                    }
                }
            }

		    return stations;
	}
	
	
	@Override
	protected void onProgressUpdate(Integer... values) 
	{
		super.onProgressUpdate(values);
	}
	
	@Override
	protected void onPostExecute(ArrayList<Station> cursor)
	{
		super.onPostExecute(cursor);
		stationsTaskFinishedListener.onTaskFinished(cursor);
	}}
