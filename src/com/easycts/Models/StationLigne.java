package com.easycts.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class StationLigne implements Parcelable {
	private String endstation;
	private String type;
	ArrayList<String> hours;

	public StationLigne(String endstation, String type) {
		super();
		this.endstation = endstation;
		this.type = type;
		this.hours = new ArrayList<String>();
	}

	public StationLigne(Parcel in) {
		this.endstation = in.readString();
		this.type = in.readString();
		in.readStringList(this.hours);
	}

	public void setEndstation(String endstation) {
		this.endstation = endstation;
	}

	public String getEndstation() {
		return endstation;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setHours(ArrayList<String> hours) {
		this.hours = hours;
	}

	public ArrayList<String> getHours() {
		return hours;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeString(endstation);
		dest.writeString(type);
		dest.writeStringList(hours);
	}

	public static final Parcelable.Creator<StationLigne> CREATOR = new Parcelable.Creator<StationLigne>() {
		public StationLigne[] newArray(int size) {
			return new StationLigne[size];
		}

		@Override
		public StationLigne createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			return new StationLigne(in);
		}
	};
}
