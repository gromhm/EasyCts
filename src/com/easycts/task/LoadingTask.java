package com.easycts.task;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import com.easycts.R;
import com.easycts.ui.SplashActivity;
import com.easycts.ui.Network.soapHelper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LoadingTask extends AsyncTask<String, Integer, Integer> 
{
	
	public interface LoadingTaskFinishedListener {
		void onTaskFinished(Integer result);
	}

	// This is the progress bar you want to update while the task is in progress
	private final ProgressBar progressBar;
	// This is the listener that will be told when this task is finished
	private final LoadingTaskFinishedListener finishedListener;
	Context mContext;

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
	public LoadingTask(ProgressBar progressBar, LoadingTaskFinishedListener finishedListener) {
		mContext = ((Activity) finishedListener);
		this.progressBar = progressBar;
		this.finishedListener = finishedListener;
	}

	@Override
	protected Integer doInBackground(String... params) {
		Log.i("Tutorial", "Starting download task ");
		
		try {
			soapHelper.soap();
		}
		catch (SocketTimeoutException e1) 
		{
			e1.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return downloadResources(params[0]);
	}

	private Integer downloadResources(String dbName) {
		int count, resultToReturn=0;
		String dbUrl = mContext.getString(R.string.database_url);
		HttpURLConnection conection = null;
		
		//Get the currentDbVersion
		 SharedPreferences settings = mContext.getSharedPreferences(SplashActivity.PREFS_NAME, 0);
		 Float currentDbVersion = settings.getFloat(SplashActivity.PREFS_DBVERSION, 0f);
		 Float serverVersion = currentDbVersion;
		 
		try {
			URL url = new URL(dbUrl + "?type=upgrade&version="+currentDbVersion);
			conection = (HttpURLConnection) url.openConnection();
			conection.setConnectTimeout(20000);
			conection.connect();

			if (conection.getResponseCode() == HttpURLConnection.HTTP_OK) 
			{
	            String disposition = conection.getHeaderField("Content-Disposition");	 
	            if (disposition != null) 
	            {
	                // extracts file name from header field
	                int index = disposition.indexOf("filename=");
	                if (index > 0) 
	                {
	                    String serverfileName = disposition.substring(index + 9, disposition.length());
	                    serverVersion = Float.parseFloat(serverfileName.substring(8, serverfileName.length()-7));
	                }
	            }

				// getting file length
				int lenghtOfFile = conection.getContentLength();

				InputStream input = new BufferedInputStream(url.openStream(), 8192);

				// Output stream to write file
				FileOutputStream output = mContext.openFileOutput(dbName,
						Activity.MODE_PRIVATE);

				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					// publishing the progress....
					// After this onProgressUpdate will be called
					publishProgress((int) ((total * 100) / lenghtOfFile));

					// writing data to file
					output.write(data, 0, count);
				}
				String str = new String(data, "UTF-8");
				// flushing output
				output.flush();

				// closing streams
				output.close();
				input.close();
				
				//Save dbversion
				SharedPreferences.Editor editor = settings.edit();
				editor.putFloat(SplashActivity.PREFS_DBVERSION, serverVersion);
				editor.commit();
				resultToReturn = 1;
			}
			else if(conection.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT)
			{
				resultToReturn = 2;
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
		
		return resultToReturn;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		progressBar.setProgress(values[0]); // This is ran on the UI thread so
											// it is ok to update our progress
											// bar ( a UI view ) here
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		finishedListener.onTaskFinished(result);
	}
}