package com.easycts.Ui.Mainactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.easycts.Intents;
import com.easycts.Models.Deviation;
import com.easycts.Network.soapDeviationsHelper;
import com.easycts.Network.soapHelper;
import com.easycts.R;
import com.easycts.Task.GenericSoapTask;
import com.easycts.Ui.Adapter.CollectionDeviationArrayAdapter;
import com.easycts.Ui.Adapter.CollectionInfosTraficArrayAdapter;
import com.easycts.Ui.DeviationActivity;

import java.util.ArrayList;


public class DeviationsFragment extends SherlockFragment implements GenericSoapTask.StationTaskFinishedListener<ArrayList<Deviation>>
{
    FragmentActivity mContext;
    ListView listView;
    ProgressBar progressBar;
    TextView emptyTw;
    private CollectionDeviationArrayAdapter mCollectionDeviationArrayAdapter;
    ArrayList<Deviation> mDeviations;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.activity_collection_progressbar, container, false);
        mContext = this.getSherlockActivity();
        listView = (ListView) rootView.findViewById(R.id.activity_coll_collection);
        progressBar = (ProgressBar) rootView.findViewById(R.id.activity_coll_progressbar);
        emptyTw = (TextView) rootView.findViewById(R.id.activity_coll_empty_text);
        listView.setOnItemClickListener(setOnItemClickListener);
        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        emptyTw.setVisibility(View.GONE);

        emptyTw.setText(getString(R.string.empty_deviations));

        soapHelper<ArrayList<Deviation>> soapHelper = new soapDeviationsHelper(getString(R.string.cts_soap_password));
        new GenericSoapTask<ArrayList<Deviation>>(DeviationsFragment.this, soapHelper).execute();

        return rootView;
    }

    private OnItemClickListener setOnItemClickListener = new OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int position, long id)
        {
            Deviation deviation = mDeviations.get(position);
            Intent intent = new Intent(view.getContext(), DeviationActivity.class);
            intent.putExtra(Intents.DEVIATION, deviation);
            mContext.startActivity(intent);
        }
    };

    @Override
    public void onTaskFinished(ArrayList<Deviation> results)
    {
        mDeviations = results;
        if(!results.isEmpty())
        {
            this.mCollectionDeviationArrayAdapter  = new CollectionDeviationArrayAdapter(mContext, results);
            listView.setAdapter(this.mCollectionDeviationArrayAdapter);
            listView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            emptyTw.setVisibility(View.GONE);
        }
        else
        {
            listView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            emptyTw.setVisibility(View.VISIBLE);
        }


    }
}