package com.easycts.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.easycts.R;
import com.easycts.Models.Station;

public class PagerStationActivity  extends SherlockFragmentActivity
{
	 private ViewPagerAdapter mSectionsPagerAdapter;
	 private ViewPager mViewPager;
     private Integer initialPosition;
     
	 	@Override
	    public void onCreate(Bundle savedInstanceState) 
	 	{
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_pager_station);
	        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

	        Intent i = getIntent();
	        initialPosition = i.getIntExtra(CollectionStationActivity.POSITIONID, 0);
	        ArrayList<Station> stations = i.getParcelableArrayListExtra(CollectionStationActivity.STATIONS);

	        // Set up the adapter.
	        mViewPager = (ViewPager) findViewById(R.id.viewPager);
	        mSectionsPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), stations, mViewPager);
	        mViewPager.setAdapter(mSectionsPagerAdapter);
	        mViewPager.setCurrentItem(initialPosition, false);
	        mViewPager.setOffscreenPageLimit(3);
	 	}
	 	
	 	@Override
	 	public void onStop()
	 	{
	 		super.onStop();
	 	}
	 	
	 	@Override
	 	public void onPause()
	 	{
	 		super.onPause();
	 	}
	 	
	 	@Override
		public boolean onOptionsItemSelected(MenuItem item) 
	 	{
		    switch (item.getItemId()) {
		    // Respond to the action bar's Up/Home button
		    case android.R.id.home:
		        //NavUtils.navigateUpFromSameTask(this);
		    	onBackPressed();
		        return true;
		    }
		    return super.onOptionsItemSelected(item);
		}
}


