package com.easycts.ui.mainactivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.easycts.R;
import com.easycts.Database.LigneDBAdapter;
import com.easycts.ui.CollectionStationActivity;
import com.easycts.ui.MainActivity;

public class CollectionLignesFragment extends SherlockFragment implements ActionBar.OnNavigationListener
{
	public static final String ITEMNUMBER = "com.easycts.ui.intent.STATIONID";
	
	LigneDBAdapter ligneDBAdapter;
	SimpleCursorAdapter lignesAdapter;
	SherlockFragmentActivity mContext;
	boolean mNaviFirstHit;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        View rootView = inflater.inflate(R.layout.activity_collection_ligne, container, false);
        mContext = this.getSherlockActivity();
        this.setHasOptionsMenu(true);
        mNaviFirstHit = true;
        
		ActionBar ab = mContext.getSupportActionBar();
		Context context = ab.getThemedContext();
        ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(context, R.array.ligne_type, R.layout.sherlock_spinner_item);
        list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
        ab.setListNavigationCallbacks(list, this);
        
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
				mContext.startActivity(intent);
			}
		});
        
        return rootView;
    }
    
	public void SetLignesFragmentFilter(int type)
	{
		Log.d("StationTask", "SetLignesFragmentFilter:"+(lignesAdapter!=null));
		if(lignesAdapter!=null)
			lignesAdapter.getFilter().filter(String.valueOf(type));
	}
	
    private void SetTramTxtView(TextView tv, int color)
	{
		   tv.setTextColor(color);
		   tv.setTextSize(20);
	}

	@Override
	public boolean onNavigationItemSelected(int arg0, long arg1) 
	{
		if(mNaviFirstHit)
		{
			mNaviFirstHit=false;
			return true;
		}
		
		SetLignesFragmentFilter(arg0);
		return true;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		//super.onCreateOptionsMenu(menu, inflater);
		ActionBar ab = mContext.getSupportActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
	}
}