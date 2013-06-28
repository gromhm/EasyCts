package com.easycts.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.easycts.R;
import com.easycts.task.StationsTask;
import com.easycts.task.StationsTask.StationsTaskFinishedListener;

public class PagerStationActivity  extends SherlockFragmentActivity implements StationsTaskFinishedListener
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
	        long ligneId = i.getLongExtra(MainActivity.LIGNEID, 0);
	        //long stationId = i.getLongExtra(CollectionStationActivity.STATIONID, 0);
	        initialPosition = i.getIntExtra(CollectionStationActivity.POSITIONID, 0);
						
	        new StationsTask(this).execute(String.valueOf(ligneId));
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

		@Override
		public void onTaskFinished(Cursor initialCursor)
		{ 
	        // Set up the ViewPager with the sections adapter.
	        mViewPager = (ViewPager) findViewById(R.id.viewPager);
	        // Set up the adapter.
	        mSectionsPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), initialCursor, mViewPager);
	        mViewPager.setAdapter(mSectionsPagerAdapter);
	        mViewPager.setCurrentItem(initialPosition, false);
	        mViewPager.setOffscreenPageLimit(3);
	        
		}
}


