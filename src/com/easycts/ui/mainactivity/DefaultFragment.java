package com.easycts.Ui.Mainactivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.easycts.R;

/**
 * Fragment that appears in the "content_frame", shows a planet
 */
public class DefaultFragment extends SherlockFragment
{
	public static final String ITEMNUMBER = "com.easycts.Ui.intent.ITEMNUMBER";
	
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
	
}