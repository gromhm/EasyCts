package com.easycts.Task;

import android.os.AsyncTask;

import com.easycts.Network.soapHelper;

import java.net.SocketTimeoutException;

public class GenericSoapTask<T> extends AsyncTask<Object, Integer, T> 
{
	
	public interface StationTaskFinishedListener<T> {
		void onTaskFinished(T result);
	}

	private final StationTaskFinishedListener<T> stationTaskFinishedListener;
	private soapHelper<T> soapAction;
	
	public GenericSoapTask(StationTaskFinishedListener<T> stationTaskFinishedListener, soapHelper<T> soapAction) 
	{
		this.stationTaskFinishedListener = stationTaskFinishedListener;
		this.soapAction = soapAction;
	}

	@Override
	protected T doInBackground(Object... params)
	{
		return downloadResources(params);
	}

	private T downloadResources(Object... params) 
	{
		try 
		{
			return soapAction.execute(params);
		}
		catch (SocketTimeoutException e1) 
		{
			e1.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(T result) 
	{
		super.onPostExecute(result);
		stationTaskFinishedListener.onTaskFinished(result);
	}
}