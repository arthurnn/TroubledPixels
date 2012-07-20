package com.fivehundredpx.troubledpixels.tasks;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.fivehundredpx.api.ApiHelper;
import com.fivehundredpx.api.auth.AccessToken;

public class UserDetailTask extends AsyncTask<Object, Void, JSONObject> {
	private static final String TAG = "UserDetailTask";

	private String url = "/users.json";

	public interface Delegate {
		public void success(JSONObject user);
		public void fail();
	}

	private Delegate _d;

	public UserDetailTask(Delegate d) {
		this._d = d;
	}

	@Override
	protected JSONObject doInBackground(Object... params) {
		final AccessToken a = (AccessToken) params[0];
		JSONObject obj = ApiHelper.request(a, url);
		try {
			return obj.getJSONObject("user");
		} catch (JSONException e) {
			Log.e(TAG, "", e);
			return null;
		}
	}

	@Override
	protected void onPostExecute(JSONObject result) {

		if (result != null)
			_d.success(result);
	}

}
