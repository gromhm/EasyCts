package com.easycts.Network;

import com.easycts.Models.InfosTrafic;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

public class soapInfosTraficHelper extends soapHelper<ArrayList<InfosTrafic>> {
	public soapInfosTraficHelper(String pwd) {
		super(pwd);
	}

	private final String METHOD_NAME = "InfosTrafic";

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
		
		for (int i = 0; i < propertiesCount; i++) 
		{
			SoapObject arivee = (SoapObject) arrivees.getProperty(i);
			String id = arivee.getProperty("ID").toString();
			String titre = arivee.getProperty("Titre").toString();
			String description = arivee.getProperty("Description").toString();
			String date = arivee.getProperty("Debut").toString();
			String fin = arivee.getProperty("Fin").toString();
			infosTrafic.add(new InfosTrafic(id, titre, description, exergue, position, lignes, date, fin));
		}

		return infosTrafic;
	}
}