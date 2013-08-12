package com.easycts.Network;

import com.easycts.Models.InfosTrafic;

import org.ksoap2.serialization.SoapObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class soapInfosTraficHelper extends soapHelper<ArrayList<InfosTrafic>> {
	public soapInfosTraficHelper(String pwd) {
		super(pwd);
	}

	private final String METHOD_NAME = "infosTrafic";

	@Override
	protected String getMethodName() {
		return METHOD_NAME;
	}

	@Override
	protected SoapObject getBodyRequest(Object... params) {
		String namespace = getNamespace();

		// Création de la requete SOAP
		SoapObject request = new SoapObject(namespace, METHOD_NAME);
		return request;
	}

	@Override
	protected ArrayList<InfosTrafic> parser(SoapObject results) {
		SoapObject arrivees = (SoapObject) results.getProperty("InfosTrafic");
		ArrayList<InfosTrafic> infosTrafic = new ArrayList<InfosTrafic>();
		int propertiesCount = arrivees.getPropertyCount();
		//DateFormat df = new SimpleDateFormat("yyyy/MM/ddTHH:mm:ss");
        SimpleDateFormat originalDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		for (int i = 0; i < propertiesCount; i++) 
		{
			SoapObject arivee = (SoapObject) arrivees.getProperty(i);
			String id = arivee.getProperty("ID").toString();
			String titre = arivee.getProperty("Titre").toString();
			String description = arivee.getProperty("Description").toString();
            String date = null, fin = null;

            try
            {
                date = dateFormat.format(originalDateFormat.parse(arivee.getProperty("Debut").toString()));
                fin= dateFormat.format(originalDateFormat.parse(arivee.getProperty("Fin").toString()));
            } catch (ParseException e)
            {
                e.printStackTrace();
            }

            infosTrafic.add(new InfosTrafic(id, titre, description, date, fin));
		}

		return infosTrafic;
	}
}