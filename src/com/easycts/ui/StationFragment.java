package com.easycts.ui;


import java.util.ArrayList;
import java.util.Iterator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.easycts.R;
import com.easycts.R.layout;
import com.easycts.Models.Ligne;
import com.easycts.Models.Station;
import com.easycts.Models.StationHours;
import com.easycts.task.StationTask;
import com.easycts.task.StationTask.StationTaskFinishedListener;
import com.easycts.ui.mainactivity.CollectionLignesFragment;

public class StationFragment extends Fragment implements StationTaskFinishedListener {
	public final static String STATION = "com.easycts.ui.intent.STATION";
	public final static String DISPLAYHOURS = "com.easycts.ui.intent.DISPLAYHOURS";
	private Station currentStation;
	private LinearLayout resultsLL;
	private ProgressBar progressbar;
	private Boolean hoursDisplayed;
	private Ligne ligne;
	
	public static StationFragment newInstance(Station station, Ligne ligne) 
	{
		StationFragment fragment = new StationFragment();
		Bundle args = new Bundle();
		args.putParcelable(STATION, station);
		args.putParcelable(CollectionLignesFragment.LIGNE, ligne);
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
		ligne = getArguments().getParcelable((CollectionLignesFragment.LIGNE));
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

		((TextView) rootViewRL.findViewById(R.id.station_fragment_title)).setText(ligne.getTitle() + " - "+ currentStation.getTitle());
		
		progressbar.setVisibility(View.VISIBLE);
		ProcessHours();
		
		LogFragment("onCreateView");
		return sw;
	}
	
	public void ProcessHours()
	{
		new StationTask(StationFragment.this).execute(getActivity().getString(R.string.cts_password), 
				currentStation.getCtsId(),
				String.valueOf(currentStation.getId()), 
				String.valueOf(ligne.getType()));
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
		//ArrayList<?> stationLignes = currentStation.getStationHours(ligne.getTitle());
		ArrayList<?> stationLignes = currentStation.getAllStationHours();
		if (stationLignes.size() > 0) {
			LogFragment("onTaskFinished");
			Iterator<StationHours> it = (Iterator<StationHours>) stationLignes.iterator();
			while (it.hasNext()) 
			{
				StationHours stationLigne = it.next();

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
	}
}