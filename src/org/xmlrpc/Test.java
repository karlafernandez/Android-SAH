package org.xmlrpc;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.conn.HttpHostConnectException;
import org.xmlrpc.android.XMLRPCClient;
import org.xmlrpc.android.XMLRPCException;
import org.xmlrpc.android.XMLRPCFault;
import org.xmlrpc.android.XMLRPCSerializable;

 
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Demonstrates use of XMLRPC library.
 * 
 * What you really need to do is:
 * <ul>
 *  <li>create XMLRPCClient client
 *  <pre>XMLRPCClient client = new XMLRPCClient(URI.create("http://10.0.2.2:8888"));</pre>
 *  or even
 *  <pre>XMLRPCClient client = new XMLRPCClient("http://10.0.2.2:8888");</pre>
 *  </li>
 *  <li>call RPCXML method
 *  <pre>
 *  try {
 *      // call method "add" with two parameters: 2 and 4
 *      int i = (Integer) client.call("add", 2, 4);
 *      Log.d("XMLRPC Test", "result int i = " + i);
 *  } catch (XMLRPCException e) {
 *      Log.d("XMLRPC Test", "Error", e);
 *  }
 *  </pre>
 *	</li>
 * </ul>
 * 
 */

public class Test extends Activity {

	private XMLRPCClient client;
	private URI uri;
	private DateFormat dateFormat;
	private DateFormat timeFormat;
	private Drawable errorDrawable;

	private TextView status;
	private TextSwitcher testResult;
	private ListView tests;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		uri = URI.create("http://192.168.1.39:9090");
		client = new XMLRPCClient(uri);

		setContentView(R.layout.main);
        testResult = (TextSwitcher) findViewById(R.id.text_result);
        
        LayoutInflater inflater = LayoutInflater.from(this);
        View v0 = inflater.inflate(R.layout.text_view, null);
        View v1 = inflater.inflate(R.layout.text_view, null);
        LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        testResult.addView(v0, 0, layoutParams);
        testResult.addView(v1, 1, layoutParams);
        testResult.setText("WARNING, before calling any test make sure server.py is running !!!");

        Animation inAnim = AnimationUtils.loadAnimation(this, R.anim.push_left_in);
        Animation outAnim = AnimationUtils.loadAnimation(this, R.anim.push_left_out);
//        Animation inAnim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
//        Animation outAnim = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        inAnim.setStartOffset(250);
        testResult.setInAnimation(inAnim);
        testResult.setOutAnimation(outAnim);
        errorDrawable = getResources().getDrawable(R.drawable.error);
        errorDrawable.setBounds(0, 0, errorDrawable.getIntrinsicWidth(), errorDrawable.getIntrinsicHeight());

        status = (TextView) findViewById(R.id.status);
		dateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.FULL);
		timeFormat = SimpleDateFormat.getTimeInstance(SimpleDateFormat.DEFAULT);

		tests = (ListView) findViewById(R.id.tests);
		ArrayAdapter<String> adapter = new TestAdapter(this, R.layout.test, R.id.title);
		adapter.add("add 3 to 3.6;in [int, float] out double");
		tests.setAdapter(adapter);
		tests.setOnItemClickListener(testListener);
		
		QueryParams params = new QueryParams();
		params.setDataset("flickr");
		params.setDescriptor(".rgb");
		params.setDistanceFunction("L2");
		params.setResultPerPage(""  + 15);
		params.setQuery("100_400709508_1ec299db74_m.jpg");

		ImageSearch img = new ImageSearchImpl();
		String[] photos = img.getResultForQuery(params);
		if (photos != null) { 	
			String[] results = new String[photos.length];
			for (int i = 0; i < results.length; i++) {
				String imgurl = "http://192.168.1.39/flickr/" + photos[i]; 
				  
				final LoaderImageView image = new LoaderImageView(this, imgurl);
		        image.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		        this.addContentView(image, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

			}
		}
		

	}
	
	
	OnItemClickListener testListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (position == 0) {
				Object[] params = new Object[] { "configuration", "flickr", ".rgb" };
		        XMLRPCMethod method = new XMLRPCMethod("ImageSearch", new XMLRPCMethodCallback() {
					public void callFinished(Object result) {
						testResult.setText("OK " + result.toString());
					}
		        }); 
		        method.call(params);
			}  
		}
	};
	
 	
	class TestAdapter extends ArrayAdapter<String> {
		private LayoutInflater layouter;
		private int layoutId;
		public TestAdapter(Context context, int layoutId, int textId) {
			super(context, layoutId, textId);
			this.layoutId = layoutId;
			layouter = LayoutInflater.from(Test.this);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = layouter.inflate(layoutId, null);
			TextView title = (TextView) view.findViewById(R.id.title);
			TextView params = (TextView) view.findViewById(R.id.params);
			String string = getItem(position);
			String[] arr = string.split(";");
			title.setText(arr[0]);
			if (arr.length == 2) {
				params.setText(arr[1]);
			} else {
				params.setVisibility(View.GONE);
			}
			return view;
		}
	}

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
			status.setTextColor(0xff80ff80);
			status.setError(null);
			status.setText("Calling host " + uri.getHost());
			tests.setEnabled(false);
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
						tests.setEnabled(true);
						status.setText("XML-RPC call took " + (t1-t0) + "ms");
						callBack.callFinished(result);
					}
    			});
    		} catch (final XMLRPCFault e) {
    			handler.post(new Runnable() {
					public void run() {
						testResult.setText("");
						tests.setEnabled(true);
						status.setTextColor(0xffff8080);
						status.setError("", errorDrawable);
						status.setText("Fault message: " + e.getFaultString() + "\nFault code: " + e.getFaultCode());
						Log.d("Test", "error", e);
					}
    			});
    		} catch (final XMLRPCException e) {
    			handler.post(new Runnable() {
					public void run() {
						testResult.setText("");
						tests.setEnabled(true);
						status.setTextColor(0xffff8080);
						status.setError("", errorDrawable);
						Throwable couse = e.getCause();
						if (couse instanceof HttpHostConnectException) {
							status.setText("Cannot connect to " + uri.getHost() + "\nMake sure server.py on your development host is running !!!");
						} else {
							status.setText("Error " + e.getMessage());
						}
						Log.d("Test", "error", e);
					}
    			});
    		}
		}
	}
}
