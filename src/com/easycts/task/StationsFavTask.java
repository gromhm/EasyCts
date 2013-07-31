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

        //Get the Cursor results
        stationDBAdapter = new StationDBAdapter(mContext);
        stationDBAdapter.open();
        Cursor curs = stationDBAdapter.getLignesAndStationsFavs(favs);


        /*HashMap<Long, ArrayList<Station>> stations = new  HashMap<Long, ArrayList<Station>>();
        HashMap<Long, Ligne> lignes = new  HashMap<Long, Ligne>();

        while (curs.moveToNext())
        {
            Ligne ligne = Ligne.FromCursorWithSpecificId(curs, "ligne_id");
            if(!lignes.containsKey(ligne.getId()))
            {
                lignes.put(ligne.getId(), ligne);
                stations.put(ligne.getId(), new ArrayList<Station>());
            }

            Station station = Station.FromCursor(curs);
            station.setFav(true);

            stations.get(ligne.getId()).add(station);
        }
        stationDBAdapter.close();

        ArrayList<Pair<Ligne, Station>> pairs = new ArrayList<Pair<Ligne, Station>>();
        Iterator it = stations.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            ArrayList<Station> entryStations = (ArrayList<Station>)entry.getValue();
            Ligne entryLigne = lignes.get(entry.getKey());
            for(Station station:entryStations)
            {
                pairs.add(Pair.of(entryLigne, station));
            }
        }*/

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
	}}
