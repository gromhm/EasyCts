package com.easycts.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.ResourceCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.*;
import com.easycts.R;
import com.easycts.Utils;
import com.easycts.Database.LigneDBAdapter;
import com.easycts.Database.StationDBAdapter;

public class CollectionStationActivity extends SherlockActivity implements OnItemClickListener{
	StationDBAdapter stationDBAdapter;
	MyAdapter adapter;
	ListView listView;
	Context mContext;
	
	public final static String STATIONID = "com.easycts.ui.intent.STATIONID";
	public static final String POSITIONID = "com.easycts.ui.intent.POSITIONID";
	public static final String FAVORITES = "com.easycts.ui.SP.FAVORITES";
	public long ligneId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_collection_station);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Intent i = getIntent();
		ligneId = i.getLongExtra(MainActivity.LIGNEID, 0);

		stationDBAdapter = new StationDBAdapter(this);
		stationDBAdapter.open();

		adapter = new MyAdapter(this, null);

		adapter.setFilterQueryProvider(new FilterQueryProvider() {
			public Cursor runQuery(CharSequence constraint) {
				return stationDBAdapter.getAllLigne(constraint.toString());
			}});

		adapter.getFilter().filter(String.valueOf(ligneId));
		
		listView = (ListView) findViewById(R.id.listStations);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// NavUtils.navigateUpFromSameTask(this);
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) 
	{
		Intent intent = new Intent(CollectionStationActivity.this, PagerStationActivity.class);
		intent.putExtra(STATIONID, id);
		intent.putExtra(POSITIONID, position);
		intent.putExtra(MainActivity.LIGNEID, ligneId);
		startActivity(intent);
		
	}

	private class MyAdapter extends ResourceCursorAdapter 
	{
		private LayoutInflater mInflater;
		private List<Integer> favArray;
		
        public MyAdapter(Context context, Cursor cur) 
        {
            super(context, R.layout.cursor_row, cur, true);
            favArray =  Utils.loadArray(context);
            mInflater = LayoutInflater.from(context);
        }
        
        @Override
        public View newView(Context context, Cursor cur, ViewGroup parent) 
        {
            return mInflater.inflate(R.layout.station_row, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cur) {
            TextView tvListText = (TextView)view.findViewById(R.id.station_row_text);
            CheckBox cbListCheck = (CheckBox)view.findViewById(R.id.station_row_checkbox);
            
            Long longid = cur.getLong(cur.getColumnIndex(StationDBAdapter.ARRET_KEY));
            
            tvListText.setText(cur.getString(cur.getColumnIndex(StationDBAdapter.ARRET_TITLE)));
            cbListCheck.setChecked(favArray.contains(longid.intValue())? true : false);
            cbListCheck.setTag(longid.intValue());
            
            cbListCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() 
            {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Integer intId = Integer.parseInt(buttonView.getTag().toString());
					if(isChecked)
					{
						favArray.add(intId);
					}
					else
					{
						favArray.remove(intId);
					}
					Utils.saveArray(mContext, favArray);
				}
			});	 
        }
	}
	
}
