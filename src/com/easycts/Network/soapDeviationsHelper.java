package com.easycts.Network;

import com.easycts.Models.Deviation;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

public class soapDeviationsHelper extends soapHelper<ArrayList<Deviation>> {
	public soapDeviationsHelper(String pwd) {
		super(pwd);
	}

	private final String METHOD_NAME = "deviations";

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
	protected ArrayList<Deviation> parser(SoapObject results) {
		SoapObject arrivees = (SoapObject) results.getProperty("Deviations");
		ArrayList<Deviation> deviations = new ArrayList<Deviation>();
		int propertiesCount = arrivees.getPropertyCount();
		//DateFormat df = new SimpleDateFormat("yyyy/MM/ddTHH:mm:ss");
		
		for (int i = 0; i < propertiesCount; i++) 
		{
			SoapObject arivee = (SoapObject) arrivees.getProperty(i);
			String id = arivee.getProperty("ID").toString();
			String titre = arivee.getProperty("Titre").toString();
			String description = arivee.getProperty("Description").toString();
			String exergue = arivee.getProperty("Exergue").toString();
			String position = arivee.getProperty("Position").toString();
			String lignes = arivee.getProperty("Lignes").toString();
			String date = arivee.getProperty("Debut").toString();
			String fin = arivee.getProperty("Fin").toString();
			deviations.add(new Deviation(id, titre, description, exergue, position, lignes, date, fin));
		}

		return deviations;
	}
}