package com.easycts.Ui.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.easycts.Database.StationDBAdapter;
import com.easycts.R;
import com.easycts.Utils;

import java.util.List;

public class CollectionStationResourceCursorAdapter extends ResourceCursorAdapter
{
    private LayoutInflater mInflater;
    private List<Integer> favArray;

    public CollectionStationResourceCursorAdapter(Context context, Cursor cur)
    {
        super(context, R.layout.cursor_row, cur, true);
        favArray =  Utils.loadArray(context);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cur, ViewGroup parent)
    {
        return mInflater.inflate(R.layout.station_row, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cur) {
        TextView tvListText = (TextView)view.findViewById(R.id.station_row_text);
        CheckBox cbListCheck = (CheckBox)view.findViewById(R.id.station_row_checkbox);

        Long longid = cur.getLong(cur.getColumnIndex(StationDBAdapter.ARRET_KEY));

        tvListText.setText(cur.getString(cur.getColumnIndex(StationDBAdapter.ARRET_TITLE)));
        cbListCheck.setChecked(favArray.contains(longid.intValue())? true : false);
        cbListCheck.setTag(longid.intValue());

        cbListCheck.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Integer intId = Integer.parseInt(buttonView.getTag().toString());
                if(isChecked)
                {
                    favArray.add(intId);
                }
                else
                {
                    favArray.remove(intId);
                }
                Utils.saveArray(mContext, favArray);
            }
        });
    }
}