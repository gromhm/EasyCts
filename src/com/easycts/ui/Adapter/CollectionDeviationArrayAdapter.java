package com.easycts.Ui.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.easycts.Models.Deviation;
import com.easycts.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CollectionDeviationArrayAdapter extends ArrayAdapter
{
    private LayoutInflater mInflater;
    private ArrayList<Deviation> devArray;
    private Context mContext;

    public CollectionDeviationArrayAdapter(Context context, ArrayList<Deviation> deviations)
    {
        super(context, R.layout.deviation_row, deviations);
        this.devArray = deviations;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View aView, ViewGroup parent) {

        if (aView == null)
        {
            aView = mInflater.inflate(R.layout.deviation_row, null);
        }


        Deviation dev = devArray.get(position);
        ((TextView)aView.findViewById(R.id.deviation_row_title)).setText(dev.getTitre());
        ((TextView)aView.findViewById(R.id.deviation_row_category)).setText(dev.getCategorie());
        ((TextView)aView.findViewById(R.id.deviation_row_date)).setText(dev.getDebut() + " - " + dev.getFin());

        return aView;
    }

    static class ViewHolder {
        TextView tw;
        CheckBox cb;
    }


}