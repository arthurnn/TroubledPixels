package com.fivehundredpx.troubledpixels.tasks;

import java.io.InputStream;
import java.net.URL;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

public class ImageDownloadTask extends AsyncTask<String, Void, Drawable> {
	
	public interface Delegate {
		public void success(Drawable d);
		public void fail();
	}

	private Delegate _d;
	
	
    public ImageDownloadTask(Delegate _d) {
		super();
		this._d = _d;
	}

	private Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
        	
        	Log.e("ImageDownloadTask", "", e);
            return null;
        }
    }

	@Override
	protected Drawable doInBackground(String... params) {
		final String url = params[0];
		return LoadImageFromWebOperations(url);
	}
	
	@Override
	protected void onPostExecute(Drawable result) {
		if(null!=result)
			_d.success(result);
	
	}
	
	
}
