package com.easycts.Ui.Mainactivity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockFragment;
import com.easycts.Models.Deviation;
import com.easycts.Models.InfosTrafic;
import com.easycts.Network.soapDeviationsHelper;
import com.easycts.Network.soapHelper;
import com.easycts.R;
import com.easycts.Task.GenericSoapTask;
import com.easycts.Ui.Adapter.CollectionDeviationArrayAdapter;

import java.util.ArrayList;


public class InfosTraficFragment extends SherlockFragment implements GenericSoapTask.StationTaskFinishedListener<ArrayList<InfosTrafic>>
{
    FragmentActivity mContext;
    ListView listView;
    ProgressBar progressBar;
    private CollectionDeviationArrayAdapter mCollectionDeviationArrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.activity_collection_progressbar, container, false);
        mContext = this.getSherlockActivity();
        listView = (ListView) rootView.findViewById(R.id.activity_coll_collection);
        progressBar = (ProgressBar) rootView.findViewById(R.id.activity_coll_progressbar);
        listView.setOnItemClickListener(setOnItemClickListener);
        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);

        soapHelper<ArrayList<Deviation>> soapHelper = new soapDeviationsHelper(getString(R.string.cts_soap_password));
        new GenericSoapTask<ArrayList<Deviation>>(InfoTraficFragment.this, soapHelper).execute();

        return rootView;
    }

    private OnItemClickListener setOnItemClickListener = new OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id)
        {
            /*AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setAdapter(new MyAdapter(), null);
            builder.setTitle("Title");
            builder.setPositiveButton(android.R.string.ok, new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });*/

            /*Intent intent = new Intent(mContext, PagerStationActivity.class);
            Ligne ligne = Ligne.FromCursorWithSpecificId(curs, "ligne_id");
            Station station = Station.FromCursor(curs);
            intent.putExtra(CollectionLignesFragment.LIGNE, ligne);
            intent.putExtra(STATION, station);
            mContext.startActivity(intent);*/
        }
    };

    @Override
    public void onTaskFinished(ArrayList<Deviation> results)
    {
        this.mCollectionDeviationArrayAdapter  = new CollectionDeviationArrayAdapter(mContext, results);
        listView.setAdapter(this.mCollectionDeviationArrayAdapter);
        listView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

    }
}