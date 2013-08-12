package com.easycts.Ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.easycts.Intents;
import com.easycts.Models.Deviation;
import com.easycts.R;


/**
 * Created by thomas on 11/08/13.
 */
public class DeviationActivity extends SherlockActivity
{
    Deviation mDeviation;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Params
        Intent i = getIntent();
        mDeviation = i.getParcelableExtra(Intents.DEVIATION);


        //Back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(mDeviation.getTitre());

        setContentView(R.layout.activity_deviation);
        ((TextView) findViewById(R.id.activity_deviation_title)).setText(mDeviation.getTitre());
        ((TextView) findViewById(R.id.activity_deviation_lignes)).setText(mDeviation.getLignes());
        ((TextView) findViewById(R.id.activity_deviation_categorie)).setText(mDeviation.getCategorie());
        ((TextView) findViewById(R.id.activity_deviation_periode)).setText(mDeviation.getDebut() + " - " + mDeviation.getFin());
        ((TextView) findViewById(R.id.activity_deviation_detail)).setText(mDeviation.getDescription());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
