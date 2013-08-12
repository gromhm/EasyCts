package com.easycts.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Deviation implements Parcelable 
{
	private String id;
	private String titre;
	private String description;
	private String exergue;
	private String position;
	private String lignes;
	private String debut, fin;
	private String categorie;

	public Deviation(String id, String titre, String description, String exergue, String categorie, String position, String lignes, String debut, String fin)
	{
		 this.id = id;
		 this.titre=  titre;
		 this.description = description;
		 this.exergue = exergue;
         this.categorie = categorie;
		 this.position = position;
		 this.lignes = lignes;
		 this.debut = debut;
		 this.fin = fin;
	}
	
	public Deviation(Parcel in) 
	{
		 this.id = in.readString();
		 this.titre=   in.readString();
		 this.description =  in.readString();
		 this.exergue =  in.readString();
         this.categorie = in.readString();
		 this.position =  in.readString();
		 this.lignes =  in.readString();
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
	
	public String getExergue() {
		return exergue;
	}

    public String getCategorie() {
        return categorie;
    }

	public String getPosition() {
		return position;
	}
	
	public String getLignes() {
		return lignes;
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
		dest.writeString(exergue);
        dest.writeString(categorie);
		dest.writeString(position);
		dest.writeString(lignes);
		dest.writeString(debut);
		dest.writeString(fin);
	}
	
	public static final Parcelable.Creator<Deviation> CREATOR = new Parcelable.Creator<Deviation>() {
		@Override
		public Deviation createFromParcel(Parcel source) {
			return new Deviation(source);
		}

		@Override
		public Deviation[] newArray(int size) {
			return new Deviation[size];
		}
	};
}
