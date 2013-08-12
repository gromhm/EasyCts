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
import com.easycts.Models.InfosTrafic;
import com.easycts.Network.soapDeviationsHelper;
import com.easycts.Network.soapHelper;
import com.easycts.Network.soapInfosTraficHelper;
import com.easycts.R;
import com.easycts.Task.GenericSoapTask;
import com.easycts.Ui.Adapter.CollectionDeviationArrayAdapter;
import com.easycts.Ui.Adapter.CollectionInfosTraficArrayAdapter;
import com.easycts.Ui.DeviationActivity;
import com.easycts.Ui.InfosTraficActivity;

import java.util.ArrayList;


public class InfosTraficFragment extends SherlockFragment implements GenericSoapTask.StationTaskFinishedListener<ArrayList<InfosTrafic>>
{
    FragmentActivity mContext;
    ListView listView;
    TextView emptyTw;
    ProgressBar progressBar;
    private CollectionInfosTraficArrayAdapter mCollectionInfosTraficArrayAdapter;
    ArrayList<InfosTrafic> mInfoTraf;

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

        emptyTw.setText(getString(R.string.empty_infos_trafic));

        soapHelper<ArrayList<InfosTrafic>> soapHelper = new soapInfosTraficHelper(getString(R.string.cts_soap_password));
        new GenericSoapTask<ArrayList<InfosTrafic>>(InfosTraficFragment.this, soapHelper).execute();

        return rootView;
    }

    private OnItemClickListener setOnItemClickListener = new OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int position, long id)
        {
            InfosTrafic infoTraf = mInfoTraf.get(position);
            Intent intent = new Intent(view.getContext(), InfosTraficActivity.class);
            intent.putExtra(Intents.INFOSTRAFIC, infoTraf);
            mContext.startActivity(intent);
        }
    };

    @Override
    public void onTaskFinished(ArrayList<InfosTrafic> results)
    {
        mInfoTraf = results;
        if(!results.isEmpty())
        {
            this.mCollectionInfosTraficArrayAdapter  = new CollectionInfosTraficArrayAdapter(mContext, results);
            listView.setAdapter(this.mCollectionInfosTraficArrayAdapter);
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