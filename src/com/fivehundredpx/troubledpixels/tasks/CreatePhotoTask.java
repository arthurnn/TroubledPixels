package com.fivehundredpx.troubledpixels.tasks;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.fivehundredpx.api.PxApi;
import com.fivehundredpx.api.auth.AccessToken;
import com.fivehundredpx.troubledpixels.Application;
import com.fivehundredpx.troubledpixels.R;

public class CreatePhotoTask extends AsyncTask<Object, Void, JSONObject> {

	private static final String TAG = "CreatePhotoTask";

	
	public interface Delegate {
		public void success(JSONObject obj);
		public void fail();
	}

	private Delegate _d;
	
	
	public CreatePhotoTask(Delegate _d) {
		super();
		this._d = _d;
	}

	@Override
	protected JSONObject doInBackground(Object... params) {
		AccessToken accessToken = (AccessToken) params[0];
		String name = (String) params[1];
		String desc = (String) params[2];
		final Context context = Application.getContext();

		final ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair("name", name));
		postParams.add(new BasicNameValuePair("description", desc));
		// dont post to profile. only to library
		postParams.add(new BasicNameValuePair("privacy", "1"));
		
		
		final PxApi api = new PxApi(accessToken,
				context.getString(R.string.px_consumer_key),
				context.getString(R.string.px_consumer_secret));
		JSONObject json = api.post("/photos",postParams);
		
		try {
			Log.w(TAG,json.getJSONObject("photo").getString("id"));
			Log.w(TAG,json.getString("upload_key"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return json;
	}
	
	@Override
	protected void onPostExecute(JSONObject result) {

		if(null!=result)
			_d.success(result);
	}
}
