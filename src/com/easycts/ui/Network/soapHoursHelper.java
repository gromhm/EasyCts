package com.easycts.ui.Network;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.xmlpull.v1.XmlPullParserException;

import android.R;

import com.easycts.Models.StationHours;


public class soapHoursHelper
{
	private final String	METHOD_NAME	= "rechercheProchainesArriveesWeb";
	private final String	NAMESPACE	= "http://www.cts-strasbourg.fr/";
	private final String	URL	= "http://opendata.cts-strasbourg.fr/webservice_v4/Service.asmx";

	public ArrayList<StationHours> getHours (String pwd, int code, int mod, Date date, int nbHours) throws IOException, XmlPullParserException
	{	
		//Toutes les données demandées sont mises dans une enveloppe.
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope (SoapEnvelope.VER11);
		envelope.encodingStyle = "utf-8";
		envelope.dotNet = false;
		
		//Body 
		SoapObject request = getBodyRequest(code, mod, date, nbHours);
		
		//Header        
		envelope.headerOut = buildAuthHeader(pwd);

		//Préparation de la requête
		envelope.bodyOut = request;
        envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE (URL);
		
		androidHttpTransport.debug = true;
		
		//Envoie de la requête
		androidHttpTransport.call (NAMESPACE+METHOD_NAME, envelope);
		//Obtention du résultat
		SoapObject soapResult = (SoapObject) envelope.getResponse ();
		return parser(soapResult);
	}
	
	private SoapObject getBodyRequest(int code, int mod, Date date, int nbHours)
	{
		// Création de la requête SOAP
		SoapObject request = new SoapObject (NAMESPACE, METHOD_NAME);
		
		//body
		PropertyInfo ca = new PropertyInfo();
		ca.setNamespace(NAMESPACE);
		ca.setName("CodeArret"); 
		ca.setValue(Integer.toString(code)); 
        request.addProperty(ca);
        
		PropertyInfo mo = new PropertyInfo();
		mo.setNamespace(NAMESPACE);
		mo.setName("Mode"); 
		mo.setValue(mod); 
        request.addProperty(mo);
        
		PropertyInfo he = new PropertyInfo();
		he.setNamespace(NAMESPACE);
		he.setName("Heure"); 
		he.setValue(new SimpleDateFormat("HH:mm:ss").format(date)); 
        request.addProperty(he);
        
		PropertyInfo nbh = new PropertyInfo();
		nbh.setNamespace(NAMESPACE);
		nbh.setName("NbHoraires"); 
		nbh.setValue(nbHours); 
        request.addProperty(nbh);
        
        return request;
	}
	
 	private Element[] buildAuthHeader(String pwd) 
	{
		Element[] header = new Element[1];
		
	    Element h = new Element().createElement(NAMESPACE, "CredentialHeader");
	    
	    Element username = new Element().createElement(NAMESPACE, "ID");
	    username.addChild(Node.TEXT, pwd); 	    
	    h.addChild(Node.ELEMENT, username);
	    
	    Element pass = new Element().createElement(NAMESPACE, "MDP");
	    pass.addChild(Node.TEXT, "piurght");
	    h.addChild(Node.ELEMENT, pass);
	    
	    header[0] = h;
	    
	    return header;
	}
 	
 	private ArrayList<StationHours> parser(SoapObject results)
 	{
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
 		
 		return new ArrayList<StationHours>(stationsGrouped.values());
 	}
}