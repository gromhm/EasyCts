package com.easycts.ui;

import java.util.ArrayList;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.easycts.Models.Ligne;
import com.easycts.Models.Station;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
	Integer cursorLength;
	ArrayList<Station> stations;
	ViewPager mViewPager;
	int ligneType;
	Ligne ligne;
	
	public ViewPagerAdapter(FragmentManager fm, ArrayList<Station> stations, Ligne ligne, ViewPager pager) {
		super(fm);
		this.stations = stations;
		this.cursorLength = stations.size();
		this.mViewPager = pager;
		this.ligne = ligne;
	}

	@Override
	public Fragment getItem(int pos) {

			Station sta = stations.get(pos);
			Log.d("StationTask", "ViewPagerAdapter-getItem:" + sta.getCtsId() + ":" + sta.getId());
			StationFragment fragment = StationFragment.newInstance(sta, ligne);
			return fragment;

	}

	@Override
	public int getCount() {
		return cursorLength;
	}

	
}