package com.easycts.Ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FilterQueryProvider;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.easycts.Database.StationDBAdapter;
import com.easycts.Models.Ligne;
import com.easycts.Models.Station;
import com.easycts.R;
import com.easycts.Ui.Adapter.CollectionStationResourceCursorAdapter;
import com.easycts.Ui.Mainactivity.CollectionLignesFragment;

import java.util.ArrayList;

public class CollectionStationActivity extends SherlockActivity implements OnItemClickListener{
	StationDBAdapter stationDBAdapter;
	CollectionStationResourceCursorAdapter adapter;
	ListView listView;
	Context mContext;
	
	public final static String STATIONID = "com.easycts.Ui.intent.STATIONID";
	public static final String POSITIONID = "com.easycts.Ui.intent.POSITIONID";
	public static final String STATION = "com.easycts.Ui.intent.STATION";
	public static final String FAVORITES = "com.easycts.Ui.SP.FAVORITES";
	public Ligne ligne;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_collection_station);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Intent i = getIntent();
		ligne = (Ligne)i.getParcelableExtra(CollectionLignesFragment.LIGNE);
		stationDBAdapter = new StationDBAdapter(this);
		stationDBAdapter.open();

		adapter = new CollectionStationResourceCursorAdapter(this, null);

		adapter.setFilterQueryProvider(new FilterQueryProvider() {
			public Cursor runQuery(CharSequence constraint) {
				return stationDBAdapter.getAllLigne(constraint.toString());
			}});

		adapter.getFilter().filter(String.valueOf(ligne.getId()));
		
		listView = (ListView) findViewById(R.id.listStations);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// NavUtils.navigateUpFromSameTask(this);
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) 
	{
		Intent intent = new Intent(CollectionStationActivity.this, PagerStationActivity.class);
		intent.putExtra(CollectionLignesFragment.LIGNE, ligne);
        Cursor cursor = adapter.getCursor();
		intent.putExtra(STATION, Station.FromCursor(cursor));
		startActivity(intent);
	}


	
}
