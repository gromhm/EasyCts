package com.easycts.Ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.easycts.Intents;
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
import java.util.List;
import java.util.Vector;

import static com.actionbarsherlock.app.ActionBar.Tab;

public class PagerStationActivity  extends SherlockFragmentActivity implements GenericSoapTask.StationTaskFinishedListener<ArrayList<StationHours>>
{
    private static final int REFRESH = 1;

    // Declare Variables
    ActionBar mActionBar;
    ViewPager mPager;
    ProgressBar mProgressBar;
    Tab tab;

	 private PagerStationViewPagerAdapter mSectionsPagerAdapter;
     private Ligne mLigne;
     private Station mStation;
    private ArrayList<StationHours> hours;

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
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(mStation.getTitle());

        // Capture ViewPager page swipes
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Find the ViewPager Position
                 mActionBar.setSelectedNavigationItem(position);
            }
        });

        if (savedInstanceState == null) {
            //Call SoapTask
            ProcessSoapHoursTask();
        }
        else
        {
            this.hours = savedInstanceState.getParcelableArrayList(Intents.HOURS);
            initialisePaging();
        }
    }


    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs)
    {
        return super.onCreateView(name, context, attrs);
    }

    Boolean refresh;
    private void ProcessSoapHoursTask()
    {
        //Layout
        mProgressBar.setVisibility(View.VISIBLE);
        mPager.setVisibility(View.GONE);

        //Menu
        refresh = true;
        invalidateOptionsMenu();

        soapHelper<ArrayList<StationHours>> soapHelper = new soapHoursHelper(getString(R.string.cts_soap_password));
        new GenericSoapTask<ArrayList<StationHours>>(PagerStationActivity.this, soapHelper).execute(mStation.getCtsId(), mLigne.getTitle());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
            case REFRESH:
                ProcessSoapHoursTask();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Intents.HOURS, hours);
    }


    private void initialisePaging()
    {
        // Activate Navigation Mode Tabs
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mActionBar.removeAllTabs();

        List<Fragment> fragments = new Vector<Fragment>();
        StationFullFragment currentFrag = StationFullFragment.newInstance(null, getHoursByType(mLigne, hours, null), mStation, mLigne);
        StationFullFragment busFrag = StationFullFragment.newInstance("Bus", getHoursByType(mLigne, hours, "Bus"), mStation, mLigne);
        StationFullFragment tramFrag = StationFullFragment.newInstance("Tram", getHoursByType(mLigne, hours, "Tram"), mStation, mLigne);

        /*currentFrag.setRetainInstance(true);
        busFrag.setRetainInstance(true);
        tramFrag.setRetainInstance(true);*/

        fragments.add(currentFrag);
        fragments.add(busFrag);
        fragments.add(tramFrag);
        this.mSectionsPagerAdapter  = new PagerStationViewPagerAdapter(super.getSupportFragmentManager(), fragments);

        // Set the View Pager Adapter into ViewPager
        mPager.setAdapter(mSectionsPagerAdapter);

        // Capture tab button clicks
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {

            @Override
            public void onTabSelected(Tab tab, FragmentTransaction ft) {
                // Pass the position on tab click to ViewPager
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(Tab tab, FragmentTransaction ft) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTabReselected(Tab tab, FragmentTransaction ft) {
                // TODO Auto-generated method stub
            }
        };

        // Create first Tab
        tab = mActionBar.newTab().setText("Ligne " + mLigne.getTitle()).setTabListener(tabListener);
        mActionBar.addTab(tab);

        // Create second Tab
        tab = mActionBar.newTab().setText("Lignes Bus").setTabListener(tabListener);
        mActionBar.addTab(tab);

        // Create third Tab
        tab = mActionBar.newTab().setText("Lignes Tram").setTabListener(tabListener);
        mActionBar.addTab(tab);

        //Layout
        mProgressBar.setVisibility(View.GONE);
        mPager.setVisibility(View.VISIBLE);

        //Menu
        refresh = false;
        invalidateOptionsMenu();
    }

    public ArrayList<StationHours> getHoursByType(Ligne ligne, ArrayList<StationHours> hours, String type)
    {
        ArrayList<StationHours> typeHours = new ArrayList<StationHours>();
        for(StationHours hour : hours)
        {
            //Autre arrets Bus ou Tram
            if(type != null)
            {
                if (hour.getType().equals(type) && !hour.getCtsLigneId().startsWith(ligne.getTitle()))
                {
                    typeHours.add(hour);
                }
            }
            //Arret
            else
            {
                if (hour.getCtsLigneId().startsWith(ligne.getTitle()))
                {
                    typeHours.add(hour);
                }
            }
        }
        return typeHours;
    }

    @Override
    public void onTaskFinished(ArrayList<StationHours> result)
    {
        this.hours = result;
        initialisePaging();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(!refresh)
            menu.add(0, REFRESH, 0, "Refresh").setIcon(R.drawable.navigation_refresh).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }
}


