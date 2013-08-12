package com.easycts.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class InfosTrafic implements Parcelable 
{
	private String id;
	private String titre;
	private String description;
	private String debut, fin;
	
	public InfosTrafic(String id, String titre, String description, String debut, String fin)
	{
		 this.id = id;
		 this.titre=  titre;
		 this.description = description;
		 this.debut = debut;
		 this.fin = fin;
	}
	
	public InfosTrafic(Parcel in) 
	{
		 this.id = in.readString();
		 this.titre=   in.readString();
		 this.description =  in.readString();
		 this.debut =  in.readString();
		 this.fin =  in.readString();
	}

	
	public String getId() {
		return id;
	}
	
	public String getTitre() {
		return titre;
	}
	
	public String getDescription() {
		return description;
	}
	

	public String getDebut() {
		return debut;
	}
	
	public String getFin() {
		return fin;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeString(id);
		dest.writeString(titre);
		dest.writeString(description);
		dest.writeString(debut);
		dest.writeString(fin);
	}
	
	public static final Parcelable.Creator<InfosTrafic> CREATOR = new Parcelable.Creator<InfosTrafic>() {
		@Override
		public InfosTrafic createFromParcel(Parcel source) {
			return new InfosTrafic(source);
		}

		@Override
		public InfosTrafic[] newArray(int size) {
			return new InfosTrafic[size];
		}
	};
}
