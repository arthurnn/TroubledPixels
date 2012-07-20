package com.fivehundredpx.troubledpixels.tasks;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.fivehundredpx.api.auth.AccessToken;
import com.fivehundredpx.troubledpixels.Application;
import com.fivehundredpx.troubledpixels.R;

public class UserDetailTask extends AsyncTask<Object, Void, Void> {
	private static final String TAG = "UserDetailTask";
	
	private String HOST = "https://api.500px.com/v1";
	private String url = "/users.json";
	
	
	public interface Delegate {
		public void success(JSONObject user);
		public void fail();
	}

	private Delegate _d;
	
	public UserDetailTask(Delegate d){
		this._d = d;
	}
	
	@Override
	protected Void doInBackground(Object... params) {
		AccessToken token = (AccessToken) params[0];
		
		try{
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(HOST + url);
			Context c = Application.getContext();
			CommonsHttpOAuthConsumer consumer = new CommonsHttpOAuthConsumer(
					c.getString(R.string.px_consumer_key),
					c.getString(R.string.px_consumer_secret));
			
			consumer.setTokenWithSecret(token.getToken(),token.getTokenSecret());
			consumer.sign(request);
			
			HttpResponse response = client.execute(request);
			// debugResponse(response);
			final int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				final String msg = String.format("Error, statusCode not OK(%d). for url: %s", statusCode, url);
				Log.e(TAG, msg);
				return null;
			}

			HttpEntity responseEntity = response.getEntity();
			InputStream inputStream = responseEntity.getContent();
			BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
			StringBuilder total = new StringBuilder();
			String line;
			while ((line = r.readLine()) != null) {
			    total.append(line);
			}
			
			
			JSONObject json = new JSONObject(total.toString());
			
			this._d.success(json.getJSONObject("user"));
			
		}catch (Exception e) {
			Log.e(TAG, "", e);
		}
		

		
		// TODO Auto-generated method stub
		return null;
	}

}
