package com.easycts.Task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.easycts.Database.FavAdapter;
import com.easycts.Models.Fav;

import java.util.ArrayList;

public class FavTask extends AsyncTask<Object, Integer, Boolean>
{
    FavAdapter favDBAdapter;
	Context mContext;

	public interface FavTaskFinishedListener
	{
		void onTaskFinished(Boolean result);
	}

	public FavTask(Context context)
	{
		mContext = (Context)context;
	}


	@Override
	protected Boolean doInBackground(Object... params)
	{
            ArrayList<Fav> favs = (ArrayList<Fav>)params[0];
            int action = (Integer)params[1];
            Long result;
            FavAdapter adapter = new FavAdapter(mContext);
            adapter.open();

            if(action==0)
            {
                Fav fav = favs.get(0);
                if(fav.getKey() != 0)
                    result = adapter.delete(fav.getKey());
                else
                    result = adapter.delete(fav.getLigneCtsKey(), fav.getArretCtsKey());

                if(result != -1)
                    return true;
            }
            else if(action==1)
            {
                Fav fav = favs.get(0);
                result= adapter.insert(fav);
                if(result != -1)
                    return true;
            }
            else if(action==2) //Clear all fav by ligne
            {
                adapter.setLigneFavs(favs, (String)params[2]);
                return true;
            }

        return false;
	}
	
	
	@Override
	protected void onProgressUpdate(Integer... values) 
	{
		super.onProgressUpdate(values);
	}
	
	@Override
	protected void onPostExecute(Boolean result)
	{
		super.onPostExecute(result);

        if(!result)
            Toast.makeText(mContext, "Une erreur est survenue pendant la mise à jour des favoris", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(mContext, "La mise à jour des favoris a bien été effectuée", Toast.LENGTH_SHORT).show();
	}}
