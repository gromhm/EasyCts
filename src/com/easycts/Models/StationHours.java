package com.easycts.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class StationHours implements Parcelable {

	private final String item1;
	private final String item2;
	private final String item3;

	public StationHours(String item1, String item2, String item3) {
		this.item1 = item1;
		this.item2 = item2;
		this.item3 = item3;
	}

	public final String getItem1() {
		return item1;
	}

	public final String getItem2() {
		return item2;
	}

	public final String getItem3() {
		return item3;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(item1);
		dest.writeString(item2);
		dest.writeString(item3);
	}
	
	private StationHours(Parcel in) {
		this.item1 = in.readString();
		this.item2 = in.readString();
		this.item3 = in.readString();
    }
	
	 public static final Parcelable.Creator<StationHours> CREATOR = new Parcelable.Creator<StationHours>() 
	 {
		 public StationHours[] newArray(int size) {
		     return new StationHours[size];
		 }
	
		@Override
		public StationHours createFromParcel(Parcel in) 
		{
			// TODO Auto-generated method stub
			return new StationHours(in);
		}
	};
}
