package com.easycts;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;

import com.easycts.ui.CollectionStationActivity;


public class Utils 
{
	public static List<Integer> loadArray(Context mContext) {  
	    SharedPreferences prefs = mContext.getSharedPreferences(CollectionStationActivity.FAVORITES, Context.MODE_PRIVATE);  
	    int size = prefs.getInt(CollectionStationActivity.FAVORITES + "_size", -1);
	    List<Integer> intList = new ArrayList<Integer>();
	    if(size != -1)
	    {
		    for(int i=0;i<size;i++)
		    	intList.add(prefs.getInt(CollectionStationActivity.FAVORITES + "_" + i, -1));  
	    }
	    	return intList;
	}
	
	public static boolean saveArray(Context mContext, List<Integer> favArray) 
	{   
	    SharedPreferences prefs = mContext.getSharedPreferences(CollectionStationActivity.FAVORITES, Context.MODE_PRIVATE);  
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.clear();
	    editor.putInt(CollectionStationActivity.FAVORITES +"_size", favArray.size());  
	    for(int i=0;i < favArray.size();i++) 
	        editor.putInt(CollectionStationActivity.FAVORITES + "_" + i, favArray.get(i));  
	    return editor.commit();  
	}
}
