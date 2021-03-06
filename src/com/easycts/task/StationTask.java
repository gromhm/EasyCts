package com.easycts.task;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.easycts.R;
import com.easycts.Models.StationLigne;
import com.easycts.ui.SplashActivity;

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

public class StationTask extends AsyncTask<String, Integer, ArrayList<StationLigne>> 
{
	
	public interface StationTaskFinishedListener {
		void onTaskFinished(ArrayList<StationLigne> result);
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
	public StationTask(StationTaskFinishedListener stationTaskFinishedListener) {
		this.stationTaskFinishedListener = stationTaskFinishedListener;
	}

	@Override
	protected ArrayList<StationLigne> doInBackground(String... params)
	{
		return downloadResources(params[0], params[1], params[2]);
	}

	private ArrayList<StationLigne> downloadResources(String ctsid, String id, String dbUrl) 
	{
		int count;
		String resultToReturn=null;
		HttpURLConnection conection = null;

		try {
			URL url = new URL(dbUrl + "?type=station&ctsid="+ctsid+"&id="+id);
			Log.d("StationTask","StationTask:dl:"+url.toString());
			conection = (HttpURLConnection) url.openConnection();
			conection.setConnectTimeout(20000);
			conection.connect();

			if (conection.getResponseCode() == HttpURLConnection.HTTP_OK) 
			{
				BufferedReader br = new BufferedReader(new InputStreamReader(conection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                br.close();
                resultToReturn = sb.toString();
			}
		}
		catch (Exception e) 
		{
			Log.e("Error: ", e.getMessage());
		}
		finally
		{
			if(conection != null)
				conection.disconnect();
		}
		
		return ProcessResults(resultToReturn);
	}

	private ArrayList<StationLigne> ProcessResults(String resuls)
	{
		Map<String, StationLigne> stationsGrouped = new HashMap<String, StationLigne>();
		
		try 
		{
			JSONObject jsonresults = new JSONObject(resuls);
			
			// Set Results
			if (jsonresults.length()>0) 
			{
				Iterator<String> iter = jsonresults.keys();
			    while (iter.hasNext()) 
			    {
			        String key = iter.next();
			        try 
			        {
			        	JSONObject value = (JSONObject)jsonresults.get(key);
			        	String type = ((JSONObject)value.get("mode")).get("value").toString();
						String endstation = ((JSONObject)value.get("destination")).get("value").toString();
						String hours = ((JSONObject)value.get("horaire")).get("value").toString();

						if(stationsGrouped.containsKey(endstation))
						{
							stationsGrouped.get(endstation).getHours().add(hours);
						}
						else
						{
							StationLigne stationLigne = new StationLigne(endstation, type);
							stationLigne.getHours().add(hours);
							stationsGrouped.put(endstation, stationLigne);
						}
			        } catch (JSONException e) 
			        {
						Log.d("StationTask", "stationtask : error parsing results");
			        }
			    }
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return  new ArrayList<StationLigne>(stationsGrouped.values());
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(ArrayList<StationLigne> result) 
	{
		super.onPostExecute(result);
		stationTaskFinishedListener.onTaskFinished(result);
	}
}