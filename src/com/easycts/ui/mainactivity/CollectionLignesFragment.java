package com.easycts.ui.mainactivity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.BaseColumns;
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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.easycts.R;
import com.easycts.Database.LigneDBAdapter;
import com.easycts.Models.Ligne;
import com.easycts.Models.Station;
import com.easycts.ui.CollectionStationActivity;
import com.easycts.ui.MainActivity;

public class CollectionLignesFragment extends SherlockFragment implements
		ActionBar.OnNavigationListener, SearchView.OnQueryTextListener,
		SearchView.OnSuggestionListener {
	public static final String ITEMNUMBER = "com.easycts.ui.intent.STATIONID";
	public static final String LIGNE = "com.easycts.ui.intent.LIGNE";

	
	LigneDBAdapter ligneDBAdapter;
	SimpleCursorAdapter lignesAdapter;
	SherlockFragmentActivity mContext;
	boolean mNaviFirstHit;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_collection_ligne,
				container, false);
		mContext = this.getSherlockActivity();
		this.setHasOptionsMenu(true);
		mNaviFirstHit = true;

		ActionBar ab = mContext.getSupportActionBar();
		Context context = ab.getThemedContext();
		ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(
				context, R.array.ligne_type, R.layout.sherlock_spinner_item);
		list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
		ab.setListNavigationCallbacks(list, this);

		ligneDBAdapter = new LigneDBAdapter(rootView.getContext());
		ligneDBAdapter.open();

		lignesAdapter = new SimpleCursorAdapter(rootView.getContext(),
				R.layout.cursor_row, null, new String[] {
						LigneDBAdapter.LIGNE_CTSID, LigneDBAdapter.LIGNE_DIR1,
						LigneDBAdapter.LIGNE_DIR2 }, new int[] {
						R.id.ligne_row_id, R.id.ligne_row_direction1,
						R.id.ligne_row_direction2 },
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

		ListView listView = (ListView) rootView.findViewById(R.id.listLignes);
		listView.setAdapter(lignesAdapter);

		lignesAdapter.setViewBinder(new ViewBinder() {
			@Override
			public boolean setViewValue(View view, Cursor cursor,
					int columnIndex) {
				if (view.getId() == R.id.ligne_row_id) {
					String empname = cursor.getString(cursor.getColumnIndex(LigneDBAdapter.LIGNE_CTSID));
					TextView tv = (TextView) view;
					tv.setTextColor(Color.WHITE);
					tv.setText(empname);

					if (empname.equals("A"))
						SetTramTxtView(tv, Color.rgb(226, 0, 26));
					else if (empname.equals("B"))
						SetTramTxtView(tv, Color.rgb(0, 158, 224));
					else if (empname.equals("C"))
						SetTramTxtView(tv, Color.rgb(242, 148, 0));
					else if (empname.equals("D"))
						SetTramTxtView(tv, Color.rgb(0, 153, 51));
					else if (empname.equals("E"))
						SetTramTxtView(tv, Color.rgb(144, 133, 186));
					else if (empname.equals("F"))
						SetTramTxtView(tv, Color.rgb(177, 200, 0));
					else
						tv.setTextColor(Color.BLACK);
					return true;
				}
				return false;
			}
		});

		lignesAdapter.setFilterQueryProvider(new FilterQueryProvider() {
			@Override
			public Cursor runQuery(CharSequence constraint) {
				return ligneDBAdapter.getAllLigne(constraint.toString());
			}
		});

		SetLignesFragmentFilter(0);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) 
			{
				Cursor cursor = lignesAdapter.getCursor();
				Intent intent = new Intent(view.getContext(), CollectionStationActivity.class);
				intent.putExtra(LIGNE, Ligne.FromCursor(cursor));
				mContext.startActivity(intent);
			}
		});

		return rootView;
	}

	public void SetLignesFragmentFilter(int type) {
		Log.d("StationTask", "SetLignesFragmentFilter:"
				+ (lignesAdapter != null));
		if (lignesAdapter != null)
			lignesAdapter.getFilter().filter(String.valueOf(type));
	}

	private void SetTramTxtView(TextView tv, int color) {
		tv.setTextColor(color);
		tv.setTextSize(20);
	}

	@Override
	public boolean onNavigationItemSelected(int arg0, long arg1) {
		if (mNaviFirstHit) {
			mNaviFirstHit = false;
			return true;
		}

		SetLignesFragmentFilter(arg0);
		return true;
	}

	private static final String[] COLUMNS = { BaseColumns._ID,
			SearchManager.SUGGEST_COLUMN_TEXT_1, };
	private SuggestionsAdapter mSuggestionsAdapter;

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
	{
		// super.onCreateOptionsMenu(menu, inflater);
		ActionBar ab = mContext.getSupportActionBar();
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Create the search view
		SearchView searchView = new SearchView(ab.getThemedContext());
		searchView.setQueryHint("Rechercher un arrêt");
		searchView.setOnQueryTextListener(this);
		searchView.setOnSuggestionListener(this);

		if (mSuggestionsAdapter == null) {
			MatrixCursor cursor = new MatrixCursor(COLUMNS);
			cursor.addRow(new String[] { "1", "'Murica" });
			cursor.addRow(new String[] { "2", "Canada" });
			cursor.addRow(new String[] { "3", "Denmark" });
			mSuggestionsAdapter = new SuggestionsAdapter(ab.getThemedContext(),
					cursor);
		}

		searchView.setSuggestionsAdapter(mSuggestionsAdapter);

		menu.add("Search")
				.setIcon(R.drawable.abs__ic_search)
				.setActionView(searchView)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
	}

	private class SuggestionsAdapter extends CursorAdapter {

		public SuggestionsAdapter(Context context, Cursor c) {
			super(context, c, 0);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			TextView tv = (TextView) view;
			final int textIndex = cursor
					.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1);
			tv.setText(cursor.getString(textIndex));
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(android.R.layout.simple_list_item_1,
					parent, false);
			return v;
		}
	}

	@Override
	public boolean onSuggestionClick(int position) {
		Cursor c = (Cursor) mSuggestionsAdapter.getItem(position);
		String query = c.getString(c
				.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
		Toast.makeText(this.mContext, "Suggestion clicked: " + query,
				Toast.LENGTH_LONG).show();
		return true;
	}

	@Override
	public boolean onSuggestionSelect(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextChange(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		Toast.makeText(this.mContext, "You searched for: " + query,
				Toast.LENGTH_LONG).show();
		return true;
	}
}