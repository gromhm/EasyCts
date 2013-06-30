package com.easycts.ui;

import java.io.Console;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.codebutler.android_websockets.SocketIOClient;
import com.easycts.R;
import com.easycts.Models.Station;
import com.easycts.Models.StationHours;
import com.easycts.R.layout;
import com.easycts.task.StationTask;
import com.easycts.task.StationTask.StationTaskFinishedListener;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Toast;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
	Integer cursorLength;
	ArrayList<Station> stations;
	ViewPager mViewPager;

	public ViewPagerAdapter(FragmentManager fm, ArrayList<Station> stations, ViewPager pager) {
		super(fm);
		this.stations = stations;
		this.cursorLength = stations.size();
		this.mViewPager = pager;
	}

	@Override
	public Fragment getItem(int pos) {

			Station sta = stations.get(pos);
			Log.d("StationTask", "ViewPagerAdapter-getItem:" + sta.getCtsId() + ":" + sta.getId());
			DummyFragment fragment = DummyFragment.newInstance(sta);
			return fragment;

	}

	@Override
	public int getCount() {
		return cursorLength;
	}

	public static class DummyFragment extends Fragment implements StationTaskFinishedListener {
		public final static String STATION = "com.easycts.ui.intent.STATION";
		public final static String DISPLAYHOURS = "com.easycts.ui.intent.DISPLAYHOURS";
		private Station currentStation;
		private LinearLayout resultsLL;
		private ProgressBar progressbar;
		private Button button;
		private Boolean hoursDisplayed;
		
		public static DummyFragment newInstance(Station station) 
		{
			DummyFragment fragment = new DummyFragment();
			Bundle args = new Bundle();
			args.putParcelable(STATION, station);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) 
		{
			super.onCreate(savedInstanceState);
			// setRetainInstance(true);
			hoursDisplayed = (savedInstanceState != null) ? savedInstanceState.getBoolean(DISPLAYHOURS): false;
			currentStation = (Station) ((hoursDisplayed) ? savedInstanceState.getParcelable(STATION): getArguments().getParcelable(STATION));
			LogFragment("onCreate");
		}


		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			ScrollView sw = (ScrollView) inflater.inflate(layout.fragment_station, container, false);

			RelativeLayout rootViewRL = (RelativeLayout) sw
					.findViewById(R.id.drawer_station_fragment_layout);
			resultsLL = (LinearLayout) rootViewRL
					.findViewById(R.id.station_fragment_results);
			progressbar = (ProgressBar) rootViewRL
					.findViewById(R.id.station_fragment_progressbar);
			button = (Button) rootViewRL
					.findViewById(R.id.station_fragment_button);

			((TextView) rootViewRL.findViewById(R.id.station_fragment_title)).setText(currentStation.getTitle());
			
			/*if (!hoursDisplayed)
			{
				button.setOnClickListener(new View.OnClickListener() 
				{
					public void onClick(View v) {
						((Button) v).setVisibility(View.GONE);
						progressbar.setVisibility(View.VISIBLE);
						new StationTask(DummyFragment.this).execute(
								currentStation.getCtsId(),
								String.valueOf(currentStation.getId()),
								DummyFragment.this.getString(R.string.database_url));
					}
				});
			}
			else
			{
				DisplayHours();
			}*/
			
			button.setVisibility(View.GONE);
			progressbar.setVisibility(View.VISIBLE);
			new StationTask(DummyFragment.this).execute(
					currentStation.getCtsId(),
					String.valueOf(currentStation.getId()),
					DummyFragment.this.getString(R.string.database_url));
			
			// ProgressDialog.show(this.get, "loading", "message");
			LogFragment("onCreateView");
			return sw;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState)
		{
			super.onActivityCreated(savedInstanceState);
			LogFragment("onActivityCreated");
		}
		
		@Override
		public void onSaveInstanceState(Bundle outState) {
			super.onSaveInstanceState(outState);
			LogFragment("onSaveInstanceState");
			outState.putBoolean(DISPLAYHOURS, hoursDisplayed);
			if(hoursDisplayed)
				outState.putParcelable(STATION, currentStation);
		}
		
		public void LogFragment(String function) {
			String message = String.format("fragment-%s : %s - %s/%s", function, currentStation.getTitle(), currentStation.getCtsId(), currentStation.getId());
			Log.d("StationTask", message);
		}

		@Override
		public void onTaskFinished(ArrayList<StationHours> results) {
			if (!this.isAdded())
				return;
			currentStation.setStationHours(results);
			DisplayHours();
		}

		private void DisplayHours() 
		{
			hoursDisplayed = true;
			FragmentActivity ctxt = this.getActivity();
			TableLayout table;
			ArrayList<?> stationLignes = currentStation.getStationHours();
			if (stationLignes.size() > 0) {
				LogFragment("onTaskFinished");
				Iterator it = stationLignes.iterator();
				while (it.hasNext()) 
				{
					StationHours stationLigne = (StationHours)it.next();

					// New title
					TextView titleView = new TextView(ctxt);
					titleView.setTextAppearance(ctxt,
							R.style.fragmentStationTableTitle);
					titleView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
							R.drawable.section_header);
					titleView.setText(stationLigne.getEndstation());
					resultsLL.addView(titleView);

					// New table
					table = new TableLayout(ctxt);
					table.setLayoutParams(new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT, 4));
					table.setPadding(15, 15, 15, 15);
					resultsLL.addView(table);

					// First Row
					TableRow tableRow = new TableRow(ctxt);
					tableRow.setLayoutParams(new TableLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));

					TextView hoursView = new TextView(ctxt);
					hoursView.setTextAppearance(ctxt,
							R.style.fragmentStationTableFirstTw);
					hoursView.setLayoutParams(new TableRow.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT, 1.0f));
					TextView timeView = new TextView(ctxt);
					timeView.setTextAppearance(ctxt,
							R.style.fragmentStationTableFirstTw);
					timeView.setLayoutParams(new TableRow.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT, 1.0f));

					hoursView.setText("Heure de passage");
					timeView.setText("Temps restant");

					tableRow.addView(hoursView);
					tableRow.addView(timeView);
					table.addView(tableRow);

					// hours
					for (String hour : stationLigne.getHours()) 
					{
						TableRow tableR = new TableRow(ctxt);
						tableR.setLayoutParams(new TableLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT));

						TextView hoursV = new TextView(ctxt);
						hoursV.setTextAppearance(ctxt,
								R.style.fragmentStationTableSecondTw);
						hoursV.setLayoutParams(new TableRow.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.MATCH_PARENT, 1.0f));
						TextView timeV = new TextView(ctxt);
						timeV.setTextAppearance(ctxt,
								R.style.fragmentStationTableSecondTw);
						timeV.setLayoutParams(new TableRow.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.MATCH_PARENT, 1.0f));

						hoursV.setText(hour);
						timeV.setText(hour);

						tableR.addView(hoursV);
						tableR.addView(timeV);
						table.addView(tableR);
					}
				}
			}
			// No results
			else {
				// New title
				TextView titleView = new TextView(ctxt);
				titleView.setTextAppearance(ctxt,
						R.style.fragmentStationErrorTw);
				titleView
						.setLayoutParams(new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT, 4));
				titleView.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
				titleView.setText("Aucune donnée disponible pour cet arrêt.");
				resultsLL.addView(titleView);
			}
			resultsLL.setVisibility(View.VISIBLE);
			progressbar.setVisibility(View.GONE);
			button.setVisibility(View.GONE);
		}
	}
}