package com.easycts.Models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.easycts.Database.LigneDBAdapter;
import com.easycts.Database.StationDBAdapter;

public class Ligne implements Parcelable
{
	// Notez que l'identifiant est un long
	  private long id;
	  private String title;
	  private String direction1;
	  private String direction2;
	  private int type;
	  
		public Ligne()
		{
			
		}
	  
	  public Ligne(long id, String title, int type, String direction1, String direction2) {
	    super();
	    this.id = id;
	    this.title = title;
	    this.type = type;
	    this.direction1 = direction1;
	    this.direction2 = direction2;
	  }
	 
	  public Ligne(Parcel in) 
	  {
		  	this.id = in.readLong();
		    this.title = in.readString();
		    this.type = in.readInt();
		    this.direction1 = in.readString();
		    this.direction2 = in.readString();
		}
	  
	  public long getId() {
	    return id;
	  }
	 
	  public void setId(long id) {
	    this.id = id;
	  }
	 
	  public String getTitle() {
	    return title;
	  }
	 
	  public void setTitle(String title) {
	    this.title = title;
	  }
	 
	  public String getDirection1() {
		    return direction1;
	  }
	 
	  public void setDirection1(String direction) {
	    this.direction1 = direction;
	  }
	  
	  public String getDirection2() {
		    return direction2;
	  }
	 
	  public void setDirection2(String direction) {
	    this.direction2 = direction;
	  }
	  
	  public int getType() {
		    return type;
	  }
	 
	  public void setDirection2(int type) 
	  {
	    this.type = type;
	  }
	  
	public static Ligne FromCursor(Cursor cursor)
	{
		return new Ligne(cursor.getLong(cursor.getColumnIndex(LigneDBAdapter.LIGNE_KEY)), 
    			cursor.getString(cursor.getColumnIndex(LigneDBAdapter.LIGNE_CTSID)),
    			cursor.getInt(cursor.getColumnIndex(LigneDBAdapter.LIGNE_TYPE)),
    			cursor.getString(cursor.getColumnIndex(LigneDBAdapter.LIGNE_DIR1)),
    			cursor.getString(cursor.getColumnIndex(LigneDBAdapter.LIGNE_DIR2)));
	}

    public static Ligne FromCursorWithSpecificId(Cursor cursor, String id)
    {
        return new Ligne(cursor.getLong(cursor.getColumnIndex(id)),
                cursor.getString(cursor.getColumnIndex(LigneDBAdapter.LIGNE_CTSID)),
                cursor.getInt(cursor.getColumnIndex(LigneDBAdapter.LIGNE_TYPE)),
                cursor.getString(cursor.getColumnIndex(LigneDBAdapter.LIGNE_DIR1)),
                cursor.getString(cursor.getColumnIndex(LigneDBAdapter.LIGNE_DIR2)));
    }
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) 
	{
		dest.writeLong(id);
		dest.writeString(title);
		dest.writeInt(type);
		dest.writeString(direction1);
		dest.writeString(direction2);
	}
	
	public static final Parcelable.Creator<Ligne> CREATOR = new Parcelable.Creator<Ligne>() {
		@Override
		public Ligne createFromParcel(Parcel source) {
			return new Ligne(source);
		}

		@Override
		public Ligne[] newArray(int size) {
			return new Ligne[size];
		}
	};
}
