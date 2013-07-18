package com.easycts.Ui;


import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.easycts.Models.Ligne;
import com.easycts.Models.Station;
import com.easycts.R;
import com.easycts.Ui.Mainactivity.CollectionLignesFragment;
import com.easycts.Ui.Mainactivity.FavoritesFragment;


public class FavoriteActivity extends SherlockFragmentActivity 
{
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		 
		Intent i = getIntent();
		Station station = i.getParcelableExtra(FavoritesFragment.STATION);
		Ligne ligne = i.getParcelableExtra(CollectionLignesFragment.LIGNE);
		
		setContentView(R.layout.activity_fragment_fav);

	    getSupportFragmentManager().beginTransaction().
	    add(R.id.fav_fragment_container, StationFragment.newInstance(station, ligne)).commit();
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
}
