package org.xmlrpc;


import java.io.IOException;
import java.net.MalformedURLException;
  
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
  
 public class LoaderImageView extends LinearLayout{
  
        private static final int COMPLETE = 0;
        private static final int FAILED = 1;
  
        private Context mContext;
        private Drawable mDrawable;
        private ProgressBar mSpinner;
        private ImageView mImage;
        
         public LoaderImageView(final Context context, final AttributeSet attrSet) {
                super(context, attrSet);
                final String url = attrSet.getAttributeValue(null, "image");
                if(url != null){
                        instantiate(context, url);
                } else {
                        instantiate(context, null);
                }
        }
        
         public LoaderImageView(final Context context, final String imageUrl) {
                super(context);
                instantiate(context, imageUrl);        
        }
         private void instantiate(final Context context, final String imageUrl) {
                mContext = context;
                
                mImage = new ImageView(mContext);
                mImage.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                
                mSpinner = new ProgressBar(mContext);
                mSpinner.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                        
                mSpinner.setIndeterminate(true);
                
                addView(mSpinner);
                addView(mImage);
                
                if(imageUrl != null){
                        setImageDrawable(imageUrl);
                }
        }
         public void setImageDrawable(final String imageUrl) {
                mDrawable = null;
                mSpinner.setVisibility(View.VISIBLE);
                mImage.setVisibility(View.GONE);
                new Thread(){
                        public void run() {
                                try {
                                        mDrawable = getDrawableFromUrl(imageUrl);
                                        imageLoadedHandler.sendEmptyMessage(COMPLETE);
                                } catch (MalformedURLException e) {
                                        imageLoadedHandler.sendEmptyMessage(FAILED);
                                } catch (IOException e) {
                                        imageLoadedHandler.sendEmptyMessage(FAILED);
                                }
                        };
                }.start();
        }
         private final Handler imageLoadedHandler = new Handler(new Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                        switch (msg.what) {
                        case COMPLETE:
                                mImage.setImageDrawable(mDrawable);
                                mImage.setVisibility(View.VISIBLE);
                                mSpinner.setVisibility(View.GONE);
                                break;
                        case FAILED:
                        default:
                                // Could change image here to a 'failed' image
                                // otherwise will just keep on spinning
                                break;
                        }
                        return true;
                }              
        });
  
         private static Drawable getDrawableFromUrl(final String url) throws IOException, MalformedURLException {
                return Drawable.createFromStream(((java.io.InputStream)new java.net.URL(url).getContent()), "name");
        }
        
}