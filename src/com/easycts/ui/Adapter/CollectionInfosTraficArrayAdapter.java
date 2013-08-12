package com.easycts.Ui.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.easycts.Models.Deviation;
import com.easycts.Models.InfosTrafic;
import com.easycts.R;

import java.util.ArrayList;
import java.util.Date;

public class CollectionInfosTraficArrayAdapter extends ArrayAdapter
{
    private LayoutInflater mInflater;
    private ArrayList<InfosTrafic> infosArray;
    private Context mContext;

    public CollectionInfosTraficArrayAdapter(Context context, ArrayList<InfosTrafic> infos)
    {
        super(context, R.layout.deviation_row, infos);
        this.infosArray = infos;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View aView, ViewGroup parent) {
        ViewHolder holder;
        if (aView == null)
        {
            aView = mInflater.inflate(R.layout.deviation_row, null);
        }

        InfosTrafic info = infosArray.get(position);
        ((TextView)aView.findViewById(R.id.infos_trafic_row_title)).setText(info.getTitre());
        ((TextView)aView.findViewById(R.id.infos_trafic_row_date)).setText(info.getDebut() + " - " + info.getFin());

        return aView;
    }

    static class ViewHolder {
        TextView tw;
        CheckBox cb;
    }


}