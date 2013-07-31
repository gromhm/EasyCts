package com.easycts.Ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.easycts.Database.StationDBAdapter;
import com.easycts.Models.Fav;
import com.easycts.Models.Ligne;
import com.easycts.Models.Station;
import com.easycts.R;
import com.easycts.Task.FavTask;
import com.easycts.Task.StationsTask;
import com.easycts.Ui.Adapter.CollectionStationResourceCursorAdapter;
import com.easycts.Ui.Mainactivity.CollectionLignesFragment;

import java.util.ArrayList;

public class CollectionStationActivity extends SherlockActivity implements OnItemClickListener, StationsTask.StationsTaskFinishedListener {
	CollectionStationResourceCursorAdapter adapter;
	ListView listView;
    ProgressBar mProgressBar;
	Context mContext;
	public static final String STATION = "com.easycts.Ui.intent.STATION";
	public static final String FAVORITES = "com.easycts.Ui.SP.FAVORITES";
	public Ligne ligne;
    private ArrayList<Station> stations;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_collection_station);
		Intent i = getIntent();
		ligne = i.getParcelableExtra(CollectionLignesFragment.LIGNE);

        //Action Bar
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setTitle("Ligne " + ligne.getTitle());

		listView = (ListView) findViewById(R.id.listStations);
        mProgressBar = (ProgressBar) findViewById(R.id.stationCollectionLoader);
        mProgressBar.setVisibility(View.VISIBLE);
		listView.setOnItemClickListener(this);

        new StationsTask(this).execute(ligne);
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
		intent.putExtra(STATION, stations.get(position));
		startActivity(intent);
	}


    @Override
    public void onTaskFinished(ArrayList<Station> stations)
    {
        this.stations = stations;
        adapter = new CollectionStationResourceCursorAdapter(this, stations, ligne);
        listView.setAdapter(adapter);
        listView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }


    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        ArrayList<Fav> favs = adapter.getFavs();
        new FavTask(this).execute(favs, 2, ligne.getTitle());
    }
}
