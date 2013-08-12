package com.easycts.Ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.easycts.Intents;
import com.easycts.Models.InfosTrafic;
import com.easycts.R;

/**
 * Created by thomas on 11/08/13.
 */
public class InfosTraficActivity extends SherlockActivity
{
    InfosTrafic mInfoTraf;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Params
        Intent i = getIntent();
        mInfoTraf = i.getParcelableExtra(Intents.INFOSTRAFIC);

        //Back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(mInfoTraf.getTitre());

        setContentView(R.layout.activity_infostrafic);
        ((TextView) findViewById(R.id.activity_infostrafic_title)).setText(mInfoTraf.getTitre());
        ((TextView) findViewById(R.id.activity_infostrafic_periode)).setText(mInfoTraf.getDebut() + " - " + mInfoTraf.getFin());
        ((TextView) findViewById(R.id.activity_infostrafic_detail)).setText(mInfoTraf.getDescription());
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
