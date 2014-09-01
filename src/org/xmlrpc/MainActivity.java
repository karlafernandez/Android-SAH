package org.xmlrpc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.os.Bundle;
import android.os.Environment;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;


@SuppressLint("NewApi")
public class MainActivity extends Activity {
	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Bitmap bitmap = DownloadImage("http://developer.android.com/assets/images/dac_logo.png");
		
	
		 
		final LoaderImageView image = new LoaderImageView(this, "http://www.elearningguild.com/assets/images/MLC13/stages/stage-jeff-batt_185.jpg");
        image.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
        this.addContentView(image, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
		//imageView.setImageBitmap(bitmap);
	}
	
	 

	private Drawable LoadImageFromWebOperations(String url) {
	    try
	      {
	       InputStream is = (InputStream) new URL(url).getContent();
	       Drawable d = Drawable.createFromStream(is, "src name");
	       return d;
	      }catch (Exception e) {
	    	  return null;
	      }
	}
    private InputStream OpenHttpConnection(String urlString) throws IOException {
        InputStream in = null;
        int response = -1;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");

        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        } catch (Exception ex) {
            throw new IOException("Error connecting");
        }
        return in;
    }

    private Bitmap DownloadImage(String URL) {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return bitmap;
    }

 
}
