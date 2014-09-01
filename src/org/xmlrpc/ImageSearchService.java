package org.xmlrpc;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

 
import org.apache.http.conn.HttpHostConnectException;
import org.xmlrpc.Test.XMLRPCMethodCallback;
import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;
import org.xmlrpc.android.XMLRPCFault;

import android.os.Handler;
import android.util.Log;

/**
 * 
 */

class ImageSearchService {

	
	XMLRPCClient client = new XMLRPCClient(URI.create("http://192.168.1.39:9090"));

	interface XMLRPCMethodCallback {
		void callFinished(Object result);
	}
	
	class XMLRPCMethod extends Thread {
		private String method;
		private Object[] params;
		private Handler handler;
		private XMLRPCMethodCallback callBack;
		public XMLRPCMethod(String method, XMLRPCMethodCallback callBack) {
			this.method = method;
			this.callBack = callBack;
			handler = new Handler();
		}
		public void call() {
			call(null);
		}
		public void call(Object[] params) {
			this.params = params;
			start();
		}
		@Override
		public void run() {
    		try {
    			final long t0 = System.currentTimeMillis();
    			final Object result = client.callEx(method, params);
    			final long t1 = System.currentTimeMillis();
    			handler.post(new Runnable() {
					public void run() {
 						callBack.callFinished(result);
					}
    			});
    		} catch (final XMLRPCFault e) {
    			handler.post(new Runnable() {
					public void run() {
 						Log.d("Test", "error", e);
					}
    			});
    		} catch (final XMLRPCException e) {
    			handler.post(new Runnable() {
					public void run() {
 						Throwable couse = e.getCause();
 						Log.d("Test", "error", e);
					}
    			});
    		}
		}
	}

	public ImageSearchService() {
		 
	}

	
	public String[] query(String datasetPath, String dataExtension, String image)
			throws XMLRPCException {


		// set configuration
		setConfiguration(datasetPath, dataExtension);

		// Build our parameter list.
		Object[] params;
		params = new Object[] { "query", image, new Integer(10) };
		
		// Call the server, and get our result.
		Object[] tmp = (Object[]) client.callEx("ImageSearch", params);
		
		
		
		String[] result = new String[tmp.length];
		for (int i = 0; i < tmp.length; i++) {
			result[i] = "" + tmp[i];
		}
		
		return result;
	}

	public String[] getRandomSet(String datasetPath, String dataExtension)
			throws XMLRPCException {

		// set configuration
		setConfiguration(datasetPath, dataExtension);

		Object[] params;
		// Build our parameter list.
		params = new Object[] { "getRandomSet", datasetPath };
		// Call the server, and get our result.
		Log.d("rpc",  params.toString());

		Object[] tmp = (Object[]) client.callEx("ImageSearch", params);
		// Print out our result.

		String[] result = new String[tmp.length];
		for (int i = 0; i < tmp.length; i++) {
			result[i] = "" + tmp[i];
			Log.d("rpc",  result[i]);
		}
		
		return result;
	}

	public void setConfiguration(String datasetPath, String dataExtension) throws XMLRPCException
			{
		// Build our parameter list.
		Object[] params = new Object[] { "configuration", datasetPath,
				dataExtension };
		// Call the server, and get our result.
		
		Log.d("rpc", params.toString());
		client.callEx("ImageSearch", params);
		
	}

}