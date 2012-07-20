package com.fivehundredpx.troubledpixels.tasks;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.fivehundredpx.api.ApiHelper;
import com.fivehundredpx.api.auth.AccessToken;

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
		
		
		JSONObject json = ApiHelper.requestPost(accessToken, "/photos?name=Hello&description=World");
		
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
