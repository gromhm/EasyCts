package com.easycts.Network;

import android.annotation.SuppressLint;

import com.easycts.Models.StationHours;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class soapHoursHelper extends soapHelper<ArrayList<StationHours>>
{
	public soapHoursHelper(String pwd) {
		super(pwd);
	}

	private final String	METHOD_NAME	= "rechercheProchainesArriveesWeb";

	@Override
	protected String getMethodName() {
		return METHOD_NAME;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	protected SoapObject getBodyRequest(Object... params) 
	{
		String namespace = getNamespace();
		int code = Integer.parseInt((String)params[0]);
		String ligneTitle= params[1].toString();
		Date date= new Date();
		int nbHours= 5;
		
		// Création de la requete SOAP
		SoapObject request = new SoapObject (namespace, METHOD_NAME);
		
		//body
		PropertyInfo ca = new PropertyInfo();
		ca.setNamespace(namespace);
		ca.setName("CodeArret"); 
		ca.setValue(Integer.toString(code)); 
        request.addProperty(ca);
        
		PropertyInfo mo = new PropertyInfo();
		mo.setNamespace(namespace);
		mo.setName("Mode"); 
		mo.setValue(0);
        request.addProperty(mo);
        
		PropertyInfo he = new PropertyInfo();
		he.setNamespace(namespace);
		he.setName("Heure"); 
		he.setValue(new SimpleDateFormat("HH:mm:ss").format(date)); 
        request.addProperty(he);
        
		PropertyInfo nbh = new PropertyInfo();
		nbh.setNamespace(namespace);
		nbh.setName("NbHoraires"); 
		nbh.setValue(nbHours); 
        request.addProperty(nbh);
        
        return request;
	}

	@Override
	protected ArrayList<StationHours> parser(SoapObject results) {
		SoapObject arrivees = (SoapObject)results.getProperty("ListeArrivee");
 		int propertiesCount = arrivees.getPropertyCount();
 		Map<String, StationHours> stationsGrouped = new HashMap<String, StationHours>();
 		
 		for(int i=0; i<propertiesCount;i++)
 		{
 			SoapObject arivee = (SoapObject)arrivees.getProperty(i);
 			String horaire = arivee.getProperty("Horaire").toString();
 			String destination = arivee.getProperty("Destination").toString();
 			String mode = arivee.getProperty("Mode").toString();
 			String key = mode+"-"+destination;
 			
 			if(stationsGrouped.containsKey(key))
			{
				stationsGrouped.get(key).getHours().add(horaire);
			}
			else
			{
				StationHours stationLigne = new StationHours(destination, mode);
				stationLigne.getHours().add(horaire);
				stationsGrouped.put(key, stationLigne);
			}
 		}

        ArrayList<StationHours> result = new ArrayList<StationHours>(stationsGrouped.values());

        Collections.sort(result, new Comparator<StationHours>() {
            public int compare(StationHours s1, StationHours s2) {
                return s1.getCtsLigneId().compareTo(s2.getCtsLigneId());
            }
        });

 		return result;
	}
}