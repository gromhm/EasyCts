package com.easycts.Ui.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


public class PagerStationViewPagerAdapter extends FragmentPagerAdapter  {
    final int PAGE_COUNT = 3;
    private List<Fragment> fragments;

    public PagerStationViewPagerAdapter(FragmentManager supportFragmentManager, List<Fragment> fragments) {
        super(supportFragmentManager);
        this.fragments = fragments;
    }


    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

	@Override
	public int getCount() {
		return PAGE_COUNT;
	}




}