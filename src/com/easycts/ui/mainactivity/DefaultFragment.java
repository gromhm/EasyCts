package com.easycts.ui.mainactivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.ResourceCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.easycts.R;
import com.easycts.Database.LigneDBAdapter;
import com.easycts.Models.Station;
import com.easycts.ui.CollectionStationActivity;
import com.easycts.ui.MainActivity;
import com.easycts.ui.ViewPagerAdapter.DummyFragment;
import com.easycts.ui.mainactivity.Views.FavFragment;
import com.easycts.ui.mainactivity.Views.LignesFragment;
import com.easycts.task.LignesTask;
import com.easycts.task.LignesTask.LignesTaskFinishedListener;

/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class DefaultFragment extends SherlockFragment
{
	public static final String ITEMNUMBER = "com.easycts.ui.intent.ITEMNUMBER";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		int i = getArguments().getInt(ITEMNUMBER);
		View rootView =  inflater.inflate(R.layout.fragment_planet, container, false);
		String fragment_title = getResources().getStringArray(R.array.menu_array)[i];
		
		((TextView) rootView.findViewById(R.id.text)).setText(fragment_title);

		getActivity().setTitle(fragment_title);
		return rootView;
	}
	
	public void SetLignesFragmentFilter(int type)
	{
		LignesFragment.SetLignesFragmentFilter(type);
	}
	
}