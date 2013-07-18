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
import com.easycts.Ui.Adapter.NewAdapter;
import com.easycts.Utils;

import java.util.ArrayList;

public class StationFullFragment extends SherlockFragment {
    ArrayList<String> groupItem;
    ArrayList<Object> childItem;
    ArrayList<StationHours> hours;
    Station station;
    Ligne ligne;
    String type;

    @Override
    public SherlockFragmentActivity getSherlockActivity()
    {
        return super.getSherlockActivity();
    }

    @Override
    public void onCreate(android.os.Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        if((savedInstanceState != null))
        {
            hours = savedInstanceState.getParcelableArrayList(Intents.HOURS);
        }
        else
        {
            hours = args.getParcelableArrayList(Intents.HOURS);
        }
        station = (Station) ((savedInstanceState != null) ? savedInstanceState.getParcelable(Intents.STATION): args.getParcelable(Intents.STATION));
        ligne = (Ligne) ((savedInstanceState != null) ? savedInstanceState.getParcelable(Intents.LIGNE): args.getParcelable(Intents.LIGNE));

        type = (savedInstanceState != null) ? savedInstanceState.getString(Intents.FRAGMENTTYPE): args.getString(Intents.FRAGMENTTYPE);

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
        ExpandableListView expandbleLis = (ExpandableListView)view.findViewById(R.id.list_station_full);
        expandbleLis.setDividerHeight(2);
        expandbleLis.setGroupIndicator(null);
        expandbleLis.setClickable(true);

        NewAdapter mNewAdapter = new NewAdapter(groupItem, childItem);
        mNewAdapter.setInflater(inflater, this.getSherlockActivity());
        expandbleLis.setAdapter(mNewAdapter);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }

    public void setGroupData() {
        for(StationHours hour : hours)
        {
            if (hour.getType().equals(type))
            {
                groupItem.add(hour.getEndstation());
            }
        }
    }

    public void setChildGroupData() {
        for(StationHours hour : hours)
        {
            if (hour.getType().equals(type))
            {
                childItem.add(hour.getHours());
            }
        }
    }
}