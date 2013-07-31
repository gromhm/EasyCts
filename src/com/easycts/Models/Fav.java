package com.easycts.Models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.easycts.Database.FavAdapter;

import java.util.ArrayList;

/**
 * Created by thomas on 24/07/13.
 */
public class Fav implements Parcelable
{
    long key;
    String arretCtsKey;
    String ligneCtsKey;

    private Fav()
    {
        key = 0;
    }

    public Fav(String arretCtsKey, String ligneCtsKey)
    {
        key = 0;
        this.arretCtsKey = arretCtsKey;
        this.ligneCtsKey = ligneCtsKey;
    }

    public Fav(Parcel in)
    {
        this.key = in.readLong();
        this.arretCtsKey = in.readString();
        this.ligneCtsKey = in.readString();
    }

    public long getKey() {
        return key;
    }

    private void setKey(long key) {
        this.key = key;
    }

    public String getArretCtsKey() {
        return arretCtsKey;
    }

    public void setArretCtsKey(String key) {
        this.arretCtsKey = key;
    }

    public String getLigneCtsKey() {
        return ligneCtsKey;
    }

    public void setLigneCtsKey(String key) {
        this.ligneCtsKey = key;
    }

    public static Fav fromCursor(Cursor curs)
    {
        Fav fav = new Fav();
        fav.setArretCtsKey(curs.getString(curs.getColumnIndex(FavAdapter.FAV_ARRET_CTS_KEY)));
        fav.setLigneCtsKey(curs.getString(curs.getColumnIndex(FavAdapter.FAV_LIGNE_CTS_KEY)));
        fav.setKey(curs.getLong(curs.getColumnIndex(FavAdapter.FAV_KEY)));
        return fav;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(key);
        dest.writeString(arretCtsKey);
        dest.writeString(ligneCtsKey);
    }

    public static final Parcelable.Creator<Fav> CREATOR = new Parcelable.Creator<Fav>() {
        @Override
        public Fav createFromParcel(Parcel source) {
            return new Fav(source);
        }

        @Override
        public Fav[] newArray(int size) {
            return new Fav[size];
        }
    };

    public static boolean FavsContains(ArrayList<Fav> favs, String ligneCtsId, String arretCtsId)
    {
        for (Fav fav:favs)
        {
            if(fav.getArretCtsKey().equals(arretCtsId) && fav.getLigneCtsKey().equals(ligneCtsId))
                return true;
        }

        return false;
    }

    public static int FavsContains(ArrayList<Fav> favs, String arretCtsId)
    {
        for (int i = 0; i< favs.size(); i++)
        {
            if(favs.get(i).getArretCtsKey().equals(arretCtsId))
                return i;
        }

        return -1;
    }
}
