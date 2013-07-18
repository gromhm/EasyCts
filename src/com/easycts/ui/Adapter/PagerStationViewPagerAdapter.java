package com.easycts.Ui.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.easycts.Intents;
import com.easycts.Models.Ligne;
import com.easycts.Models.Station;
import com.easycts.Models.StationHours;
import com.easycts.Ui.StationFullFragment;
import com.easycts.Ui.StationLightFragment;
import com.easycts.Utils;

import java.util.ArrayList;


public class PagerStationViewPagerAdapter extends FragmentPagerAdapter  {
	Station station;
	ViewPager mViewPager;
	Ligne ligne;
    final int PAGE_COUNT = 3;

    StationLightFragment fragmentTab;
    StationFullFragment fragmentTabAllBus;
    StationFullFragment fragmentTabAllTram;
    ArrayList<StationHours> hours;

	public PagerStationViewPagerAdapter(FragmentManager fm, Station station, Ligne ligne, ArrayList<StationHours> hours) {
		super(fm);
		this.station = station;
        this.ligne = ligne;
        this.hours = hours;
	}

	@Override
	public Fragment getItem(int pos) {
        Fragment fragmentResult = null;
        Bundle args = new Bundle();
            switch (pos)
            {
                case 0:
                    fragmentResult = fragmentTab != null ? fragmentTab : new StationLightFragment();
                    break;
                case 1:
                    fragmentResult = fragmentTabAllBus != null ? fragmentTabAllBus : new StationFullFragment();
                    args.putString(Intents.FRAGMENTTYPE, "Bus");
                    break;
                case 2:
                    fragmentResult = fragmentTabAllTram != null ? fragmentTabAllTram : new StationFullFragment();
                    args.putString(Intents.FRAGMENTTYPE, "Tram");
                    break;
            }


            args.putParcelable(Intents.HOURS, station);
            args.putParcelable(Intents.LIGNE, ligne);
            args.putParcelableArrayList(Intents.HOURS, hours);
            fragmentResult.setArguments(args);
            return fragmentResult;
	}

	@Override
	public int getCount() {
		return PAGE_COUNT;
	}

    public void processResult(ArrayList<StationHours> result)
    {

    }
}