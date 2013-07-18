package com.easycts.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class StationHours implements Parcelable {
	private String endstation;
	private String type;
	ArrayList<String> hours;

	public StationHours(String endstation, String type) {
		super();
		this.endstation = endstation;
		this.type = type;
		this.hours = new ArrayList<String>();
	}

	public StationHours(Parcel in) {
		this.hours = new ArrayList<String>();
		
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

    private String mCtsLigneId;
    public String getCtsLigneId()
    {
        return mCtsLigneId = (mCtsLigneId == null)? endstation.split(" ")[0] : mCtsLigneId;
    }

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

    public Boolean IsTram()
    {
        return type == "Tram";
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

	public static final Parcelable.Creator<StationHours> CREATOR = new Parcelable.Creator<StationHours>() {
		public StationHours[] newArray(int size) {
			return new StationHours[size];
		}

		@Override
		public StationHours createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			return new StationHours(in);
		}
	};
}
