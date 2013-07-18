package com.easycts.Ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.easycts.Models.Ligne;
import com.easycts.Models.Station;
import com.easycts.Models.StationHours;
import com.easycts.Network.soapHelper;
import com.easycts.Network.soapHoursHelper;
import com.easycts.R;
import com.easycts.Task.GenericSoapTask;
import com.easycts.Ui.Adapter.PagerStationViewPagerAdapter;
import com.easycts.Ui.Mainactivity.CollectionLignesFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PagerStationActivity  extends SherlockFragmentActivity implements GenericSoapTask.StationTaskFinishedListener<ArrayList<StationHours>>
{
    // Declare Variables
    ActionBar mActionBar;
    ViewPager mPager;
    ActionBar.Tab tab;
    ProgressBar mProgressBar;

	 private PagerStationViewPagerAdapter mSectionsPagerAdapter;
     private Ligne mLigne;
     private Station mStation;

	 	@Override
	    public void onCreate(Bundle savedInstanceState) 
	 	{
	        super.onCreate(savedInstanceState);

            //Params
            Intent i = getIntent();
            mLigne = i.getParcelableExtra(CollectionLignesFragment.LIGNE);
            mStation = i.getParcelableExtra(CollectionStationActivity.STATION);

            // Get the view from activity_main.xml
            setContentView(R.layout.activity_pager_station);

            // Locate ViewPager in activity_main.xml
            mPager = (ViewPager) findViewById(R.id.viewPager);
            mProgressBar = (ProgressBar) findViewById(R.id.viewPagerloader);

            //Back button
	        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            // Capture ViewPager page swipes
            ViewPager.SimpleOnPageChangeListener ViewPagerListener = new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    // Find the ViewPager Position
                    mActionBar.setSelectedNavigationItem(position);
                }
            };

            mPager.setOnPageChangeListener(ViewPagerListener);

            //Call SoapTask
            ProcessSoapHoursTask();
	 	}

        private void ProcessSoapHoursTask()
        {
            soapHelper<ArrayList<StationHours>> soapHelper = new soapHoursHelper(getString(R.string.cts_soap_password));
            new GenericSoapTask<ArrayList<StationHours>>(PagerStationActivity.this, soapHelper).execute(mStation.getCtsId(), mLigne.getTitle());
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
    public void onTaskFinished(ArrayList<StationHours> result)
    {
        // Activate Navigation Mode Tabs
        mActionBar = getSupportActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Activate Fragment Manager
        FragmentManager fm = getSupportFragmentManager();

        // Locate the adapter class called ViewPagerAdapter.java
        mSectionsPagerAdapter = new PagerStationViewPagerAdapter(getSupportFragmentManager(), mStation, mLigne, result);

        // Set the View Pager Adapter into ViewPager
        mPager.setAdapter(mSectionsPagerAdapter);

        // Capture tab button clicks
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {

            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                // Pass the position on tab click to ViewPager
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // TODO Auto-generated method stub
            }
        };

        // Create first Tab
        tab = mActionBar.newTab().setText("Arret").setTabListener(tabListener);
        mActionBar.addTab(tab);

        // Create second Tab
        tab = mActionBar.newTab().setText("Bus").setTabListener(tabListener);
        mActionBar.addTab(tab);

        // Create third Tab
        tab = mActionBar.newTab().setText("Tram").setTabListener(tabListener);
        mActionBar.addTab(tab);

        mProgressBar.setVisibility(View.GONE);
        mPager.setVisibility(View.VISIBLE);

        mSectionsPagerAdapter.processResult(result);
    }
}


