package qianye.jnak.service;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * 用户认证
 * 
 * @author panxianyi
 * 
 */
public class sso {
	private String NAMESPACE = "http://tempuri.org/";
	private String URL = "http://218.24.45.57:6377/ServiceUUCA.svc";
	private String METHOD_NAME = "LoginIn";
	private String SOAP_ACTION = "http://tempuri.org/IServiceUUCA/LoginIn";

	public sso() {
	}

	public sso(String namespace, String url, String methodName,
			String soapAction) {
		this.NAMESPACE = namespace;
		this.URL = url;
		this.METHOD_NAME = methodName;
		this.SOAP_ACTION = soapAction;
	}

	public String CheckUser(String user, String pass) {
		String returnStr = "0";

		try {

			SoapObject soapObject = new SoapObject(NAMESPACE, METHOD_NAME);
			soapObject.addProperty("userID", user);
			soapObject.addProperty("password", pass);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(soapObject);

			// AndroidHttpTransport ht = new AndroidHttpTransport(URL);
			HttpTransportSE ht = new HttpTransportSE(URL);
			ht.call(SOAP_ACTION, envelope);

			// SoapObject result = (SoapObject)envelope.bodyIn;
			// SoapObject detail = (SoapObject)
			// result.getProperty("getWeatherbyCityNameResult");

			Object obj = envelope.getResponse();

			// SoapObject detail =(SoapObject)obj;

			// Object result = (Object)envelope.getResponse();
			returnStr = obj.toString();
			// soapObject = (SoapObject) envelope.bodyIn;
			// SoapObject detail = (SoapObject)
			// result.getProperty("getWeatherResult");
			// System.out.println(soapObject.toString());
			// success= Boolean.parseBoolean(soapObject.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return returnStr;
	}

	public Boolean UserLogOut(String user) {
		Boolean success = false;
		String METHOD_NAME = "LogOut";
		String SOAP_ACTION = "http://tempuri.org/IServiceUUCA/LogOut";
		try {

			SoapObject soapObject = new SoapObject(NAMESPACE, METHOD_NAME);
			soapObject.addProperty("userID", user);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.bodyOut = soapObject;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(soapObject);

			HttpTransportSE ht = new HttpTransportSE(URL);
			ht.call(SOAP_ACTION, envelope);

			Object obj = envelope.getResponse();

			// SoapObject detail =(SoapObject)obj;

			// Object result = (Object)envelope.getResponse();
			// returnStr=obj.toString();
			// soapObject = (SoapObject) envelope.bodyIn;
			// SoapObject detail = (SoapObject)
			// result.getProperty("getWeatherResult");
			// System.out.println(soapObject.toString());
			success = Boolean.parseBoolean(obj.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return success;
	}
}
