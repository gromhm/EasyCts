package com.easycts.Models;

import java.util.ArrayList;
import com.easycts.Database.StationDBAdapter;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class Station implements Parcelable {
	private long id;
	private String title;
	private String ctsId;
	ArrayList<StationHours> stationHours;

	public Station(long id, String ctsId, String title) {
		super();
		this.id = id;
		this.ctsId = ctsId;
		this.title = title;
		this.stationHours = new ArrayList<StationHours>();
	}
	
	public Station(Parcel in) 
	{
		this.stationHours = new ArrayList<StationHours>();
		this.id = in.readLong();
		this.ctsId = in.readString();
		this.title = in.readString();
		in.readTypedList(this.stationHours, StationHours.CREATOR);
	}

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCtsId() {
		return ctsId;
	}

	public ArrayList<StationHours> getAllStationHours() {
		return stationHours;
	}
	
	public ArrayList<StationHours> getStationHours(String ligneIdentifier) 
	{
		ArrayList<StationHours> results = new ArrayList<StationHours>();
		for(StationHours sta : stationHours)
		{
			if(sta.getEndstation().startsWith(ligneIdentifier.replaceFirst("^0+(?!$)", "")))
			{
				results.add(sta);
			}
		}
		
		return results;
	}

	public void setStationHours(ArrayList<StationHours> stationHours) {
		this.stationHours = stationHours;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeLong(id);
		dest.writeString(ctsId);
		dest.writeString(title);
		dest.writeTypedList(stationHours);
	}
	
	public static Station FromCursor(Cursor cursor)
	{
		return new Station(cursor.getLong(cursor.getColumnIndex(StationDBAdapter.ARRET_KEY)), 
    			cursor.getString(cursor.getColumnIndex(StationDBAdapter.ARRET_CTSID)),
    			cursor.getString(cursor.getColumnIndex(StationDBAdapter.ARRET_TITLE)));
	}
	
	public static final Parcelable.Creator<Station> CREATOR = new Parcelable.Creator<Station>() {
		@Override
		public Station createFromParcel(Parcel source) {
			return new Station(source);
		}

		@Override
		public Station[] newArray(int size) {
			return new Station[size];
		}
	};
	
}
