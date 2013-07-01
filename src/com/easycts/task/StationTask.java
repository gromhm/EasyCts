package com.easycts.task;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.easycts.R;
import com.easycts.Models.StationHours;
import com.easycts.ui.SplashActivity;
import com.easycts.ui.Network.soapHoursHelper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.Toast;

public class StationTask extends AsyncTask<String, Integer, ArrayList<StationHours>> 
{
	
	public interface StationTaskFinishedListener {
		void onTaskFinished(ArrayList<StationHours> result);
	}
	// This is the listener that will be told when this task is finished
	private final StationTaskFinishedListener stationTaskFinishedListener;

	/**
	 * A Loading task that will load some resources that are necessary for the
	 * app to start
	 * 
	 * @param progressBar
	 *            - the progress bar you want to update while the task is in
	 *            progress
	 * @param finishedListener
	 *            - the listener that will be told when this task is finished
	 */
	public StationTask(StationTaskFinishedListener stationTaskFinishedListener) 
	{
		this.stationTaskFinishedListener = stationTaskFinishedListener;
	}

	@Override
	protected ArrayList<StationHours> doInBackground(String... params)
	{
		return downloadResources(params[0], params[1], params[2], params[3]);
	}

	private ArrayList<StationHours> downloadResources(String pwd, String ctsid, String id, String ligneType) 
	{
		try 
		{
			return new soapHoursHelper().getHours(pwd, Integer.parseInt(ctsid), Integer.parseInt(ligneType) ==0 ? 1:2, new Date(), 5);
		}
		catch (SocketTimeoutException e1) 
		{
			e1.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(ArrayList<StationHours> result) 
	{
		super.onPostExecute(result);
		stationTaskFinishedListener.onTaskFinished(result);
	}
}