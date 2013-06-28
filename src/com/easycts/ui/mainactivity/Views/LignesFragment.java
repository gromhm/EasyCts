package com.easycts.ui.mainactivity.Views;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.easycts.R;
import com.easycts.Database.LigneDBAdapter;
import com.easycts.ui.CollectionStationActivity;
import com.easycts.ui.MainActivity;

public class LignesFragment
{
	static SimpleCursorAdapter lignesAdapter;
	static LigneDBAdapter ligneDBAdapter;
	static SherlockFragment parrentfragment;
	
	public static View GetView(SherlockFragment fragment, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.activity_collection_ligne, container, false);
		parrentfragment = fragment;
		ligneDBAdapter = new LigneDBAdapter(rootView.getContext());
		ligneDBAdapter.open();
		
		lignesAdapter = new SimpleCursorAdapter(rootView.getContext(), R.layout.cursor_row, null,
				new String[] { LigneDBAdapter.LIGNE_CTSID, LigneDBAdapter.LIGNE_DIR1, LigneDBAdapter.LIGNE_DIR2 },
				new int[] {  R.id.ligne_row_id, R.id.ligne_row_direction1, R.id.ligne_row_direction2},
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
	
		ListView listView = (ListView) rootView.findViewById(R.id.listLignes);
		listView.setAdapter(lignesAdapter);
		
		lignesAdapter.setViewBinder(new ViewBinder() 
		{
			@Override
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				if (view.getId() == R.id.ligne_row_id) 
				{
				 	int getIndex = cursor.getColumnIndex(LigneDBAdapter.LIGNE_CTSID);
				    String empname = cursor.getString(getIndex);
				    TextView tv = (TextView) view;
				    tv.setTextColor(Color.WHITE);
				    tv.setText(empname);
				    
				    if(empname.equals("A"))
				    	SetTramTxtView(tv,Color.rgb(226, 0, 26));
				    else if(empname.equals("B"))
				    	SetTramTxtView(tv,Color.rgb(0, 158, 224));
				    else if(empname.equals("C"))
				    	SetTramTxtView(tv,Color.rgb(242, 148, 0));
				    else if(empname.equals("D"))
				    	SetTramTxtView(tv,Color.rgb(0, 153, 51));
				    else if(empname.equals("E"))
				    	SetTramTxtView(tv,Color.rgb(144, 133, 186));
				    else if(empname.equals("F"))
				    	SetTramTxtView(tv,Color.rgb(177, 200, 0));
				    else
				    	 tv.setTextColor(Color.BLACK);
				    return true;
				} 
				return false;          
			}
		});
		
		lignesAdapter.setFilterQueryProvider(new FilterQueryProvider() {
			@Override
			public Cursor runQuery(CharSequence constraint) 
			{
				return ligneDBAdapter.getAllLigne(constraint.toString());
			}
		});
		
		SetLignesFragmentFilter(0);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				Intent intent = new Intent(view.getContext(), CollectionStationActivity.class);
				intent.putExtra(MainActivity.LIGNEID, id);
				parrentfragment.startActivity(intent);
			}
		});
		
		return rootView;
	}
	
	public static void SetLignesFragmentFilter(int type)
	{
		Log.d("StationTask", "SetLignesFragmentFilter:"+(lignesAdapter!=null));
		if(lignesAdapter!=null)
			lignesAdapter.getFilter().filter(String.valueOf(type));
	}
	
	private static void SetTramTxtView(TextView tv, int color)
	{
		   tv.setTextColor(color);
		   tv.setTextSize(20);
	}
}

