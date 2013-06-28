package com.easycts.task;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.widget.SimpleCursorAdapter;

import com.easycts.Database.LigneDBAdapter;

public class LignesTask extends AsyncTask<Void, Integer, Cursor>
{
	LigneDBAdapter ligneAdapter;
	LignesTaskFinishedListener lignesTaskFinishedListener;
	
	public interface LignesTaskFinishedListener 
	{
		void onTaskFinished(Cursor cursor);
	}
	
	public LignesTask(LigneDBAdapter ligneAdapter, LignesTaskFinishedListener lignesTaskFinishedListener) 
	{
        this.ligneAdapter = ligneAdapter;
		this.lignesTaskFinishedListener = lignesTaskFinishedListener;
    }
	
	@Override
	protected Cursor doInBackground(Void... params) 
	{
		ligneAdapter.open();
		Cursor cursor = ligneAdapter.getAllLigne(String.valueOf(0));
		return cursor;
	}
	
	 @Override
    protected void onPostExecute(Cursor cursor) 
	{
		 lignesTaskFinishedListener.onTaskFinished(cursor);
    }
}