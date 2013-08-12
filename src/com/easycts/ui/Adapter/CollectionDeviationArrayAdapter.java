package com.easycts.Ui.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.easycts.Models.Fav;
import com.easycts.Models.Ligne;
import com.easycts.Models.Station;
import com.easycts.R;

import java.util.ArrayList;

public class CollectionDeviationArrayAdapter extends ArrayAdapter
{
    private static int TAGARRET = 1;

    private LayoutInflater mInflater;
    private ArrayList<Fav> favArray;
    private ArrayList<Station> stations;
    private Ligne ligne;
    private Context mContext;

    public CollectionDeviationArrayAdapter(Context context, ArrayList<Station> stations, Ligne ligne)
    {
        super(context, R.layout.station_row, stations);
        this.stations = stations;
        favArray = new ArrayList<Fav>();
        mContext = context;
        this.ligne = ligne;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View aView, ViewGroup parent) {
        ViewHolder holder;
        if (aView == null) {
            aView = mInflater.inflate(R.layout.station_row, null);
            holder = new ViewHolder();
            holder.cb=(CheckBox)aView.findViewById(R.id.station_row_checkbox);
            holder.cb.setOnCheckedChangeListener(checkListener);
            holder.tw = (TextView)aView.findViewById(R.id.station_row_text);
            aView.setTag(holder);
        }
         else {
            holder = (ViewHolder) aView.getTag();
        }


        Station station = stations.get(position);
        holder.tw.setText(station.getTitle());
        holder.cb.setTag(position);
        holder.cb.setChecked(station.isFav());

        return aView;
    }

    private OnCheckedChangeListener checkListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton checkboxView, boolean isChecked) {
            if(checkboxView.getTag() == null)
                return;

            int position = (Integer)checkboxView.getTag();
            Station station = stations.get(position);
            station.setFav(isChecked);

            Fav fav = new Fav(station.getCtsId(), ligne.getTitle());
            int index = Fav.FavsContains(favArray, fav.getArretCtsKey());

            if(index == -1 && isChecked)
            {
                favArray.add(fav);
            }
            else
            {
                if(index != -1 && !isChecked)
                    favArray.remove(index);
            }
        }
    };

    public ArrayList<Fav> getFavs()
    {
        return favArray;
    }

    static class ViewHolder {
        TextView tw;
        CheckBox cb;
    }


}