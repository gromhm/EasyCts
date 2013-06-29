package com.easycts.ui;

import java.io.File;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.easycts.task.LoadingTask;
import com.easycts.task.LoadingTask.LoadingTaskFinishedListener;
import com.easycts.ui.Network.soapHelper;
import com.easycts.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

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

		dbName = getString(R.string.database_name);
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
				Toast.makeText(this, "La v�rification de mise � jour n'a pu �tre effectu�e.", Toast.LENGTH_LONG).show();
				completeSplash();
			}
			else
				closeAppWithMessage("Easy CTS n�c�site un acc�s r�seau pour son premier d�marage");
		}
	}

	// This is the callback for when your async task has finished
	@Override
	public void onTaskFinished(Integer result) 
	{
		//File downloaded
		if(result == 1)
		{
			Toast.makeText(this, "Les donn�es ont bien �t�s mises � jour.", Toast.LENGTH_LONG).show();
			completeSplash();
		}
		//No update
		else if(result == 2)
		{
			if (resourcesAlreadyExist) 
			{
				Toast.makeText(this, "Les donn�es sont � jour.", Toast.LENGTH_LONG).show();
				completeSplash();
			}
		}
		//Error
		else
		{
			if (resourcesAlreadyExist) 
			{
				Toast.makeText(this, "Erreur lors de la v�rification de mise � jour.", Toast.LENGTH_LONG).show();
				completeSplash();
			}
			else
				closeAppWithMessage("Erreur r�seau.");
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
