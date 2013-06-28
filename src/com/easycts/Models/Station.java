package com.easycts.Models;

import java.util.ArrayList;
import java.util.List;

import com.easycts.Database.StationDBAdapter;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class Station implements Parcelable {
	// Notez que l'identifiant est un long
	private long id;
	private String title;
	private String ctsId;
	ArrayList<StationLigne> stationLignes;

	public Station(long id, String ctsId, String title) {
		super();
		this.id = id;
		this.ctsId = ctsId;
		this.title = title;
		this.stationLignes = new ArrayList<StationLigne>();
	}

	public Station(Cursor cursor) {
		super();
		this.id = cursor.getLong(cursor
				.getColumnIndex(StationDBAdapter.ARRET_KEY));
		this.ctsId = cursor.getString(cursor
				.getColumnIndex(StationDBAdapter.ARRET_CTSID));
		this.title = cursor.getString(cursor
				.getColumnIndex(StationDBAdapter.ARRET_TITLE));
		this.stationLignes = new ArrayList<StationLigne>();
	}

	public Station(Parcel in) {
		this.id = in.readLong();
		this.title = in.readString();
		this.ctsId = in.readString();
		in.readTypedList(this.stationLignes, StationLigne.CREATOR);
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

	public ArrayList<StationLigne> getStationHours() {
		return stationLignes;
	}

	public void setStationHours(ArrayList<StationLigne> stationHours) {
		this.stationLignes = stationHours;
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
		dest.writeTypedList(stationLignes);
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
