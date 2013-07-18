package com.easycts.Ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.easycts.Database.DBAdapter;
import com.easycts.R;
import com.easycts.Task.LoadingTask;
import com.easycts.Task.LoadingTask.LoadingTaskFinishedListener;

import java.io.File;

public class SplashActivity extends Activity implements LoadingTaskFinishedListener {
	
	String dbName, filesDir;
	Boolean resourcesAlreadyExist;
	public final static String PREFS_NAME = "easyCtsPrefs";
	public final static String PREFS_DBVERSION = "dbVersion";
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

		dbName = DBAdapter.DATABASE_NAME;
		filesDir = getFilesDir().getPath();
		resourcesAlreadyExist = this.resourcesAlreadyExist(filesDir, dbName);
		Boolean networkIsActif = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
		
		if (networkIsActif)
		{			 
			// Show the splash screen
			setContentView(R.layout.activity_splash);

			// Find the progress bar
			ProgressBar progressBar = (ProgressBar) findViewById(R.id.activity_splash_progress_bar);

			// Start your loading
			new LoadingTask(progressBar, this).execute(dbName);
		} 
		else 
		{
			if (resourcesAlreadyExist)
			{
				Toast.makeText(this, "La vérification de mise é jour n'a pu étre effectuée.", Toast.LENGTH_LONG).show();
				completeSplash();
			}
			else
				closeAppWithMessage("Easy CTS nécésite un accés réseau pour son premier démarage");
		}
	}

	// This is the callback for when your async Task has finished
	@Override
	public void onTaskFinished(Integer result) 
	{
		//File downloaded
		if(result == 1)
		{
			Toast.makeText(this, "Les données ont bien étés mises é jour.", Toast.LENGTH_LONG).show();
			completeSplash();
		}
		//No update
		else if(result == 2)
		{
			if (resourcesAlreadyExist) 
			{
				Toast.makeText(this, "Les données sont à jour.", Toast.LENGTH_LONG).show();
				completeSplash();
			}
		}
		//Error
		else
		{
			if (resourcesAlreadyExist) 
			{
				Toast.makeText(this, "Erreur lors de la vérification de mise à jour.", Toast.LENGTH_LONG).show();
				completeSplash();
			}
			else
				closeAppWithMessage("Erreur réseau.");
		}
	}

	private void completeSplash() {
		startApp();
		finish(); // Don't forget to finish this Splash Activity so the user
					// can't return to it!
	}

	private void startApp() {
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		startActivity(intent);
	}

	private void closeAppWithMessage(String message)
	{
		Toast.makeText(this,message, Toast.LENGTH_LONG).show();
		finish();
	}
	
	private boolean resourcesAlreadyExist(String filesDir, String fileName) 
	{
		return new File(String.format("%s/%s", filesDir, fileName)).exists();
	}
}
