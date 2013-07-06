package com.easycts.ui.mainactivity;

import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.easycts.R;
import com.easycts.Utils;
import com.easycts.Database.StationDBAdapter;
import com.easycts.Models.Ligne;
import com.easycts.Models.Station;
import com.easycts.ui.CollectionStationActivity;
import com.easycts.ui.FavoriteActivity;
import com.easycts.ui.MainActivity;
import com.easycts.ui.PagerStationActivity;
import com.easycts.ui.StationFragment;


public class FavoritesFragment extends SherlockFragment 
{
	public final static String STATION = "com.easycts.ui.intent.STATION";
	
	FragmentActivity mContext;
	StationDBAdapter stationDBAdapter;
	SimpleCursorAdapter lignesAdapter;
	Cursor curs;
	Ligne ligne;
	Station station;
	
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	    {
		View rootView = inflater.inflate(R.layout.activity_collection_station, container, false);
		mContext = this.getActivity();
		stationDBAdapter = new StationDBAdapter(mContext);
		stationDBAdapter.open();

		curs = stationDBAdapter.getLignesAndStationsByStationId(TextUtils.join(",", Utils.loadArray(mContext)));
		
		lignesAdapter = new SimpleCursorAdapter(mContext, android.R.layout.simple_list_item_1, curs,
				new String[] { StationDBAdapter.ARRET_TITLE },
				new int[] { android.R.id.text1},
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		
		ListView listView = (ListView) rootView.findViewById(R.id.listStations);
		listView.setAdapter(lignesAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) 
			{
				Intent intent = new Intent(mContext, FavoriteActivity.class);

				Ligne ligne = Ligne.FromCursor(curs);
				Station station = Station.FromCursorWithSpecificId(curs, "id_ligne");
				intent.putExtra(CollectionLignesFragment.LIGNE, ligne);
				intent.putExtra(STATION, station);

				mContext.startActivity(intent);
				
			}
		});
		
		return rootView;
 	}
}