package com.easycts.Ui.Mainactivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.easycts.Database.LigneDBAdapter;
import com.easycts.Database.StationDBAdapter;
import com.easycts.Models.InfosTrafic;
import com.easycts.Models.Ligne;
import com.easycts.Models.Station;
import com.easycts.Network.soapHelper;
import com.easycts.Network.soapInfosTraficHelper;
import com.easycts.R;
import com.easycts.Task.GenericSoapTask;
import com.easycts.Task.StationsFavTask;
import com.easycts.Ui.PagerStationActivity;

import java.util.ArrayList;


public class DeviationsFragment extends SherlockFragment implements StationsFavTask.StationsFavTaskFinishedListener, GenericSoapTask.StationTaskFinishedListener<ArrayList<InfosTrafic>>
{
    public final static String STATION = "com.easycts.Ui.intent.STATION";

    FragmentActivity mContext;
    ListView listView;
    ProgressBar progressBar;
    SimpleCursorAdapter lignesAdapter;
    private Cursor curs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.activity_collection_station, container, false);
        mContext = this.getSherlockActivity();
        listView = (ListView) rootView.findViewById(R.id.listStations);
        progressBar = (ProgressBar) rootView.findViewById(R.id.stationCollectionLoader);
        listView.setOnItemClickListener(setOnItemClickListener);
        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        new StationsFavTask(this).execute();

        soapHelper<ArrayList<InfosTrafic>> soapHelper = new soapInfosTraficHelper(getString(R.string.cts_soap_password));
        new GenericSoapTask<ArrayList<InfosTrafic>>(DeviationsFragment.this, soapHelper).execute();

        return rootView;
    }

    private OnItemClickListener setOnItemClickListener = new OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id)
        {
            Intent intent = new Intent(mContext, PagerStationActivity.class);
            Ligne ligne = Ligne.FromCursorWithSpecificId(curs, "ligne_id");
            Station station = Station.FromCursor(curs);
            intent.putExtra(CollectionLignesFragment.LIGNE, ligne);
            intent.putExtra(STATION, station);
            mContext.startActivity(intent);
        }
    };

    @Override
    public void onTaskFinished(Cursor results) {
        curs = results;
        lignesAdapter = new SimpleCursorAdapter(mContext,
                R.layout.fav_row, results, new String[] {
                LigneDBAdapter.LIGNE_CTSID, StationDBAdapter.ARRET_TITLE,
                LigneDBAdapter.LIGNE_DIR1 }, new int[] {
                R.id.fav_row_ligne, R.id.fav_row_station},
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        listView.setAdapter(lignesAdapter);

        lignesAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor,
                                        int columnIndex) {
                if (view.getId() == R.id.fav_row_ligne) {
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

        listView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void SetTramTxtView(TextView tv, int color) {
        tv.setTextColor(color);
        tv.setTextSize(20);
    }
}