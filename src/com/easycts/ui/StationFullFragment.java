package com.easycts.Ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.easycts.Intents;
import com.easycts.Models.Ligne;
import com.easycts.Models.Station;
import com.easycts.Models.StationHours;
import com.easycts.R;
import com.easycts.Ui.Adapter.PagerStationBaseExpandableListAdapter;

import java.util.ArrayList;

public class StationFullFragment extends SherlockFragment {
    ArrayList<String> groupItem;
    ArrayList<Object> childItem;
    ArrayList<StationHours> hours;
    Station station;
    Ligne ligne;
    String type;

    public static StationFullFragment newInstance(String type, ArrayList<StationHours> hours, Station station, Ligne ligne)
    {
        Bundle args = new Bundle();
        args.putParcelableArrayList(Intents.HOURS, hours);
        args.putString(Intents.FRAGMENTTYPE, type);
        args.putParcelable(Intents.STATION, station);
        args.putParcelable(Intents.LIGNE, ligne);

        StationFullFragment f = new StationFullFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public SherlockFragmentActivity getSherlockActivity()
    {
        return super.getSherlockActivity();
    }

    @Override
    public void onCreate(android.os.Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle args = savedInstanceState==null? getArguments() : savedInstanceState;
        hours = args.getParcelableArrayList(Intents.HOURS);
        station = args.getParcelable(Intents.STATION);
        ligne = args.getParcelable(Intents.LIGNE);
        type = args.getString(Intents.FRAGMENTTYPE);

        groupItem = new ArrayList<String>();
        childItem = new ArrayList<Object>();
        setGroupData();
        setChildGroupData();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Get the view from fragmenttab2.xml
        View view = inflater.inflate(R.layout.fragment_station_full, container, false);
        final ExpandableListView expandbleLis = (ExpandableListView)view.findViewById(R.id.list_station_full);
        expandbleLis.setDividerHeight(2);
        expandbleLis.setGroupIndicator(null);
        expandbleLis.setClickable(true);

        final PagerStationBaseExpandableListAdapter mNewAdapter = new PagerStationBaseExpandableListAdapter(groupItem, childItem);
        mNewAdapter.setInflater(inflater, this.getSherlockActivity());
        expandbleLis.setAdapter(mNewAdapter);

        expandbleLis.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            public void onGroupExpand(int groupPosition) {
                int len = groupItem.size();
                for (int i = 0; i < len; i++) {
                    if (i != groupPosition) {
                        expandbleLis.collapseGroup(i);
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Intents.HOURS, hours);
        outState.putParcelable(Intents.STATION, station);
        outState.putParcelable(Intents.LIGNE, ligne);
        outState.putString(Intents.FRAGMENTTYPE, type);
        setUserVisibleHint(true);
    }

    public void setGroupData() {
        for(StationHours hour : hours)
        {
            groupItem.add(hour.getEndstation());
        }
    }

    public void setChildGroupData() {
        for(StationHours hour : hours)
        {
           childItem.add(hour.getHours());
        }
    }

}