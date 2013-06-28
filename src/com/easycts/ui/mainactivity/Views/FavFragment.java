package com.easycts.ui.mainactivity.Views;

import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.easycts.R;
import com.easycts.Utils;
import com.easycts.Database.LigneDBAdapter;
import com.easycts.Database.StationDBAdapter;
import com.easycts.ui.CollectionStationActivity;
import com.easycts.ui.MainActivity;
import com.easycts.ui.PagerStationActivity;

public class FavFragment
{
	static SimpleCursorAdapter lignesAdapter;
	static SherlockFragment parrentfragment;
	
	static StationDBAdapter stationDBAdapter;
	static Cursor curs;
	
	public static View GetView(SherlockFragment fragment, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.activity_collection_station, container, false);
		parrentfragment = fragment;
		
		stationDBAdapter = new StationDBAdapter(fragment.getActivity());
		stationDBAdapter.open();
		curs = stationDBAdapter.getStationsById(TextUtils.join(",", Utils.loadArray(fragment.getActivity())));
		
		lignesAdapter = new SimpleCursorAdapter(rootView.getContext(), android.R.layout.simple_list_item_1, curs,
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
				Intent intent = new Intent(parrentfragment.getActivity(), PagerStationActivity.class);
				intent.putExtra(CollectionStationActivity.STATIONID, id);
				intent.putExtra(CollectionStationActivity.POSITIONID, position);
				curs.moveToPosition(position);
				intent.putExtra(MainActivity.LIGNEID, curs.getLong(curs.getColumnIndex(StationDBAdapter.ARRET_LIGNE_KEY)));
				parrentfragment.getActivity().startActivity(intent);
			}
		});
		
		return rootView;
	}
}

