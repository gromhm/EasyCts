package com.easycts.ui.Network;

import java.io.IOException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.xmlpull.v1.XmlPullParserException;


public class soapHelper
{
	private static final String	SOAP_ACTION	= "http://www.cts-strasbourg.fr/rechercheProchainesArriveesWeb";
	private static final String	METHOD_NAME	= "rechercheProchainesArriveesWeb";
	private static final String	NAMESPACE	= "http://www.cts-strasbourg.fr/";
	//L'URL suivante ne peut pas être localhost car localhost représente l'émulateur
	private static final String	URL	= "http://opendata.cts-strasbourg.fr/webservice_v4/Service.asmx?wsdl";

	public static SoapObject soap () throws IOException, XmlPullParserException
	{
		// Création de la requête SOAP
		SoapObject request = new SoapObject (NAMESPACE, METHOD_NAME);
		//Toutes les données demandées sont mises dans une enveloppe.
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope (
				SoapEnvelope.VER11);
		envelope.dotNet = false;
		
		envelope.headerOut = new Element[1];
		envelope.headerOut[0] = buildAuthHeader();
		
		envelope.bodyOut = new Element[1];
		envelope.bodyOut = buildBody();
		
		//Préparation de la requête
		envelope.setOutputSoapObject (request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE (URL);
		
		
		//Envoie de la requête
		androidHttpTransport.call (SOAP_ACTION, envelope);
		//Obtention du résultat
		SoapObject soapResult = (SoapObject) envelope.getResponse ();
		return soapResult;
	}
	
	private static Element buildAuthHeader() {
	    Element h = new Element().createElement(NAMESPACE, "CredentialHeader");
	    Element username = new Element().createElement(NAMESPACE, "ID");
	    username.addChild(Node.TEXT, "sieferthomas@gmail.com");
	    h.addChild(Node.ELEMENT, username);
	    Element pass = new Element().createElement(NAMESPACE, "MDP");
	    pass.addChild(Node.TEXT, "piurght");
	    h.addChild(Node.ELEMENT, pass);

	    return h;
	}
	
	private static Element buildBody() {
	    Element h = new Element().createElement(NAMESPACE, "rechercheProchainesArriveesWeb");
	    Element username = new Element().createElement(NAMESPACE, "CodeArret");
	    username.addChild(Node.TEXT, "319");
	    h.addChild(Node.ELEMENT, username);
	    
	    Element pass = new Element().createElement(NAMESPACE, "Mode");
	    pass.addChild(Node.TEXT, "0");
	    h.addChild(Node.ELEMENT, pass);

	    Element c = new Element().createElement(NAMESPACE, "Heure");
	    c.addChild(Node.TEXT, "14");
	    h.addChild(Node.ELEMENT, c);
	    
	    Element d = new Element().createElement(NAMESPACE, "NbHoraires");
	    d.addChild(Node.TEXT, "5");
	    h.addChild(Node.ELEMENT, d);
	    return h;
	}
}