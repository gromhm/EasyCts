package com.easycts.Network;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;



public abstract class soapHelper<T>
{	
	protected String getUrl()
	{
		return "http://opendata.cts-strasbourg.fr/webservice_v4/Service.asmx";
	}
	
	protected String getNamespace()
	{
		return "http://www.cts-strasbourg.fr/";
	}
	
	private String pwd;
	protected String getPassword()
	{
		return pwd;
	}

	protected abstract String getMethodName();

	public soapHelper(String pwd)
	{
		this.pwd = pwd;
	}
	
 	private Element[] getAuthHeader(String pwd, String namespace) 
	{
		Element[] header = new Element[1];
		
	    Element h = new Element().createElement(namespace, "CredentialHeader");
	    
	    Element username = new Element().createElement(namespace, "ID");
	    username.addChild(Node.TEXT, pwd); 	    
	    h.addChild(Node.ELEMENT, username);
	    
	    Element pass = new Element().createElement(namespace, "MDP");
	    pass.addChild(Node.TEXT, "piurght");
	    h.addChild(Node.ELEMENT, pass);
	    
	    header[0] = h;
	    
	    return header;
	}
 	
 	public T execute(Object... params) throws IOException, XmlPullParserException
 	{
 		//Toutes les données demandées sont mises dans une enveloppe.
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope (SoapEnvelope.VER11);
		envelope.encodingStyle = "utf-8";
		envelope.dotNet = false;
		
		String namespace = getNamespace();
		String methodName = getMethodName();
		String pwd = getPassword();
		String url = getUrl();
		
		//Header        
		envelope.headerOut = getAuthHeader(pwd, namespace);
		
		//Body 
		SoapObject request = getBodyRequest(params);

		//Préparation de la requete
		envelope.bodyOut = request;
        envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE (url);
		
		androidHttpTransport.debug = true;
		
		//Envoie de la requete
		androidHttpTransport.call(namespace+methodName, envelope);
		
		//Obtention du résultat
		SoapObject soapResult = (SoapObject) envelope.getResponse ();
		return parser(soapResult);
 	}
 	
 	protected abstract SoapObject getBodyRequest(Object... params);
 	
 	protected abstract T parser(SoapObject results);

}